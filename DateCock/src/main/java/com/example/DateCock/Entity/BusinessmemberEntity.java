package com.example.DateCock.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Table(name = "datecockbusinessmember")
    public class BusinessmemberEntity {

        @Id
        @Column
        String businessnumber;
        @Column
        String password;
        @Column
        String email;
        @Column
        String businessname;
        @Column
        String phone;
        @Column
        String birthyear;

        public String getBusinesspw() {
            return "password";
        }
    }


