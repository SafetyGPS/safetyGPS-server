package com.safetygps.safetygps_server.controller.facilty;

import com.safetygps.safetygps_server.domain.facility.Facility;
import com.safetygps.safetygps_server.service.facility.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;


    @GetMapping("/sync")
    public ResponseEntity<?> syncFromOpenApi(@RequestParam String sigunNm) {
        facilityService.syncFacilities(sigunNm);
        return ResponseEntity.ok(sigunNm + " 시설 데이터 저장 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Facility>> getFacilities(
            @RequestParam String sigunNm,
            @RequestParam(required = false) String gu,
            @RequestParam(required = false) String dong
    ) {
        List<Facility> facilities = facilityService.getFacilities(sigunNm, gu, dong);
        return ResponseEntity.ok(facilities);
    }
}