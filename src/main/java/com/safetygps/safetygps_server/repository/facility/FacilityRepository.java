package com.safetygps.safetygps_server.repository.facility;

import com.safetygps.safetygps_server.domain.facility.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findBySigunNm(String sigunNm);
    List<Facility> findBySigunNmAndGu(String sigunNm, String gu);
    List<Facility> findBySigunNmAndGuAndDong(String sigunNm, String gu, String dong);
    boolean existsByInstitutionNameAndRoadAddress(String institutionName, String roadAddress);

    @Query("""
        SELECT f FROM Facility f
        WHERE (:sigunNm IS NULL OR f.sigunNm = :sigunNm)
          AND (:gu IS NULL OR f.gu = :gu)
          AND (:dong IS NULL OR f.dong = :dong)
    """)
    List<Facility> findByConditions(
            @Param("sigunNm") String sigunNm,
            @Param("gu") String gu,
            @Param("dong") String dong
    );
}