package com.example.DateCock.DTO;


import com.example.DateCock.Entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class MemberDTO {
   String id;    // 아이디 (id)
     String password;  // 비밀번호 (pwd)
    // private String pwdConfirm; // 비밀번호 확인 필드는 DTO에 보통 포함하지 않음
    String email;     // 이메일
     String name;      // 이름
     String birth;     // 생년월일 (String 타입으로 받고, 필요시 Date로 변환)
     String phone;     // 전화번호
     int admin = 0;
    String password_confirm;
     public MemberEntity toEntity(){
         return new MemberEntity(id,password,email,name,birth,phone,admin);
     }

}


