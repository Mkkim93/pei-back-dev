package kr.co.pei.pei_app.application.service.users;

import kr.co.pei.pei_app.application.dto.users.UsersFindDTO;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolesService {

    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public Page<UsersFindDTO> findAllUsers(Pageable pageable) {
        return usersRepository.findAllUsers(pageable);
    }
}
