package com.safetygps.safetygps_server.service.cctv;

import com.safetygps.safetygps_server.domain.cctv.CctvResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CctvService {

    public List<CctvResponse> findByAddressKeyword(String keyword) {
        List<CctvResponse> result = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream("/data/cctv_locations.xlsx")) {

            if (inputStream == null) {
                log.error("❌ CCTV Excel 파일을 찾을 수 없습니다. (경로: /data/cctv_locations.xlsx)");
                return result;
            }

            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // 헤더 스킵

                    String address = getCellValue(row.getCell(3)); // 소재지지번주소
                    String latStr = getCellValue(row.getCell(11)); // 위도
                    String lonStr = getCellValue(row.getCell(12)); // 경도

                    if (address.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) continue;

                    if (address.contains(keyword)) {
                        try {
                            double latitude = Double.parseDouble(latStr);
                            double longitude = Double.parseDouble(lonStr);
                            result.add(new CctvResponse(address, latitude, longitude));
                        } catch (NumberFormatException e) {
                            log.warn("⚠️ 위도/경도 변환 실패 (주소: {} | 위도: {} | 경도: {})", address, latStr, lonStr);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("❌ CCTV 엑셀 파일 읽는 중 오류 발생: ", e);
        }

        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue().trim();
                } catch (IllegalStateException e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            default -> "";
        };
    }
}
