package kr.co.pei.pei_app.application.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 알림 Document DTO (일단 모든 도메인 공통으로 사용, 차후 도메인 많이지면 분리 하는 것이 좋을 듯)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyPostDTO {

    private Long id;
    private Long receiverId;
    private String message;
    private String type;
    private Long targetId;
    private String url;
}
