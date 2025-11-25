package com.safetygps.safetygps_server.controller.review.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "리뷰 응답 DTO")
public record ReviewResponse(
        @Schema(description = "리뷰 식별자", example = "1")
        Long id,

        @Schema(description = "작성자 이름", example = "홍길동")
        String name,

        @Schema(description = "리뷰 본문", example = "야간에 순찰이 자주 있어 안심돼요.")
        String content,

        @Schema(description = "별점", example = "5")
        int rating,

        @Schema(description = "전체 주소", example = "수원시 장안구 연무동")
        String address,

        @Schema(description = "시/군", example = "수원시")
        String sigunNm,

        @Schema(description = "구", example = "장안구")
        String gu,

        @Schema(description = "동/읍/면", example = "연무동")
        String dong,

        @Schema(description = "생성일시")
        LocalDateTime createdAt,

        @Schema(description = "수정일시")
        LocalDateTime updatedAt
) {
}
