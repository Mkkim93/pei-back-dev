package kr.co.pei.pei_app.domain.entity.notify;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collation = "notify")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notify {

    @Id
    private String id;
    private Long receiverId;
    private String message;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String type; // 도메인 구분
    private Long targetId; // 도메인 PK
    private Boolean isRead = false;
    private String url; // 클릭시 이동 경로
    private LocalDateTime expired;
}
