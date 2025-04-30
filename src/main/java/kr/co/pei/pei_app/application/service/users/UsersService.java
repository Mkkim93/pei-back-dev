package kr.co.pei.pei_app.application.service.users;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.aop.AuditLogContext;
import kr.co.pei.pei_app.application.dto.users.UsersFindDTO;
import kr.co.pei.pei_app.application.dto.users.PasswordRequest;
import kr.co.pei.pei_app.application.dto.users.UsersDetailDTO;
import kr.co.pei.pei_app.application.exception.users.UserMailNotFoundException;
import kr.co.pei.pei_app.application.exception.users.UsersExistException;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.application.service.auth.UsersContextService;
import kr.co.pei.pei_app.domain.entity.log.AuditLog;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// UsersContext 순환 참조 걸림 쓰지 말것
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {

    private final BCryptPasswordEncoder encoder;
    private final UsersRepository usersRepository;
    private final AuthService authService;

    public UsersDetailDTO detail() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Users users = usersRepository.findByUsername(
                username).orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        return new UsersDetailDTO(
                users.getUsername(), users.getName(), users.getPhone(),
                users.getMail(), users.getRoleType().getText(), users.getDescription(),
                users.getUserImg(), users.getHospital().getName());
    }

    // 사용자 계정 찾기
    public void recoverUsername(String phone) {
        authService.sendOTP(phone);
    }

    // 사용자가 응답받은 인증 번호 입력 후 발급받은 인증번호와 일치하면 사용자의 계정 응답
    public String requestUsername(String phone, String code) {
        authService.getUsernameByCode(phone, code);

        return usersRepository.findUsernameByPhone(phone);
    }

    // 사용자의 계정에 등록된 개인 메일 주소로 비밀번호 변경 링크를 전송
    public String recoverPassword(String mail) {
        String username = usersRepository.findUsernameByMail(mail);
        if (username == null) {
            log.info("잘못된 이메일 주소 또는 사용자가 존재하지 않음");
            throw new UserMailNotFoundException("사용자 정보가 존재하지 않습니다.");
        }

        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재 하지 않습니다."));

        UUID authUUID = UUID.randomUUID();
        HashMap<String, Object> authMap = new HashMap<>();
        authMap.put("mail", users.getMail());
        authMap.put("uuid", authUUID);

        Map<String, Object> savedMap = authService.saveUserMail(authMap);
        authService.sendPassword(savedMap);

        return savedMap.get("uuid").toString();
    }

    public boolean myPasswordValid(PasswordRequest request) {
        Users users = usersRepository.findByUsername(
                request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        boolean matches = encoder.matches(request.getPassword(), users.getPassword());

        if (!matches) {
            return false;
        }
        return true;
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public boolean deleteUsername(String username) {

        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾는 도중 오류가 발생 하였습니다."));

        usersRepository.delete(users);

        Optional<Users> deleted = usersRepository.findByUsername(username);

        if (deleted.isPresent()) {
            return false;
        }
        return true;
    }

    @AuditLog(action = "비밀번호 변경", description = "비밀번호를 변경 하였습니다.")
    public int resetPassword(String token, String password) {

        int count = 0;

        try {
            Map<String, Object> usersUUIDToken = authService.getUsersUUIDToken(token);

            String userMail = usersUUIDToken.get("mail").toString();
            // TODO 없을 경우 예외
            String username = usersRepository.findUsernameByMail(userMail);
            AuditLogContext.setUserMail(userMail);

            count = usersRepository.updateTempPassword(encodedPassword(password), username);

        } catch (IllegalArgumentException e) {
            log.warn("사용자 비밀번호 변경 실패 : {}", e.getMessage());
            throw new IllegalArgumentException("사용자 비밀번호 변경에 실패 했습니다.");
        } finally {
            AuditLogContext.clear();
        }
        return count;
    }

    public String encodedPassword(String password) {
        return encoder.encode(password);
    }
}
