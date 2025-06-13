package com.example.DateCock.Repository;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.DateCourseReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface DateCourseReservationRepository extends JpaRepository<DateCourseReservationEntity,String> {

    @Query(value = "SELECT * FROM DATECOCKRESERVATION WHERE DATECOCKRESERVATION.businessname=:businessname AND DATECOCKRESERVATION.day=:day",nativeQuery = true)
    DateCourseReservationEntity reservationOut(@Param("businessname") String businessname, @Param("day") Date day);

    @Query(value = "SELECT * FROM DATECOCKRESERVATION WHERE DATECOCKRESERVATION.id =:id order by day asc",nativeQuery = true )
    List<DateCourseReservationEntity> allReservations(@Param("id") String id);

    @Query(value = "SELECT * FROM DATECOCKBUSINESSINPUT WHERE DATECOCKBUSINESSINPUT.businessname=:businessname",nativeQuery = true)
    BusinessEntity ditails(@Param("businessname") String businessname);

    @Query(value = "SELECT RESERVATION_ID as reservation_id, ID, NAME, PHONE, DAY, INTIME, BUSINESSNAME, IMAGE FROM DATECOCKRESERVATION WHERE DATECOCKRESERVATION.id =:id AND DATECOCKRESERVATION.businessname =:businessname AND DATECOCKRESERVATION.day =:day",nativeQuery = true)
    DateCourseReservationEntity datereservationselect(@Param("id") String id,@Param("businessname") String businessname,@Param("day") Date day);

    @Transactional
    @Modifying
    @Query(value = " DELETE FROM DATECOCKRESERVATION WHERE DATECOCKRESERVATION.id =:id AND DATECOCKRESERVATION.name=:name AND DATECOCKRESERVATION.day=:day AND DATECOCKRESERVATION.businessname =:businessname",nativeQuery = true)
    int datereservationdelete(@Param("id") String id,@Param("name")String name,@Param("businessname") String businessname,@Param("day") Date day);
}
