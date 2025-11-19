package com.safetygps.safetygps_server.controller.securitylight;

import com.safetygps.safetygps_server.controller.securitylight.response.SecurityLightResponse;
import com.safetygps.safetygps_server.service.securitylight.SecurityLightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SecurityLightControllerImpl implements SecurityLightController {

    private final SecurityLightService securityLightService;

    @Override
    public ResponseEntity<List<SecurityLightResponse>> getSecurityLights(String addressKeyword) {
        return ResponseEntity.ok(securityLightService.findByAddress(addressKeyword));
    }

    @Override
    public ResponseEntity<String> syncSecurityLights() {
        securityLightService.syncSecurityLights();
        return ResponseEntity.ok("✅ 보안등 데이터 동기화 완료");
    }
}
