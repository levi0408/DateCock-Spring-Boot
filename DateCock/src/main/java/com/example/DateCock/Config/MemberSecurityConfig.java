// MemberSecurityConfig.java
package com.example.DateCock.Config;

import com.example.DateCock.Service.CustomUserDetails;
import com.example.DateCock.Service.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MemberSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public MemberSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public static BCryptPasswordEncoder memberPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(memberPasswordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf() //
                .and()  //
                .authorizeHttpRequests()
                .requestMatchers(
                        "/", "/main", "/signup", "/DateCocklog", "/membersave","/datecourseout",
                        "/checkUserId", "/css/**", "/js/**", "/image/**",
                        "/businesssignup", "/businessmembersave", "/businessnumbercheck2","/businesssearch",
                         "/game", "/listup", "/support",
                        "/businessloginprocess",
                        // 아이디/비밀번호 찾기 관련 경로들
                        "/membersearch",       // 아이디/비밀번호 찾기 페이지 자체
                        "/searchIdAjax",       // 아이디 찾기 AJAX 요청
                        "/searchpwd",          // 비밀번호 재설정 페이지로 이동 (POST 요청)
                        "/checkMemberExists",  // 비밀번호 찾기 시 회원 존재 여부 확인 AJAX
                        "/sendVerificationCode",// 이메일 인증번호 발송 AJAX
                        "/checkVerificationCode",// 이메일 인증번호 확인 AJAX
                        "/updatePassword"      // <--- 이 경로를 추가합니다.
                ).permitAll() // 이 경로들은 모두에게 허용
                .requestMatchers("/mypage", "/datecourseout", "/listup", "/support").authenticated()
                .requestMatchers("/businessinput", "/businesssave", "/businessmypage").hasRole("BUSINESS") // 이 역할 기반 권한은 유지
                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                .and()
                .formLogin()
                .loginPage("/DateCocklog")
                .loginProcessingUrl("/loginprocess")
                .usernameParameter("loginId")
                .passwordParameter("loginPw")
                .successHandler((request, response, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof CustomUserDetails) {
                        CustomUserDetails customUser = (CustomUserDetails) principal;

                        if (customUser.isBusiness()) {
                            request.getSession().setAttribute("buisnessloginstate", true);
                            request.getSession().setAttribute("businessname", customUser.getBusinessmember().getBusinessname());
                            request.getSession().setAttribute("businessnumberA", customUser.getUsername());
                            response.sendRedirect("/main"); // 기업 회원은 마이페이지로
                        } else {
                            request.getSession().setAttribute("personalloginstate", true);
                            request.getSession().setAttribute("name", customUser.getDisplayName());
                            request.getSession().setAttribute("id", customUser.getUsername());
                            response.sendRedirect("/main"); // 개인 회원은 메인으로
                        }
                    } else {
                        response.sendRedirect("/main"); // 혹시 모를 예외 상황 대비
                    }
                })

                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("/signup?error=true");
                    }
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll();

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}