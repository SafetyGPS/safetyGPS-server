package com.safetygps.safetygps_server.controller.facility;

import com.safetygps.safetygps_server.controller.facility.response.FacilityResponse;
import com.safetygps.safetygps_server.service.facility.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FacilityControllerImpl implements FacilityController {

    private final FacilityService facilityService;

    @Override
    public ResponseEntity<String> syncFromOpenApi(String sigunNm) {
        facilityService.syncFacilities(sigunNm);
        return ResponseEntity.ok("✅ " + sigunNm + " 시설 데이터 동기화 완료");
    }

    @Override
    public ResponseEntity<List<FacilityResponse>> getFacilities(String sigunNm, String gu, String dong) {
        return ResponseEntity.ok(facilityService.getFacilities(sigunNm, gu, dong));
    }
}
