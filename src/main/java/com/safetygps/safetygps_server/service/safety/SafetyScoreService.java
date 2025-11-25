package com.safetygps.safetygps_server.service.safety;

import com.safetygps.safetygps_server.controller.safety.response.SafetyScoreResponse;
import com.safetygps.safetygps_server.repository.cctv.CctvRepository;
import com.safetygps.safetygps_server.repository.facility.FacilityRepository;
import com.safetygps.safetygps_server.repository.review.UserReviewRepository;
import com.safetygps.safetygps_server.repository.securitylight.SecurityLightRepository;
import com.safetygps.safetygps_server.service.common.AddressComponents;
import com.safetygps.safetygps_server.service.common.AddressParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SafetyScoreService {

    private static final double CCTV_WEIGHT = 0.30;
    private static final double SECURITY_LIGHT_WEIGHT = 0.25;
    private static final double FACILITY_WEIGHT = 0.20;
    private static final double REVIEW_WEIGHT = 0.25;

    // 기준값은 실제 데이터 분포에 맞춰 조정한다.
    private static final double CCTV_TARGET = 20.0;
    private static final double SECURITY_LIGHT_TARGET = 40.0;
    private static final double FACILITY_TARGET = 3.0;

    private static final double REVIEW_CONFIDENCE_TARGET = 10.0;
    private static final double MAX_RATING = 5.0;

    private final CctvRepository cctvRepository;
    private final SecurityLightRepository securityLightRepository;
    private final FacilityRepository facilityRepository;
    private final UserReviewRepository userReviewRepository;

    @Transactional(readOnly = true)
    public SafetyScoreResponse calculateSafetyScore(String address) {
        AddressComponents components = AddressParser.parse(address);

        String dongKeyword = components.dong();

        long cctvCount = cctvRepository.countByAddressContaining(dongKeyword);
        long securityLightCount = securityLightRepository.countByAddressKeyword(dongKeyword);
        long facilityCount = facilityRepository.countByLocation(
                components.sigunNm(),
                components.gu(),
                components.dong()
        );
        long reviewCount = userReviewRepository.countByLocation(
                components.sigunNm(),
                components.gu(),
                components.dong()
        );
        long reviewScoreSum = userReviewRepository.sumRatingByLocation(
                components.sigunNm(),
                components.gu(),
                components.dong()
        );

        double cctvScore = normalizedScore(cctvCount, CCTV_TARGET);
        double securityLightScore = normalizedScore(securityLightCount, SECURITY_LIGHT_TARGET);
        double facilityScore = normalizedScore(facilityCount, FACILITY_TARGET);
        double reviewAverage = reviewCount > 0 ? reviewScoreSum / (double) reviewCount : 0.0;
        double reviewScore = weightedReviewScore(reviewAverage, reviewCount);

        long totalScore = Math.round(
                cctvScore * CCTV_WEIGHT
                        + securityLightScore * SECURITY_LIGHT_WEIGHT
                        + facilityScore * FACILITY_WEIGHT
                        + reviewScore * REVIEW_WEIGHT
        );

        return new SafetyScoreResponse(
                address.trim(),
                components.sigunNm(),
                components.gu(),
                components.dong(),
                cctvCount,
                securityLightCount,
                facilityCount,
                reviewCount,
                reviewAverage,
                cctvScore,
                securityLightScore,
                facilityScore,
                reviewScore,
                totalScore
        );
    }

    private double normalizedScore(long count, double target) {
        if (target <= 0) {
            return 0.0;
        }
        // 포화 함수로 과도한 개수에 대한 체감 효과를 반영한다.
        double ratio = count / target;
        double saturated = 1 - Math.exp(-ratio);
        return Math.min(100.0, saturated * 100.0);
    }

    private double weightedReviewScore(double average, long reviewCount) {
        if (average <= 0 || reviewCount <= 0) {
            return 0.0;
        }
        double coverage = Math.min(Math.log1p(reviewCount) / Math.log1p(REVIEW_CONFIDENCE_TARGET), 1.0);
        double normalized = Math.max(0.0, Math.min(average / MAX_RATING, 1.0));
        return normalized * 100.0 * coverage;
    }
}
