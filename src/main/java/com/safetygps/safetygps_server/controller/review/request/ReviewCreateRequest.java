package com.safetygps.safetygps_server.controller.review.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "리뷰 생성 요청 DTO")
public record ReviewCreateRequest(

        @Schema(description = "작성자 이름", example = "홍길동")
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(max = 50, message = "이름은 50자를 넘을 수 없습니다.")
        String name,

        @Schema(description = "리뷰 본문", example = "안전해서 좋았어요!")
        @NotBlank(message = "본문 내용은 필수입니다.")
        @Size(max = 2000, message = "본문은 2000자를 넘을 수 없습니다.")
        String content,

        @Schema(description = "별점 (1~5)", example = "5")
        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5를 넘을 수 없습니다.")
        int rating,

        @Schema(description = "주소 (예: 수원시 장안구 연무동)", example = "수원시 장안구 연무동")
        @NotBlank(message = "주소는 필수입니다.")
        String address
) {
}
