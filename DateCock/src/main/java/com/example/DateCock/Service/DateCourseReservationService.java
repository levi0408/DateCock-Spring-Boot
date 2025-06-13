package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.DateCourseReservationEntity;

import java.sql.Date;
import java.util.List;

public interface DateCourseReservationService {
    DateCourseReservationEntity reservationOut(String businessname, Date day);

    void insert(DateCourseReservationEntity dateentity);

    List<DateCourseReservationEntity> allReservations(String id);

    BusinessEntity businessinputselect(String businessname);

    DateCourseReservationEntity datereservationselect(String id, String businessname, Date day);

    int datereservationdelete(String id, String name,String businessname, Date day);
}
