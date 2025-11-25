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

    private static final long CCTV_WEIGHT = 2L;
    private static final long SECURITY_LIGHT_WEIGHT = 2L;
    private static final long FACILITY_WEIGHT = 50L;

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
        long reviewScoreSum = userReviewRepository.sumRatingByLocation(
                components.sigunNm(),
                components.gu(),
                components.dong()
        );

        long totalScore = cctvCount * CCTV_WEIGHT
                + securityLightCount * SECURITY_LIGHT_WEIGHT
                + facilityCount * FACILITY_WEIGHT
                + reviewScoreSum;

        return new SafetyScoreResponse(
                address.trim(),
                components.sigunNm(),
                components.gu(),
                components.dong(),
                cctvCount,
                securityLightCount,
                facilityCount,
                reviewScoreSum,
                totalScore
        );
    }
}
