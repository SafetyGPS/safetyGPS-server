package com.safetygps.safetygps_server.controller.securitylight.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "보안등 정보 응답 DTO")
public record SecurityLightResponse(
        @Schema(description = "보안등 위치명", example = "연무2-11")
        @JsonProperty("LMP_LC_NM")
        String lampLocationName,

        @Schema(description = "위도", example = "37.304872")
        @JsonProperty("LATITUDE")
        Double latitude,

        @Schema(description = "경도", example = "127.012345")
        @JsonProperty("LONGITUDE")
        Double longitude
) {}
