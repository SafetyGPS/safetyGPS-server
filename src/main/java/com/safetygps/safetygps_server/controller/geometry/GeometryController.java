package com.safetygps.safetygps_server.controller.geometry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetygps.safetygps_server.service.geometry.GeometryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GeometryController {

    private static final Logger logger = LoggerFactory.getLogger(GeometryController.class);
    private final GeometryService geometryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeometryController(GeometryService geometryService) {
        this.geometryService = geometryService;
    }

    @GetMapping("/api/geometry/coords")
    public ResponseEntity<JsonNode> getCoordinates(@RequestParam String geometryUrl) {
        try {
            JsonNode coordinates = geometryService.fetchCoordinates(geometryUrl);
            return ResponseEntity.ok(coordinates);
        } catch (Exception e) {
            logger.error("Failed to fetch coordinates from URL: {}", geometryUrl, e);
            return ResponseEntity
                    .badRequest()
                    .body(objectMapper.valueToTree(
                            Map.of("error", "Failed to fetch geometry coordinates",
                                    "message", e.getMessage())));
        }
    }
}
