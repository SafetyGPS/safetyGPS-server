package com.safetygps.safetygps_server.domain.cctv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CctvRecord(

        @JsonProperty("CCTV 도로명주소")
        String address,

        @JsonProperty("위도")
        String latitude,

        @JsonProperty("경도")
        String longitude
) {}
