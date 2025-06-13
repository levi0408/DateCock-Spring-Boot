package com.example.DateCock.DTO;

import com.example.DateCock.Entity.BusinessEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class BusinessDTO {
    String businessnumber;
    String businessname;
    String address;
    String businesstime;
    String phone;
    String image1;
    String information;
    int age;
    String zone;
    String activity;

    public BusinessEntity entity() {
        return new BusinessEntity(businessnumber,businessname,address,businesstime,phone,image1,information,age,zone,activity);
    }
    public BusinessDTO(String businessname, String zone, String activity, int age, String image1, String address, String businesstime, String phone, String information) {
        this.businessname = businessname;
        this.zone = zone;
        this.activity = activity;
        this.age = age;
        this.image1 = image1;
        this.address = address;
        this.businesstime = businesstime;
        this.phone = phone;
        this.information = information;
    }
}
