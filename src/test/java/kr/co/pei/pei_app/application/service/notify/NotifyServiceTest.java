package kr.co.pei.pei_app.application.service.notify;

import kr.co.pei.pei_app.application.dto.notify.NotifyFindDTO;
import kr.co.pei.pei_app.application.service.users.UsersService;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class NotifyServiceTest {

    @Autowired
    private NotifyService service;

    @Autowired
    private UsersService usersService;

    @BeforeEach
    void loginUser() {
        Users mockUser = Users.builder()
                .id(1L)
                .username("user1")
                .name("관리자1")
                .password("1234")
                .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("전체 알림 조회 테스트")
    void findAll() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersService.findByUsername(username).get();
        boolean isRead = false;

        // when
        Page<NotifyFindDTO> all = service.findAll(pageRequest, isRead);
        List<NotifyFindDTO> content = all.getContent();
        NotifyFindDTO first = content.getFirst();

        for (NotifyFindDTO dto : all) {
            System.out.println("dto.getIsDisplayed() = " + dto.getIsDisplayed());
            System.out.println("dto.getMessage() = " + dto.getMessage());
            System.out.println("dto.getType() = " + dto.getType());
        }

        // then
        assertThat(users.getId()).isEqualTo(first.getReceiverId());
    }

}