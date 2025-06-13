package com.example.DateCock.Repository;

import com.example.DateCock.DTO.BusinessDTO;
import com.example.DateCock.Entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, String> {

    @Query("SELECT b FROM BusinessEntity b WHERE b.age = :age AND b.zone = :zone AND b.activity = :activity")
    List<BusinessEntity> findByCondition(@Param("age") int age,
                                         @Param("zone") String zone,
                                         @Param("activity") String activity);

    @Query(value = "SELECT ADDRESS FROM DATECOCKBUSINESSINPUT WHERE DATECOCKBUSINESSINPUT.businessname =:businessname",nativeQuery = true)
    String businessAddressOut(@Param("businessname") String businessname);

    @Query(value = "SELECT * FROM DATECOCKBUSINESSINPUT WHERE DATECOCKBUSINESSINPUT.businessname =:businessname",nativeQuery = true)
    BusinessEntity datereservationOut(@Param("businessname") String businessname);

    List<BusinessEntity> findByZoneAndBusinessnameContaining(String zone, String keyword);

    // 전체 지역에서 가게명만 검색할 때 (기본 제공)
    List<BusinessEntity> findByBusinessnameContaining(String keyword);
}
