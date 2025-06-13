package com.example.DateCock.Service;

import com.example.DateCock.Entity.MemberEntity;
import com.example.DateCock.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JavaMailSender mailSender; // 이메일 발송을 위해 주입

    public void insert(MemberEntity member){
        memberRepository.save(member);
    }

    /**
     * 사용자 아이디가 이미 존재하는지 확인합니다.
     * @param id 확인할 사용자 아이디
     * @return 아이디가 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean isUserIdExists(String id) {
        return memberRepository.existsById(id);
    }

    /**
     * 이름과 이메일로 회원 아이디를 찾습니다.
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @return 찾은 사용자 아이디, 없으면 null
     */
    @Override
    public String findUserIdByNameAndEmail(String name, String email)
    {
        Optional<MemberEntity> memberOptional = memberRepository.findByNameAndEmail(name, email);
        return memberOptional.map(MemberEntity::getId).orElse(null);
    }

    /**
     * id와 email로 회원의 존재 여부를 확인합니다.
     * @return 회원이 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean checkMemberExists(String id, String email) {
        return memberRepository.existsByIdAndEmail(id, email);
    }

    /**
     * 인증번호가 포함된 이메일을 발송합니다.
     * @param email 수신자 이메일 주소
     * @param verificationCode 발송할 인증번호
     */
    @Override
    public void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[DateCock] 비밀번호 재설정 인증번호 안내");
        message.setText("안녕하세요. DateCock 비밀번호 재설정을 위한 인증번호는 [ " + verificationCode + " ] 입니다.\n"
                + "정확히 입력해주세요.");
        mailSender.send(message);
    }

    /**
     * 사용자 비밀번호를 업데이트합니다.
     * @param id 비밀번호를 업데이트할 사용자 아이디
     * @param newEncodedPassword 새로 암호화된 비밀번호
     */
    @Override
    @Transactional
    public void updatePassword(String id, String newEncodedPassword) {
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다: " + id));
        member.setPassword(newEncodedPassword);
        memberRepository.save(member);
    }

    @Override
    public String userName(String userid) {
        return memberRepository.userName(userid);
    }

    @Override
    public String userPhone(String userid) {
        return memberRepository.userPhone(userid);
    }

    @Override
    public void userinfodelete(String id) {
        memberRepository.deleteById(id);
    }

    @Override
    public MemberEntity usermodifyview(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public MemberEntity userpwmodifyview(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public MemberEntity getuserpw(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public void userpwmodif(MemberEntity memberEntity) {

    }

    @Override
    public void userinfomodify(MemberEntity memberentity) {
        memberRepository.save(memberentity);
    }
}
