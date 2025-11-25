package com.safetygps.safetygps_server.controller.safety.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주소별 안전 점수 응답 DTO")
public record SafetyScoreResponse(
        @Schema(description = "요청한 원본 주소")
        String requestAddress,

        @Schema(description = "시/군 이름")
        String sigunNm,

        @Schema(description = "구 이름")
        String gu,

        @Schema(description = "동/읍/면 이름")
        String dong,

        @Schema(description = "해당 지역 CCTV 개수")
        long cctvCount,

        @Schema(description = "해당 지역 보안등 개수")
        long securityLightCount,

        @Schema(description = "해당 지역 시설 개수")
        long facilityCount,

        @Schema(description = "해당 지역 사용자 리뷰 개수")
        long reviewCount,

        @Schema(description = "사용자 리뷰 평균 점수(0~5)")
        double reviewAverage,

        @Schema(description = "CCTV 지표 점수(0~100)")
        double cctvScore,

        @Schema(description = "보안등 지표 점수(0~100)")
        double securityLightScore,

        @Schema(description = "치안시설 지표 점수(0~100)")
        double facilityScore,

        @Schema(description = "사용자 리뷰 지표 점수(0~100, 표본수 가중 반영)")
        double reviewScore,

        @Schema(description = "가중치가 반영된 총 안전점수")
        long totalScore
) {
}
