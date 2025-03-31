package kr.co.pei.pei_app.application.dto.notify;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotifyFindDTO {

    private String id;
    private String message;
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    private Long targetId;
    private String url;
    private Boolean isRead;

    public NotifyFindDTO(String id, String message, String type,
                         LocalDateTime createAt, Long targetId, String url, Boolean isRead) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.createAt = createAt;
        this.targetId = targetId;
        this.url = url;
        this.isRead = isRead;
    }
}
