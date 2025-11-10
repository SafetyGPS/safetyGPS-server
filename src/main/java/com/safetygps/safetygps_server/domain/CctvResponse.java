package com.safetygps.safetygps_server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CctvResponse {
    private String address;
    private double latitude;
    private double longitude;
}