package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.BusinessmemberEntity;
import com.example.DateCock.Entity.MemberEntity;
import com.example.DateCock.Repository.BusinessmemberRepository;
import com.example.DateCock.Repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BusinessmemberRepository businessRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String loginType = request.getParameter("loginType"); // "member" 또는 "business"

        MemberEntity member = memberRepository.findById(username).orElse(null);
        if (member != null) {
            if ("business".equals(loginType)) {
                throw new UsernameNotFoundException("사업자 로그인 페이지에서 개인 계정을 사용할 수 없습니다.");
            }
            return new CustomUserDetails(member, null);
        }

        BusinessmemberEntity business = businessRepository.findById(username).orElse(null);
        if (business != null) {
            if ("member".equals(loginType)) {
                throw new UsernameNotFoundException("개인 로그인 페이지에서 사업자 계정을 사용할 수 없습니다.");
            }
            return new CustomUserDetails(null, business);
        }

        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }


}
