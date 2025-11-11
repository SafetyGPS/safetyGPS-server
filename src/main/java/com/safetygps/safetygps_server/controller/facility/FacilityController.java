package com.safetygps.safetygps_server.controller.facility;

import com.safetygps.safetygps_server.controller.facility.response.FacilityResponse;
import com.safetygps.safetygps_server.domain.facility.Facility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Facility API", description = "공공데이터 기반 시설 관리 API")
public interface FacilityController {

    @Operation(
            summary = "시설 데이터 동기화",
            description = "경기도 공공데이터 포털로부터 시설 정보를 조회하고 DB에 저장합니다."
    )
    @PostMapping("/api/facilities/sync")
    ResponseEntity<String> syncFromOpenApi(
            @Parameter(description = "시군명 (예: 수원시, 고양시)", example = "수원시")
            @RequestParam String sigunNm
    );

    @Operation(
            summary = "시설 데이터 조회",
            description = "DB에 저장된 시설 데이터를 시/구/동 기준으로 조회합니다."
    )
    @GetMapping("/api/facilities")
    ResponseEntity<List<FacilityResponse>> getFacilities(
            @Parameter(description = "시군명", example = "수원시") @RequestParam String sigunNm,
            @Parameter(description = "구 이름", example = "팔달구") @RequestParam(required = false) String gu,
            @Parameter(description = "동 이름", example = "인계동") @RequestParam(required = false) String dong
    );
}
