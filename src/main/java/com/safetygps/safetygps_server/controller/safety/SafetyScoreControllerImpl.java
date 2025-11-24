package com.safetygps.safetygps_server.controller.safety;

import com.safetygps.safetygps_server.controller.safety.response.SafetyScoreResponse;
import com.safetygps.safetygps_server.service.safety.SafetyScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SafetyScoreControllerImpl implements SafetyScoreController {

    private final SafetyScoreService safetyScoreService;

    @Override
    public ResponseEntity<SafetyScoreResponse> getSafetyScore(String address) {
        return ResponseEntity.ok(safetyScoreService.calculateSafetyScore(address));
    }
}
