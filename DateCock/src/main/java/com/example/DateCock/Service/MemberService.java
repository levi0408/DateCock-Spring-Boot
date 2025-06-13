package com.example.DateCock.Service;

import com.example.DateCock.Entity.MemberEntity;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    void insert(MemberEntity member);

    // id와 email로 회원 존재 여부를 확인하는 메서드 추가
    boolean checkMemberExists(String id, String email);

    boolean isUserIdExists(String id);

    String findUserIdByNameAndEmail(String name, String email);

    // 이메일 인증 관련 메서드 추가
    void sendVerificationEmail(String email, String verificationCode);

    void updatePassword(String id, String newEncodedPassword); // 비밀번호 재설정 메서드 추가

    String userName(String userid);

    String userPhone(String userid);

    void userinfodelete(String id);

    void userinfomodify(MemberEntity memberentity);

    MemberEntity usermodifyview(String id);

    MemberEntity userpwmodifyview(String id);

    MemberEntity getuserpw(String id);

    void userpwmodif(MemberEntity memberEntity);
}
