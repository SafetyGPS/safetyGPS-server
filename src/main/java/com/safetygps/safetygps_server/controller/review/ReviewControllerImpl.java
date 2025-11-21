package com.safetygps.safetygps_server.controller.review;

import com.safetygps.safetygps_server.controller.review.request.ReviewCreateRequest;
import com.safetygps.safetygps_server.controller.review.request.ReviewUpdateRequest;
import com.safetygps.safetygps_server.controller.review.response.ReviewResponse;
import com.safetygps.safetygps_server.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @Override
    public ResponseEntity<ReviewResponse> createReview(@Valid ReviewCreateRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @Override
    public ResponseEntity<List<ReviewResponse>> getReviews(String sigunNm, String gu, String dong) {
        return ResponseEntity.ok(reviewService.getReviews(sigunNm, gu, dong));
    }

    @Override
    public ResponseEntity<ReviewResponse> getReview(Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @Override
    public ResponseEntity<ReviewResponse> updateReview(Long id, @Valid ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteReview(Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
