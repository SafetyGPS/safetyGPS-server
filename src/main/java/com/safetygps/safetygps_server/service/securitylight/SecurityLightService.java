package com.safetygps.safetygps_server.service.securitylight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetygps.safetygps_server.controller.securitylight.response.SecurityLightResponse;
import com.safetygps.safetygps_server.domain.securitylight.SecurityLightRecord;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityLightService {

    private static final TypeReference<List<SecurityLightRecord>> RECORD_LIST_TYPE =
            new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    private List<SecurityLightRecord> cachedRecords = Collections.emptyList();

    @PostConstruct
    void loadSecurityLightData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/security_light.json")) {
            if (inputStream == null) {
                log.error("❌ 보안등 JSON 파일을 찾을 수 없습니다. (경로: /data/security_light.json)");
                return;
            }

            JsonNode root = objectMapper.readTree(inputStream);
            JsonNode recordsNode = root.path("records");

            if (!recordsNode.isArray()) {
                log.error("❌ 보안등 JSON 구조가 예상과 다릅니다. 'records' 배열을 찾을 수 없습니다.");
                return;
            }

            cachedRecords = objectMapper.readValue(recordsNode.traverse(), RECORD_LIST_TYPE);
            log.info("보안등 데이터 {}건 로드 완료", cachedRecords.size());
        } catch (IOException e) {
            log.error("❌ 보안등 JSON 파싱 중 오류가 발생했습니다.", e);
        }
    }

    public List<SecurityLightResponse> findByAddress(String addressKeyword) {
        if (addressKeyword == null || addressKeyword.isBlank() || cachedRecords.isEmpty()) {
            return List.of();
        }

        final String trimmedKeyword = addressKeyword.trim();
        final String lowestUnit = extractLowestUnit(addressKeyword);

        return cachedRecords.stream()
                .filter(record -> matches(record, trimmedKeyword, lowestUnit))
                .map(record -> new SecurityLightResponse(
                        record.lampLocationName(),
                        toDouble(record.latitude()),
                        toDouble(record.longitude())
                ))
                .toList();
    }

    private boolean matches(SecurityLightRecord record, String fullKeyword, String lowestUnit) {
        return contains(record.roadAddress(), fullKeyword)
                || contains(record.lotAddress(), fullKeyword)
                || (lowestUnit != null && (contains(record.roadAddress(), lowestUnit)
                || contains(record.lotAddress(), lowestUnit)));
    }

    private boolean contains(String source, String keyword) {
        return source != null && keyword != null && source.contains(keyword);
    }

    private String extractLowestUnit(String addressKeyword) {
        String[] parts = addressKeyword.trim().split("\\s+");
        if (parts.length == 0) {
            return null;
        }
        String candidate = parts[parts.length - 1];
        return candidate.isBlank() ? null : candidate;
    }

    private Double toDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.warn("⚠️ 위도/경도 숫자 변환 실패: {}", value);
            return null;
        }
    }
}
