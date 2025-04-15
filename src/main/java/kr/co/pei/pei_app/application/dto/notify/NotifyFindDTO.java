package kr.co.pei.pei_app.application.dto.notify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private LocalDateTime createdAt;
    private Long targetId;
    private String url;
    private Boolean isRead;
    private Boolean isDisplayed;

    @JsonIgnore
    private Long receiverId;

    public NotifyFindDTO(String id, String message, String type,
                         LocalDateTime createdAt, Long targetId, String url, Boolean isRead, Boolean isDisplayed) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.targetId = targetId;
        this.url = url;
        this.isRead = isRead;
        this.isDisplayed = isDisplayed;
    }
}
