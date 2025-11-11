package com.safetygps.safetygps_server.domain.securitylight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SecurityLightRecord(
        @JsonProperty("보안등위치명")
        String lampLocationName,

        @JsonProperty("소재지도로명주소")
        String roadAddress,

        @JsonProperty("소재지지번주소")
        String lotAddress,

        @JsonProperty("위도")
        String latitude,

        @JsonProperty("경도")
        String longitude
) {}
