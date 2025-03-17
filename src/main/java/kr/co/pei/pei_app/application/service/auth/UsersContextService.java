package kr.co.pei.pei_app.application.service.auth;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.application.service.users.UsersService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 현재 로그인 돤 사용자 정보(Entity) 호출
 * Jwt 에 별도 구현한 loadUserByName 이 있지만 해당 객체는 인증과 관련된 로직으로 현재 메서드로 분리하여 비즈니스 로직에서는 이 메서드로 사용자 정보를 가지고 온다
 * 사용자 정보는 Header 에 저장된 Access Token 을 기준으로 가지고 온다.
 */
@Service
@RequiredArgsConstructor
public class UsersContextService {

    private final UsersService usersService;

    public Users getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return usersService.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("유저 정보가 없습니다."));
        }
        throw new IllegalStateException("현재 로그인된 사용자를 찾을 수 없습니다.");
    }
}
