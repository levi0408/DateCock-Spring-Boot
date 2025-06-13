package com.example.DateCock.Service;


import com.example.DateCock.DTO.BusinessmemberDTO;
import com.example.DateCock.Entity.BusinessmemberEntity;

public interface BusinessmemberService {
    void businessmembersave(BusinessmemberEntity businessmemberEntity);


    BusinessmemberEntity modifyview(String businessnumber);

    void modifygo(BusinessmemberEntity businessmemberEntity);

    BusinessmemberEntity getbusinesspw(String businessnumber);

    void businessinfodelete(String businessnumber);

    boolean MatchBusinessNumber(String businessnumber);

    boolean MatchBusinessnumberEmail(String businessnumber, String email);

    String SendCode(String email);

    String NewPassword(String businessnumber);

    BusinessmemberDTO findbusinessnumber(String businessname, String email);
}
