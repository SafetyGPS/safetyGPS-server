package com.safetygps.safetygps_server.service.securitylight;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetygps.safetygps_server.controller.securitylight.response.SecurityLightResponse;
import com.safetygps.safetygps_server.domain.securitylight.SecurityLight;
import com.safetygps.safetygps_server.domain.securitylight.SecurityLightRecord;
import com.safetygps.safetygps_server.repository.securitylight.SecurityLightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityLightService {

    private static final TypeReference<List<SecurityLightRecord>> RECORD_LIST_TYPE =
            new TypeReference<>() {};

    private final ObjectMapper objectMapper;
    private final SecurityLightRepository securityLightRepository;

    @Transactional
    public void syncSecurityLights() {
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

            List<SecurityLightRecord> records = objectMapper.readValue(recordsNode.traverse(), RECORD_LIST_TYPE);
            List<SecurityLight> securityLights = records.stream()
                    .map(this::toEntity)
                    .toList();

            securityLightRepository.deleteAllInBatch();
            securityLightRepository.saveAll(securityLights);
            log.info("보안등 데이터 {}건 DB 저장 완료", securityLights.size());
        } catch (IOException e) {
            log.error("❌ 보안등 JSON 파싱 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<SecurityLightResponse> findByAddress(String addressKeyword) {
        if (addressKeyword == null || addressKeyword.isBlank()) {
            return List.of();
        }

        final String trimmedKeyword = addressKeyword.trim();
        final String lowestUnit = extractLowestUnit(addressKeyword);

        List<SecurityLight> securityLights =
                securityLightRepository.searchByAddressKeywords(trimmedKeyword, lowestUnit);

        return securityLights.stream()
                .map(light -> new SecurityLightResponse(
                        light.getLampLocationName(),
                        toDouble(light.getLatitude()),
                        toDouble(light.getLongitude())
                ))
                .toList();
    }

    private String extractLowestUnit(String addressKeyword) {
        String[] parts = addressKeyword.trim().split("\\s+");
        if (parts.length == 0) {
            return null;
        }
        String candidate = parts[parts.length - 1];
        return candidate.isBlank() ? null : candidate;
    }

    private SecurityLight toEntity(SecurityLightRecord record) {
        return SecurityLight.builder()
                .lampLocationName(record.lampLocationName())
                .roadAddress(record.roadAddress())
                .lotAddress(record.lotAddress())
                .latitude(toBigDecimal(record.latitude()))
                .longitude(toBigDecimal(record.longitude()))
                .build();
    }

    private BigDecimal toBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.warn("⚠️ 위도/경도 숫자 변환 실패: {}", value);
            return null;
        }
    }

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
