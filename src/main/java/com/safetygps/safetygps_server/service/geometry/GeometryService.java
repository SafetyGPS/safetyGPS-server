package com.safetygps.safetygps_server.service.geometry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeometryService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode fetchCoordinates(String geometryUrl) throws Exception {
        try {
            String geoJsonStr = restTemplate.getForObject(geometryUrl, String.class);
            JsonNode root = objectMapper.readTree(geoJsonStr);

            JsonNode features = root.path("features");

            ObjectMapper mapper = new ObjectMapper();
            return mapper.createObjectNode().set("coordinates", features.get(0).path("geometry").path("coordinates"));
        } catch (Exception e) {
            throw new Exception("Failed to fetch geometry coordinates: " + e.getMessage());
        }
    }
}
