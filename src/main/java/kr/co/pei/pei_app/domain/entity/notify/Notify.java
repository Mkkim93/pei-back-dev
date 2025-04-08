package kr.co.pei.pei_app.domain.entity.notify;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 특정 사용자가 이벤트를 발생시키면 해당 이벤트에 대한 내용이 모든 사용자에게 알림으로 전송된다 (SSE Emit)
 */
@Data
@Document(collection = "notify")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notify {

    @Id
    private String id;
    private Long receiverId;
    private String message;
    private LocalDateTime createdAt;
    private String type; // 도메인 구분
    private Long targetId; // 도메인 PK
    private Boolean isRead = false;
    private String url; // 클릭시 이동 경로
    private LocalDateTime expired;
    private Boolean isDisplayed; // 알림 랜더링 여부 (최초 1회만 랜더링 되고 읽지 않은 알림은 '읽지 않음 알림' 목록에서 확인 가능하도록 구현)
}
