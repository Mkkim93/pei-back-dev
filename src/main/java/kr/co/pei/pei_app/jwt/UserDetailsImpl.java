package kr.co.pei.pei_app.jwt;

import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public record UserDetailsImpl(Users users) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                log.info("사용자 권한 정보: {} ", users.getRoleType().getDescription());
                return users.getRoleType().name();
            }
        });
        return collection;
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
