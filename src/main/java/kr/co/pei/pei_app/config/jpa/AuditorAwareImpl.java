package kr.co.pei.pei_app.config.jpa;

import kr.co.pei.pei_app.application.service.users.UsersService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.jwt.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Users> {

    private final UsersService usersService;

    public AuditorAwareImpl(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public Optional<Users> getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            String username = ((UserDetailsImpl) principal).getUsername();
            return usersService.findByUsername(username);
        }
        return Optional.empty();
    }
}
