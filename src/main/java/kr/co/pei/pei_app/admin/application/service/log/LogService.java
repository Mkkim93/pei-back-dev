package kr.co.pei.pei_app.admin.application.service.log;

import kr.co.pei.pei_app.admin.application.dto.log.AdminLogResponseDTO;
import kr.co.pei.pei_app.admin.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.log.Log;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.log.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UsersContextService usersContextService;

    public Page<AdminLogResponseDTO> findAll(Pageable pageable) {

        Users users = usersContextService.getCurrentUser();

        Page<Log> usersLog = logRepository.findByUsersId(users.getId(), pageable);

        return usersLog.map(log -> new AdminLogResponseDTO(
                log.getId(),
                log.getAction(),
                log.getDescription(),
                log.getCreatedAt(),
                log.getUsers().getName()
        ));
    }

    public boolean delete(Long logId) {
        int deleted = logRepository.deleteLogId(logId);

        if (deleted > 0) {
            return true;
        }
        return false;
    }
}
