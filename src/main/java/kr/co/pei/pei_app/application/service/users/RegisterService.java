package kr.co.pei.pei_app.application.service.users;

import kr.co.pei.pei_app.application.dto.users.UsersResponseDTO;
import kr.co.pei.pei_app.application.dto.users.UsersRegisterDTO;
import kr.co.pei.pei_app.application.exception.users.DuplicateException;
import kr.co.pei.pei_app.application.service.auth.AuthService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import kr.co.pei.pei_app.domain.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder encoder;

    public UsersResponseDTO register(UsersRegisterDTO usersRegisterDTO) {

        boolean existUsername = usersRepository.existsByUsername(usersRegisterDTO.getUsername());

        if (existUsername) {
            throw new DuplicateException(usersRegisterDTO.getUsername());
        }

        Users users = Users.builder()
                .username(usersRegisterDTO.getUsername())
                .password(encoder.encode(usersRegisterDTO.getPassword()))
                .name(usersRegisterDTO.getName())
                .phone(usersRegisterDTO.getPhone())
                .mail(usersRegisterDTO.getMail())
                .build();

        Users save = usersRepository.save(users);

        return new UsersResponseDTO(save.getUsername(), save.getName());
    }

    public Long requestCode(String phone) {
        return authService.sendOTP(phone);
    }

    /**
     * @param phone
     * @param code
     */
    public void validCode(String phone, String code) {

        Map<String, Object> userPhoneCode = authService.getSecretPhoneCode(phone);

        if (userPhoneCode == null || userPhoneCode.get("code") == null) {
            throw new IllegalArgumentException("인증번호가 존재하지 않습니다.");
        }

        String storedCode = userPhoneCode.get("code").toString();

        if (!storedCode.equals(code)) {
            throw new IllegalArgumentException("잘못된 인증번호입니다.");
        }

        Long expired = (Long) userPhoneCode.get("expired");

        if (expired == -2L) {
            throw new IllegalArgumentException("인증번호 만료 시간이 지났습니다.");
        }
    }

    public boolean existByUsername(String username) {
        return usersRepository.existsByUsername(username);
    }
}
