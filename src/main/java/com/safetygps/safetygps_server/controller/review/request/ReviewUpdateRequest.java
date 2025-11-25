package com.safetygps.safetygps_server.controller.review.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "리뷰 수정 요청 DTO")
public record ReviewUpdateRequest(

        @Schema(description = "수정할 작성자 이름", example = "김안심")
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(max = 50, message = "이름은 50자를 넘을 수 없습니다.")
        String name,

        @Schema(description = "수정할 본문", example = "야간에도 밝고 안전했어요.")
        @NotBlank(message = "본문 내용은 필수입니다.")
        @Size(max = 2000, message = "본문은 2000자를 넘을 수 없습니다.")
        String content,

        @Schema(description = "수정할 별점 (1~5)", example = "4")
        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5를 넘을 수 없습니다.")
        int rating,

        @Schema(description = "변경할 주소", example = "고양시 일산동구 백석동")
        @NotBlank(message = "주소는 필수입니다.")
        String address
) {
}
