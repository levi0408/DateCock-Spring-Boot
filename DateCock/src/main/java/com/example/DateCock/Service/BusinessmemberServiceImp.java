package com.example.DateCock.Service;

import com.example.DateCock.DTO.BusinessmemberDTO;
import com.example.DateCock.Entity.BusinessmemberEntity;
import com.example.DateCock.Repository.BusinessmemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class BusinessmemberServiceImp implements BusinessmemberService{
    @Autowired
    BusinessmemberRepository businessmemberRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void businessmembersave(BusinessmemberEntity businessmemberEntity) {
        businessmemberRepository.save(businessmemberEntity);
    }

    @Override
    public BusinessmemberEntity modifyview(String businessnumber) {
        return businessmemberRepository.findById(businessnumber).orElse(null);
    }

    @Override
    public void modifygo(BusinessmemberEntity businessmemberEntity) {
        businessmemberRepository.save(businessmemberEntity);
    }

    @Override
    public BusinessmemberEntity getbusinesspw(String businessnumber) {
        return businessmemberRepository.findById(businessnumber).orElse(null);
    }

    @Override
    public void businessinfodelete(String businessnumber) {
        businessmemberRepository.deleteById(businessnumber);
    }

    @Override
    public BusinessmemberDTO findbusinessnumber(String businessname, String email) {
        BusinessmemberEntity entity = businessmemberRepository.findMemberId(businessname, email);
        return new BusinessmemberDTO(entity);
    }
    //사업자 비밀번호 찾기 1단계 - 사업자 등록번호 조회

    @Override
    public boolean MatchBusinessNumber(String businessnumber) {
//        return businessmemberRepository.MatchBusinessNumber(businessnumber);
        System.out.println("DEBUG: 요청받은 사업자번호 = " + businessnumber);

        boolean exists = businessmemberRepository.MatchBusinessNumber(businessnumber);

        System.out.println("DEBUG: DB 조회 결과 = " + exists);

        return exists;
    }

    //사업자 비밀번호 찾기 2단계 - 사업자 등록번호, 이메일 확인하고 인증번호 발송

    @Override
    public boolean MatchBusinessnumberEmail(String businessnumber, String email) {
        return businessmemberRepository.MatchBusinessnumberEmail(businessnumber, email);
    }

    @Override
    public String SendCode(String email) {
        String code = createVerificationCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 찾기 인증번호");
        message.setText("인증번호: " + code);

        mailSender.send(message);

        return code;
    }

    private String createVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    //사업자 비밀번호 찾기 3단계 - 인증번호 검증 후 임시 비밀번호 발급

    @Override
    public String NewPassword(String businessnumber) {
        String tempPassword = createTempPassword();

        Optional<BusinessmemberEntity> optionalMember = Optional.ofNullable(businessmemberRepository.findByBusinessnumber(businessnumber));
        if (optionalMember.isPresent()) {
            BusinessmemberEntity member = optionalMember.get();
            String encodedPassword = passwordEncoder.encode(tempPassword);
            member.setPassword(encodedPassword);
            businessmemberRepository.save(member);
        }

        return tempPassword;
    }

    private String createTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }


}
