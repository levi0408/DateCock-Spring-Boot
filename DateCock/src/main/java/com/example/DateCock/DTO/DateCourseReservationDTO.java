package com.example.DateCock.DTO;

import com.example.DateCock.Entity.DateCourseReservationEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DateCourseReservationDTO {
    Long reservationId;
    String name;
    String phone;
    Date day;
    String intime;
    String businessname;
    String id;
    String image;

    public DateCourseReservationEntity entity() {
        return new DateCourseReservationEntity(reservationId,name,phone,day,intime,businessname,id,image);
    }
}
