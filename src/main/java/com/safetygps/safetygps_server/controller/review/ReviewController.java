package com.safetygps.safetygps_server.controller.review;

import com.safetygps.safetygps_server.controller.review.request.ReviewCreateRequest;
import com.safetygps.safetygps_server.controller.review.request.ReviewUpdateRequest;
import com.safetygps.safetygps_server.controller.review.response.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review API", description = "사용자 리뷰 CRUD API")
@RequestMapping("/api/reviews")
public interface ReviewController {

    @Operation(summary = "리뷰 등록", description = "사용자로부터 이름, 본문, 별점, 주소를 받아 리뷰를 저장합니다.")
    @PostMapping
    ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request);

    @Operation(summary = "리뷰 목록 조회", description = "시/구/동 조건으로 리뷰를 조회합니다. 파라미터는 선택입니다.")
    @GetMapping
    ResponseEntity<List<ReviewResponse>> getReviews(
            @Parameter(description = "시/군명", example = "수원시") @RequestParam(required = false) String sigunNm,
            @Parameter(description = "구 이름", example = "장안구") @RequestParam(required = false) String gu,
            @Parameter(description = "동/읍/면", example = "연무동") @RequestParam(required = false) String dong
    );

    @Operation(summary = "리뷰 단건 조회", description = "리뷰 식별자로 단건 조회합니다.")
    @GetMapping("/{id}")
    ResponseEntity<ReviewResponse> getReview(@PathVariable Long id);

    @Operation(summary = "리뷰 수정", description = "리뷰 내용을 수정합니다.")
    @PutMapping("/{id}")
    ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest request
    );

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteReview(@PathVariable Long id);
}
