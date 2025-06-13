package com.example.DateCock.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "DATECOCKRESERVATION")
@SequenceGenerator(
        name = "reservation",
        sequenceName = "DATECOCKRESERVATION_SEQ", // 실제 시퀀스 이름
        allocationSize = 1,
        initialValue = 1
)
public class DateCourseReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "reservation")
    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column
    String name ;

    @Column
    String phone;

    @Column
    Date day;

    @Column
    String intime;

    @Column
    String businessname;

    @Column
    String id;

    @Column
    String image;

}
