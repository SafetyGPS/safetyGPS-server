package com.safetygps.safetygps_server.repository.review;

import com.safetygps.safetygps_server.domain.review.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    @Query("""
        SELECT r FROM UserReview r
        WHERE (:sigunNm IS NULL OR r.sigunNm = :sigunNm)
          AND (:gu IS NULL OR r.gu = :gu)
          AND (:dong IS NULL OR r.dong = :dong)
        ORDER BY r.createdAt DESC
    """)
    List<UserReview> findByLocation(
            @Param("sigunNm") String sigunNm,
            @Param("gu") String gu,
            @Param("dong") String dong
    );

    @Query("""
        SELECT COALESCE(SUM(r.rating), 0) FROM UserReview r
        WHERE (:sigunNm IS NULL OR r.sigunNm = :sigunNm)
          AND (:gu IS NULL OR r.gu = :gu)
          AND (:dong IS NULL OR r.dong = :dong)
    """)
    long sumRatingByLocation(
            @Param("sigunNm") String sigunNm,
            @Param("gu") String gu,
            @Param("dong") String dong
    );
}
