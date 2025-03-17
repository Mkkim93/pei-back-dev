package kr.co.pei.pei_app.application.service.users;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.application.dto.users.PasswordRequest;
import kr.co.pei.pei_app.application.dto.users.UsersDetailDTO;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import kr.co.pei.pei_app.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {

    private final BCryptPasswordEncoder encoder;
    private final UsersRepository usersRepository;
    private final AuthService authService;

    public UsersDetailDTO detail(UserDetailsImpl userDetails) {

//        String findUsername = authService.findUsernameByToken(username);
        Users users = usersRepository.findByUsername(
                userDetails.getUsername()).
                orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재하지 않습니다."));

        return new UsersDetailDTO(users.getUsername(), users.getName(), users.getPhone(), users.getMail());
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

    public void recoverPassword(String username) {

        boolean existsByUsername = usersRepository.existsByUsername(username);

        if (!existsByUsername) {
            throw new IllegalArgumentException("해당 계정의 사용자 정보가 존재 하지 않습니다.");
        }

        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("사용자 정보가 존재 하지 않습니다."));
        String temp = generateTempPassword();
        String encodePassword = encoder.encode(temp);

        // TODO 변경 링크 전송
        authService.sendPassword(encodePassword, users.getMail());
    }

    public static String generateTempPassword() {
        return AuthService.generateAuthCode();
    }

//    public Map<String, Object> updatePassword(String username, String password) {
//        Map<String, Object> responseMap = authService.checkPassword(password);
//        usersRepository.updateTempPassword(encoder.encode(password),username);
//        return responseMap;
//    }

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
}
