package kr.co.pei.pei_app.domain.entity.board;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.users.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column
    private Long views = 0L;

    @CreatedBy
    @JoinColumn(name = "users_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Builder
    public Board(String title, String content, Users users) {
        this.title = title;
        this.content = content;
        this.users = users;
        this.views = 0L;
    }

    public Board(Long boardId) {
        this.id = boardId;
    }
}
