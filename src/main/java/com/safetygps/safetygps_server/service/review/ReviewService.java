package com.safetygps.safetygps_server.service.review;

import com.safetygps.safetygps_server.controller.review.request.ReviewCreateRequest;
import com.safetygps.safetygps_server.controller.review.request.ReviewUpdateRequest;
import com.safetygps.safetygps_server.controller.review.response.ReviewResponse;
import com.safetygps.safetygps_server.domain.review.UserReview;
import com.safetygps.safetygps_server.repository.review.UserReviewRepository;
import com.safetygps.safetygps_server.service.common.AddressComponents;
import com.safetygps.safetygps_server.service.common.AddressParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserReviewRepository userReviewRepository;

    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest request) {
        AddressComponents parts = AddressParser.parse(request.address());
        UserReview review = UserReview.builder()
                .writerName(request.name().trim())
                .content(request.content().trim())
                .rating(request.rating())
                .fullAddress(request.address().trim())
                .sigunNm(parts.sigunNm())
                .gu(parts.gu())
                .dong(parts.dong())
                .build();

        UserReview saved = userReviewRepository.save(review);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(String sigunNm, String gu, String dong) {
        return userReviewRepository.findByLocation(
                        trimToNull(sigunNm),
                        trimToNull(gu),
                        trimToNull(dong))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        UserReview review = userReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
        return toResponse(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long id, ReviewUpdateRequest request) {
        UserReview review = userReviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        AddressComponents parts = AddressParser.parse(request.address());
        review.update(
                request.name().trim(),
                request.content().trim(),
                request.rating(),
                request.address().trim(),
                parts.sigunNm(),
                parts.gu(),
                parts.dong()
        );

        return toResponse(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!userReviewRepository.existsById(id)) {
            throw new ReviewNotFoundException(id);
        }
        userReviewRepository.deleteById(id);
    }

    private ReviewResponse toResponse(UserReview review) {
        return new ReviewResponse(
                review.getId(),
                review.getWriterName(),
                review.getContent(),
                review.getRating(),
                review.getFullAddress(),
                review.getSigunNm(),
                review.getGu(),
                review.getDong(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
