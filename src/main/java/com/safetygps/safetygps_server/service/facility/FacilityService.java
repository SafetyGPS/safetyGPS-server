package com.safetygps.safetygps_server.service.facility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetygps.safetygps_server.domain.facility.Facility;

import com.safetygps.safetygps_server.repository.facility.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final RestTemplate restTemplate;
    private final FacilityRepository facilityRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gg-openapi.api-key}")
    private String apiKey;

    @Transactional
    public void syncFacilities(String sigunNm) {
        try {
            String url = "https://openapi.gg.go.kr/FiresttnPolcsttnM"
                    + "?KEY=" + apiKey
                    + "&Type=json"
                    + "&pIndex=1"
                    + "&pSize=100"
                    + "&SIGUN_NM=" + sigunNm;

            System.out.println("✅ 요청 URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            Map<String, Object> json = objectMapper.readValue(response.getBody(), Map.class);

            if (!json.containsKey("FiresttnPolcsttnM")) return;

            List<Map<String, Object>> wrapper = (List<Map<String, Object>>) json.get("FiresttnPolcsttnM");
            if (wrapper.size() < 2) return;

            List<Map<String, Object>> rows = (List<Map<String, Object>>) wrapper.get(1).get("row");

            for (Map<String, Object> row : rows) {
                String institutionName = (String) row.get("INST_NM");
                String address = (String) row.get("REFINE_LOTNO_ADDR");

                boolean exists = facilityRepository.existsByInstitutionNameAndRoadAddress(institutionName, address);
                if (exists) continue;
                Facility facility = Facility.builder()
                        .institutionName((String) row.get("INST_NM"))
                        .facilityType(row.get("FACLT_DIV_NM") != null ? (String) row.get("FACLT_DIV_NM") : "미지정")
                        .sigunNm(sigunNm)
                        .roadAddress(address)
                        .gu(extractGu(address))
                        .dong(extractDong(address))
                        .latitude(toBigDecimal(row.get("REFINE_WGS84_LAT")))
                        .longitude(toBigDecimal(row.get("REFINE_WGS84_LOGT")))
                        .build();

                facilityRepository.save(facility);
            }

            System.out.println(sigunNm + " 시설 데이터 저장 완료");

        } catch (Exception e) {
            throw new RuntimeException("DB 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return null;
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractGu(String address) {
        if (address == null) return null;
        for (String part : address.split(" ")) {
            if (part.endsWith("구")) return part;
        }
        return null;
    }

    private String extractDong(String address) {
        if (address == null) return null;
        for (String part : address.split(" ")) {
            if (part.endsWith("동") || part.endsWith("읍") || part.endsWith("면")) return part;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<Facility> getFacilities(String sigunNm, String gu, String dong) {
        if (dong != null && !dong.isBlank()) {
            return facilityRepository.findBySigunNmAndGuAndDong(sigunNm, gu, dong);
        } else if (gu != null && !gu.isBlank()) {
            return facilityRepository.findBySigunNmAndGu(sigunNm, gu);
        } else {
            return facilityRepository.findBySigunNm(sigunNm);
        }
    }
}
