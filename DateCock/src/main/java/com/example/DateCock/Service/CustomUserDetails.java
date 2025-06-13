package com.example.DateCock.Service;

import com.example.DateCock.Entity.BusinessEntity;
import com.example.DateCock.Entity.BusinessmemberEntity;
import com.example.DateCock.Entity.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final MemberEntity member;
    private final BusinessmemberEntity businessmember;

    // ✅ 하나로 통합된 생성자만 유지
    public CustomUserDetails(MemberEntity member, BusinessmemberEntity businessmember) {
        this.member = member;
        this.businessmember = businessmember;
    }

    public boolean isPersonal() {
        return member != null;
    }

    public boolean isBusiness() {
        return businessmember != null;
    }

    public String getDisplayName() {
        return isPersonal() ? member.getName() : businessmember.getBusinessname();
    }

    @Override
    public String getUsername() {
        return isPersonal() ? member.getId() : businessmember.getBusinessnumber();
    }

    @Override
    public String getPassword() {
        return isPersonal() ? member.getPassword() : businessmember.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> isBusiness() ? "ROLE_BUSINESS" : "ROLE_USER");
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
