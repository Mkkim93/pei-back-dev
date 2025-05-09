package kr.co.pei.pei_app.domain.entity.schedule;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@Table(name = "schedule")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Schedule {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Column
    private String status; // 일정 상태 기록

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    public Schedule(String title, String description, LocalDateTime startTime, LocalDateTime endTime,
                    String status, Users users) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isDeleted = this.isDeleted();
        this.users = users;
    }
}

