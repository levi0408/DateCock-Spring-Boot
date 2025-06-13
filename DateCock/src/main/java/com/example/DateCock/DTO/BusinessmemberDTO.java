package com.example.DateCock.DTO;

import com.example.DateCock.Entity.BusinessmemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class BusinessmemberDTO {

        String businessnumber;
        String password;
        String email;
        String businessname;
        String phone;
        String birthyear;
        String businessnewpw;
        String businessnewpw_confirm;

        public BusinessmemberEntity entity() {
            return new BusinessmemberEntity(businessnumber,password,email,businessname,phone,birthyear);
        }

        public BusinessmemberDTO(BusinessmemberEntity entity) {
            this.businessnumber = entity.getBusinessnumber();
            this.password = entity.getPassword();
            this.email = entity.getEmail();
            this.businessname = entity.getBusinessname();
            this.phone = entity.getPhone();
            this.birthyear = entity.getBirthyear();
        }
    }

