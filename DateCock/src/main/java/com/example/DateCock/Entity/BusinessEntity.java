package com.example.DateCock.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Table(name="DATECOCKBUSINESSINPUT")
public class BusinessEntity {
   @Id
   @Column
    String businessnumber;
    @Column
    String businessname;
    @Column
    String address;
    @Column
    String businesstime;
    @Column
    String phone;
    @Column
    String image;
    @Column
    String information;
    @Column
    int age;
    @Column
    String zone;
    @Column
    String activity;
}
