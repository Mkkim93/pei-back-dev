package kr.co.pei.pei_app.admin.application.dto.surveys.type;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminTypeUsageDTO {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long usageCount;

    @QueryProjection
    public AdminTypeUsageDTO(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt,
                             LocalDateTime deletedAt, Long usageCount) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.usageCount = usageCount;
    }
}


