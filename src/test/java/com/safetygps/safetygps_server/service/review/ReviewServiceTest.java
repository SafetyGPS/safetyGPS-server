package com.safetygps.safetygps_server.service.review;

import com.safetygps.safetygps_server.controller.review.request.ReviewCreateRequest;
import com.safetygps.safetygps_server.controller.review.request.ReviewUpdateRequest;
import com.safetygps.safetygps_server.controller.review.response.ReviewResponse;
import com.safetygps.safetygps_server.domain.review.UserReview;
import com.safetygps.safetygps_server.repository.review.UserReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private UserReviewRepository userReviewRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(userReviewRepository);
    }

    @Test
    void createReview_parsesAddressAndSavesEntity() {
        ReviewCreateRequest request = new ReviewCreateRequest(
                " 홍길동 ",
                " 안전해서 좋았어요 ",
                5,
                "수원시 장안구 연무동"
        );

        when(userReviewRepository.save(any(UserReview.class)))
                .thenAnswer(invocation -> {
                    UserReview toSave = invocation.getArgument(0);
                    return UserReview.builder()
                            .id(1L)
                            .writerName(toSave.getWriterName())
                            .content(toSave.getContent())
                            .rating(toSave.getRating())
                            .fullAddress(toSave.getFullAddress())
                            .sigunNm(toSave.getSigunNm())
                            .gu(toSave.getGu())
                            .dong(toSave.getDong())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                });

        ReviewResponse response = reviewService.createReview(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("홍길동");
        assertThat(response.sigunNm()).isEqualTo("수원시");
        assertThat(response.gu()).isEqualTo("장안구");
        assertThat(response.dong()).isEqualTo("연무동");

        ArgumentCaptor<UserReview> captor = ArgumentCaptor.forClass(UserReview.class);
        verify(userReviewRepository).save(captor.capture());
        UserReview saved = captor.getValue();
        assertThat(saved.getWriterName()).isEqualTo("홍길동");
        assertThat(saved.getFullAddress()).isEqualTo("수원시 장안구 연무동");
    }

    @Test
    void updateReview_appliesChangesToExistingEntity() {
        UserReview existing = UserReview.builder()
                .id(10L)
                .writerName("김안심")
                .content("초기 내용")
                .rating(3)
                .fullAddress("수원시 장안구 연무동")
                .sigunNm("수원시")
                .gu("장안구")
                .dong("연무동")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .build();

        when(userReviewRepository.findById(10L)).thenReturn(Optional.of(existing));

        ReviewUpdateRequest request = new ReviewUpdateRequest(
                "이안심",
                "야간에도 환해요",
                4,
                "수원시 장안구 영화동"
        );

        ReviewResponse response = reviewService.updateReview(10L, request);

        assertThat(response.name()).isEqualTo("이안심");
        assertThat(response.dong()).isEqualTo("영화동");
        assertThat(existing.getWriterName()).isEqualTo("이안심");
        assertThat(existing.getDong()).isEqualTo("영화동");
        verify(userReviewRepository).findById(10L);
    }

    @Test
    void deleteReview_missingIdThrowsException() {
        when(userReviewRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.deleteReview(99L))
                .isInstanceOf(ReviewNotFoundException.class);

        verify(userReviewRepository).existsById(99L);
        verify(userReviewRepository, never()).deleteById(any());
    }
}
