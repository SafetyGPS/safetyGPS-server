package com.safetygps.safetygps_server.repository.securitylight;

import com.safetygps.safetygps_server.domain.securitylight.SecurityLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SecurityLightRepository extends JpaRepository<SecurityLight, Long> {

    @Query("""
        SELECT DISTINCT s FROM SecurityLight s
        WHERE (
            :fullKeyword IS NOT NULL AND (
                (s.roadAddress IS NOT NULL AND LOWER(s.roadAddress) LIKE LOWER(CONCAT('%', :fullKeyword, '%')))
             OR (s.lotAddress IS NOT NULL AND LOWER(s.lotAddress) LIKE LOWER(CONCAT('%', :fullKeyword, '%')))
            )
        )
        OR (
            :lowestUnit IS NOT NULL AND (
                (s.roadAddress IS NOT NULL AND LOWER(s.roadAddress) LIKE LOWER(CONCAT('%', :lowestUnit, '%')))
             OR (s.lotAddress IS NOT NULL AND LOWER(s.lotAddress) LIKE LOWER(CONCAT('%', :lowestUnit, '%')))
            )
        )
    """)
    List<SecurityLight> searchByAddressKeywords(
            @Param("fullKeyword") String fullKeyword,
            @Param("lowestUnit") String lowestUnit
    );
}
