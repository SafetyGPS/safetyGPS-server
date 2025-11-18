package com.safetygps.safetygps_server.service.geometry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeometryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public JsonNode fetchCoordinates(String geometryUrl) throws Exception {
        try {
            String geoJsonStr = restTemplate.getForObject(geometryUrl, String.class);
            if (geoJsonStr == null || geoJsonStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Empty response from geometry URL");
            }
            JsonNode root = objectMapper.readTree(geoJsonStr);

            JsonNode features = root.path("features");
            if (features.isMissingNode() || !features.isArray() || features.size() == 0) {
                throw new IllegalArgumentException("Invalid GeoJSON: features array is missing or empty");
            }
            JsonNode firstFeature = features.get(0);
            JsonNode geometry = firstFeature.path("geometry");
            JsonNode coordinates = geometry.path("coordinates");
            if (coordinates.isMissingNode()) {
                throw new IllegalArgumentException("Invalid GeoJSON: coordinates not found in first feature");
            }

            return objectMapper.createObjectNode().set("coordinates", coordinates);
        } catch (Exception e) {
            throw new Exception("Failed to fetch geometry coordinates: " + e.getMessage());
        }
    }
}
