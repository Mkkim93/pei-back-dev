package kr.co.pei.pei_app.admin.application.service.file;

import jakarta.persistence.EntityNotFoundException;
import kr.co.pei.pei_app.admin.application.dto.board.AdminBoardFileSupport;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardDTO;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileBoardUpdateDTO;
import kr.co.pei.pei_app.admin.application.dto.file.AdminFileDownLoadDTO;
import kr.co.pei.pei_app.admin.application.service.s3.S3ServiceImpl;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import kr.co.pei.pei_app.domain.entity.file.RenderType;
import kr.co.pei.pei_app.domain.repository.file.FileQueryRepository;
import kr.co.pei.pei_app.domain.repository.file.FileStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileStoreService {

    private final S3ServiceImpl s3Service;
    private final FileStoreRepository fileStoreRepository;
    private final FileQueryRepository fileQueryRepository;

    public AdminFileDownLoadDTO findById(Long id) {

        FileStore fileStore = fileStoreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("파일이 존재 하지 않습니다."));

        AdminFileDownLoadDTO downloadDTO = new AdminFileDownLoadDTO(
                fileStore.getId(),
                fileStore.getName(),
                fileStore.getOrgName(),
                fileStore.getPath());

        return downloadDTO;
    }

    public void saveFiles(Board board, AdminBoardFileSupport adminBoardFileSupport) {

        List<AdminFileBoardDTO> boardFiles = adminBoardFileSupport.getBoardFiles();

        for (AdminFileBoardDTO boardFile : boardFiles) {

            FileStore fileStore = FileStore.builder()
                    .name(boardFile.getName())
                    .path(boardFile.getPath())
                    .size(boardFile.getSize())
                    .orgName(boardFile.getOrgName())
                    .type(boardFile.getType())
                    .renderType(RenderType.valueOf(boardFile.getRenderType()))
                    .used(boardFile.isUsed())
                    .board(board)
                    .build();

            fileStoreRepository.save(fileStore);
        }
    }

    // 게시글 번호에 등록된 파일 존재 여부
    public void deleteFilesIfBoardHasFiles(List<Long> boardIds) {
        List<FileStore> fileList = fileStoreRepository.findByBoardIdIn(boardIds);
        if (!fileList.isEmpty()) {
            deleteFiles(fileList);
        }
    }

    /**
     * 게시글을 삭제할 때 포함된 파일이 존재하면 삭제 처리
     * @param deleteEntityList : 삭제할 파일 pk
     *  TODO 버전 관리 필요성 협의
     */
    public void deleteFiles(List<FileStore> deleteEntityList) {
        log.info("파일이 삭제 됩니다.");
        fileStoreRepository.deleteAll(deleteEntityList);
        s3Service.s3Delete(deleteEntityList);
    }

    // 게시글 수정 폼에서 파일 삭제 API
    public void updateFiles(Board board, List<AdminFileBoardUpdateDTO> updateFiles) {

        log.info("모든 파일 : {}", updateFiles);

        List<AdminFileBoardUpdateDTO> orgFiles = updateFiles.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toList());

        List<AdminFileBoardUpdateDTO> newFiles = updateFiles.stream()
                .filter(dto -> dto.getId() == null)
                .collect(Collectors.toList());

        log.info("기존 파일 {} :", orgFiles);
        log.info("신규 파일 {} : ", newFiles);

        // 기존 파일 상태 수정 (fileIdsToDeactivate: 비활성화할 파일 ID)
        List<Long> fileIdsToDeactivate = new ArrayList<>();
        for (AdminFileBoardUpdateDTO dto : orgFiles) {

            fileStoreRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

            if (!dto.isUsed()) {
                fileIdsToDeactivate.add(dto.getId());
            }
        }

        if (!fileIdsToDeactivate.isEmpty()) {
            fileQueryRepository.orgFileUpdate(fileIdsToDeactivate);
        }

        // 새 파일 저장
        for (AdminFileBoardUpdateDTO dto : newFiles) {
            if (dto.getBoardId() == null) {
                log.info("저장되지 못하는 파일 (게시글 아이디 없음): {} ", dto.getName());
                return;
            }
            FileStore file = FileStore.builder()
                    .name(dto.getName())
                    .path(dto.getPath())
                    .orgName(dto.getOrgName())
                    .type(dto.getType())
                    .size(dto.getSize())
                    .renderType(RenderType.valueOf(dto.getRenderType()))
                    .used(dto.isUsed())
                    .board(board)
                    .build();

            fileStoreRepository.save(file);
        }
    }
}
