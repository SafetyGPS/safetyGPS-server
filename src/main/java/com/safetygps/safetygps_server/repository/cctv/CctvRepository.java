package com.safetygps.safetygps_server.repository.cctv;

import com.safetygps.safetygps_server.domain.cctv.Cctv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CctvRepository extends JpaRepository<Cctv, Long> {

    @Query("""
            SELECT c FROM Cctv c
            WHERE c.address LIKE %:keyword%
               OR c.address LIKE %:lowestUnit%
            """)
    List<Cctv> searchByAddress(@Param("keyword") String keyword,
                               @Param("lowestUnit") String lowestUnit);
}
