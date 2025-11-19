package com.safetygps.safetygps_server.service.cctv;

import com.safetygps.safetygps_server.domain.cctv.Cctv;
import com.safetygps.safetygps_server.domain.cctv.CctvRecord;
import com.safetygps.safetygps_server.domain.cctv.CctvResponse;
import com.safetygps.safetygps_server.repository.cctv.CctvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CctvService {

    private final CctvRepository cctvRepository;

    /**
     * 특정 동/읍/리만 로드해서 DB 저장
     */
    @Transactional
    public void syncCctvData(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            log.error("❌ syncCctvData 호출 시 keyword가 비어있음");
            return;
        }

        String lowestUnit = extractLowestUnit(keyword);

        log.info("📌 요청한 지역 단위: {}", lowestUnit);

        List<CctvRecord> records = loadFromExcelByDong(lowestUnit);

        if (records.isEmpty()) {
            log.warn("⚠️ '{}' 에 해당하는 CCTV 데이터 없음", lowestUnit);
            return;
        }

        List<Cctv> cctvs = records.stream()
                .map(this::toEntity)
                .toList();

        cctvRepository.deleteAllInBatch();
        cctvRepository.saveAll(cctvs);

        log.info("📌 '{}' CCTV 데이터 {}건 DB 저장 완료", lowestUnit, cctvs.size());
    }


    /**
     * Excel을 읽되, keyword(동/읍/리) 해당 행만 필터링하여 로딩
     */
    private List<CctvRecord> loadFromExcelByDong(String dong) {
        List<CctvRecord> records = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream("/data/cctv_locations.xlsx")) {

            if (inputStream == null) {
                log.error("❌ CCTV Excel 파일 없음: /data/cctv_locations.xlsx");
                return records;
            }

            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // 헤더 스킵

                    String address = getCellValue(row.getCell(3));
                    String latStr = getCellValue(row.getCell(11));
                    String lonStr = getCellValue(row.getCell(12));

                    if (address.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) continue;

                    // 🔥 동/읍/리 기준 필터링
                    if (!address.contains(dong)) continue;

                    records.add(new CctvRecord(address, latStr, lonStr));
                }
            }

        } catch (Exception e) {
            log.error("❌ CCTV Excel 읽기 오류: ", e);
        }

        return records;
    }


    /**
     * DB 조회 (이건 그대로)
     */
    @Transactional(readOnly = true)
    public List<CctvResponse> findByAddressKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();

        final String trimmed = keyword.trim();
        final String lowestUnit = extractLowestUnit(keyword);

        return cctvRepository.searchByAddress(trimmed, lowestUnit)
                .stream()
                .map(c -> new CctvResponse(
                        c.getAddress(),
                        c.getLatitude().doubleValue(),
                        c.getLongitude().doubleValue()
                ))
                .toList();
    }


    private Cctv toEntity(CctvRecord r) {
        return Cctv.builder()
                .address(r.address())
                .latitude(toBigDecimal(r.latitude()))
                .longitude(toBigDecimal(r.longitude()))
                .build();
    }

    private BigDecimal toBigDecimal(String v) {
        if (v == null || v.isBlank()) return null;
        try {
            return new BigDecimal(v);
        } catch (NumberFormatException e) {
            log.warn("⚠️ 숫자 변환 실패: {}", v);
            return null;
        }
    }

    /**
     * "서울 강남구 역삼동" → "역삼동"
     */
    private String extractLowestUnit(String keyword) {
        String[] parts = keyword.trim().split("\\s+");
        return parts.length == 0 ? null : parts[parts.length - 1];
    }


    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
