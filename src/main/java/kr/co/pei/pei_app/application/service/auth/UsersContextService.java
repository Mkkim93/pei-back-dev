package kr.co.pei.pei_app.application.service.auth;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.application.service.users.UsersService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import kr.co.pei.pei_app.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 현재 로그인 돤 사용자 정보(Entity) 호출
 * Jwt 에 별도 구현한 loadUserByName 이 있지만 해당 객체는 인증과 관련된 로직으로 현재 메서드로 분리하여 비즈니스 로직에서는 이 메서드로 사용자 정보를 가지고 온다
 * 사용자 정보는 Header 에 저장된 Access Token 을 기준으로 가지고 온다.
 * // TODO 이거 막쓰면 안됨 엑세스 토큰이 확실하게 보장된 상태에서만 사용
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersContextService {

    private final UsersService usersService;

    public Users getCurrentUser() {
        Users users = null;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                users = usersService.findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 없습니다."));
            }
        } catch (AuthenticationException e) {
            throw new AuthenticationException("인증 정보가 만료 되었습니다.") {};
        }
        log.info("users: {}", users);
        return users;
    }
}
