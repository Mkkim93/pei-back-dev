package kr.co.pei.pei_app.domain.entity.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     *  used: 현재 UI 노출 여부 논리적 판단 후 삭제 or 유지 결정 필드
     *  detail: 파일이 저장 된 시점에 true, 만약 파일 작성 중 파일 저장에 실패하거나 작성을 취소 하면 false 값을 유지한다.
     *  (>> 파일을 업로드 하는 시점에 s3 에 이미지 Url 을 저장하기 때문)
     *  향후 스프링 배치 또는 스케쥴러를 통해 해당 데이터를 s3 에서 삭제하는 방안 고려 (메타데이터를 삭제 할 지 여부는 협의)
     */
    @Column
    private boolean used;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = LAZY)
    private Board board;

    public void updateUsed(boolean used) {
        this.used = used;
    }

    @Builder
    public FileStore(Long id, String name, String orgName, String path, Long size, String type,
                     RenderType renderType, boolean used, Board board) {
        this.id = id;
        this.name = name;
        this.orgName = orgName;
        this.path = path;
        this.size = size;
        this.type = type;
        this.renderType = renderType;
        this.used = used;
        this.board = board;
    }
}
