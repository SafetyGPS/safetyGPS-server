package com.safetygps.safetygps_server.controller.cctv;

import com.safetygps.safetygps_server.controller.geometry.GeometryController;
import com.safetygps.safetygps_server.domain.cctv.CctvResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.safetygps.safetygps_server.service.cctv.CctvService;

import java.util.List;

@Tag(name = "CCTV API", description = "CCTV 위치 데이터 조회 API")
@RestController
@RequestMapping("/api/cctv")
@RequiredArgsConstructor
public class CctvController {

    private final CctvService cctvService;

    @Operation(summary = "지역명으로 CCTV 조회", description = "입력된 동/읍/리에 해당하는 CCTV 위치 데이터를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CctvResponse>> getCctvByAddress( @Parameter(description = "예: 김량장동, 수지구 상현동 등") @RequestParam String region) {
        List<CctvResponse> cctvList = cctvService.findByAddressKeyword(region);
        return ResponseEntity.ok(cctvList);
    }
}