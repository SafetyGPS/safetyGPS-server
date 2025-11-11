package com.safetygps.safetygps_server.domain.facility;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "facility")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institutionName;   // 기관명
    private String facilityType;      // 시설구분명
    private String sigunNm;           // 시군명
    private String roadAddress;       // 도로명주소
    private String gu;                // 구 이름
    private String dong;              // 동/읍/면 이름

    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;      // 위도

    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;     // 경도
}
