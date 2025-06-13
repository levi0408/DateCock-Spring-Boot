package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.DateCourseReservationEntity;
import com.example.DateCock.Repository.DateCourseReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class DateCourseReservationServiceImp implements DateCourseReservationService{
    @Autowired
    DateCourseReservationRepository dateCourseReservationRepository;

    @Override
    public DateCourseReservationEntity reservationOut(String businessname, Date day) {
        return dateCourseReservationRepository.reservationOut(businessname,day);
    }

    @Override
    public void insert(DateCourseReservationEntity dateentity) {
        dateCourseReservationRepository.save(dateentity);
    }

    @Override
    public List<DateCourseReservationEntity> allReservations(String id) {
        return dateCourseReservationRepository.allReservations(id);
    }

    @Override
    public BusinessEntity businessinputselect(String businessname) {
        return dateCourseReservationRepository.ditails(businessname);
    }

    @Override
    public DateCourseReservationEntity datereservationselect(String id, String businessname, Date day) {
        return dateCourseReservationRepository.datereservationselect(id,businessname,day);
    }

    @Override
    public int datereservationdelete(String id, String name,String businessname, Date day) {
        return dateCourseReservationRepository.datereservationdelete(id,name,businessname,day);
    }
}
