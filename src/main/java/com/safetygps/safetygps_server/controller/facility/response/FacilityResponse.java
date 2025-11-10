package com.safetygps.safetygps_server.controller.facility.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시설 정보 응답 DTO")
public record FacilityResponse(
        @Schema(description = "시설명", example = "경기도 수원소방서")
        String institutionName,

        @Schema(description = "시설 구분", example = "소방서")
        String facilityType,

        @Schema(description = "시군명", example = "수원시")
        String sigunNm,

        @Schema(description = "구 이름", example = "장안구")
        String gu,

        @Schema(description = "동/읍/면 이름", example = "정자동")
        String dong,

        @Schema(description = "도로명 주소", example = "경기도 수원시 장안구 정자천로 199")
        String roadAddress,

        @Schema(description = "위도", example = "37.2972036")
        String latitude,

        @Schema(description = "경도", example = "126.9959882")
        String longitude
) {}
