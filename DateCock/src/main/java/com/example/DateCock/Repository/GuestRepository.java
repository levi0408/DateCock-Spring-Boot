package com.example.DateCock.Repository;

import com.example.DateCock.Entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity,Long> {

    void deleteByNameAndTodays(String name, Timestamp todays);
    List<GuestEntity> findAllByOrderByTodaysDesc();
}
