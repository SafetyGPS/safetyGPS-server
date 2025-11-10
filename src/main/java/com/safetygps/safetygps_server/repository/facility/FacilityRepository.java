package com.safetygps.safetygps_server.repository.facility;

import com.safetygps.safetygps_server.domain.facility.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findBySigunNm(String sigunNm);
    List<Facility> findBySigunNmAndGu(String sigunNm, String gu);
    List<Facility> findBySigunNmAndGuAndDong(String sigunNm, String gu, String dong);
    boolean existsByInstitutionNameAndRoadAddress(String institutionName, String roadAddress);

}