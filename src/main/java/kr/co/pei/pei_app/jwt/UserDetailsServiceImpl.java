package kr.co.pei.pei_app.jwt;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        if (users == null || !users.getUsername().equals(username)) {
            throw new UsernameNotFoundException("잘못된 사용자 정보 입니다.");
        }

        return new UserDetailsImpl(users);
    }
}