package com.safetygps.safetygps_server.controller.securitylight;

import com.safetygps.safetygps_server.controller.securitylight.response.SecurityLightResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Security Light API", description = "경기도 보안등 위치 조회 API")
@RequestMapping("/api/security-lights")
public interface SecurityLightController {

    @Operation(summary = "주소 키워드로 보안등 조회", description = "경기도 JSON 데이터에서 입력 주소(예: 수원시 장안구 연무동)에 해당하는 보안등 좌표를 반환합니다.")
    @GetMapping
    ResponseEntity<List<SecurityLightResponse>> getSecurityLights(
            @Parameter(description = "읍/면/동 또는 전체 주소 (예: 수원시 장안구 연무동)", required = true)
            @RequestParam("address") String addressKeyword);
}
