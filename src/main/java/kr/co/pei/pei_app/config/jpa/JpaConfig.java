package kr.co.pei.pei_app.config.jpa;

import kr.co.pei.pei_app.admin.application.service.users.UsersService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    private final UsersService usersService;

    public JpaConfig(UsersService usersService) {
        this.usersService = usersService;
    }

    @Bean
    public AuditorAware<Users> auditorAware(UsersService usersService) {
        return new AuditorAwareImpl(usersService);
    }
}
