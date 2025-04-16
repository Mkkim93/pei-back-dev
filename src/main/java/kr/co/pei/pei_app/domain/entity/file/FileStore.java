package kr.co.pei.pei_app.domain.entity.file;

import jakarta.persistence.*;
import kr.co.pei.pei_app.domain.entity.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Table(name = "file_store")
@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FileStore {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String orgName;

    @Column
    private String path;

    @Column
    private Long size;

    @Column
    private String type;

    @Column(name = "render_type")
    @Enumerated(value = STRING)
    private RenderType renderType;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = LAZY)
    private Board board;

    @Builder
    public FileStore(String name, String orgName, String path, Long size, String type, RenderType renderType, Board board) {
        this.name = name;
        this.orgName = orgName;
        this.path = path;
        this.size = size;
        this.type = type;
        this.renderType = renderType;
        this.board = board;
    }
}
