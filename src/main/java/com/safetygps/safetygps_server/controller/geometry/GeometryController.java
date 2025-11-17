package com.safetygps.safetygps_server.controller.geometry;

import com.fasterxml.jackson.databind.JsonNode;
import com.safetygps.safetygps_server.service.geometry.GeometryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeometryController {

    private final GeometryService geometryService;

    public GeometryController(GeometryService geometryService) {
        this.geometryService = geometryService;
    }

    @GetMapping("/api/geometry/coords")
    public ResponseEntity<JsonNode> getCoordinates(@RequestParam String geometryUrl) {
        try {
            JsonNode coordinates = geometryService.fetchCoordinates(geometryUrl);
            return ResponseEntity.ok(coordinates);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }
    }
}
