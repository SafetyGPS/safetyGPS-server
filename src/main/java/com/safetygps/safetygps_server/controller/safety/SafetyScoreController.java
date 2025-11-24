package com.safetygps.safetygps_server.controller.safety;

import com.safetygps.safetygps_server.controller.safety.response.SafetyScoreResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Safety Score API", description = "주소 기반 안전지수 조회 API")
@RequestMapping("/api/safety")
public interface SafetyScoreController {

    @Operation(
            summary = "동/읍/면 안전점수 조회",
            description = "주소(예: 수원시 장안구 연무동)를 이용해 CCTV/보안등/치안센터 개수를 집계하고 가중 점수를 계산합니다."
    )
    @GetMapping("/score")
    ResponseEntity<SafetyScoreResponse> getSafetyScore(
            @Parameter(description = "시/구/동 형식의 주소", example = "수원시 장안구 연무동")
            @RequestParam String address
    );
}
