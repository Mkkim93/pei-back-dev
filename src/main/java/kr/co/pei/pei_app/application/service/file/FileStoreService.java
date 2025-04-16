package kr.co.pei.pei_app.application.service.file;

import kr.co.pei.pei_app.application.dto.board.BoardCreateDTO;
import kr.co.pei.pei_app.application.dto.file.FileBoardDTO;
import kr.co.pei.pei_app.application.service.s3.S3ServiceImpl;
import kr.co.pei.pei_app.domain.entity.board.Board;
import kr.co.pei.pei_app.domain.entity.file.FileStore;
import kr.co.pei.pei_app.domain.entity.file.RenderType;
import kr.co.pei.pei_app.domain.repository.file.FileStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileStoreService {


    private final S3ServiceImpl s3Service;
    private final FileStoreRepository fileStoreRepository;

    public void saveFiles(Board board, BoardCreateDTO boardCreateDTO) {

        List<FileBoardDTO> boardFiles = boardCreateDTO.getBoardFiles();

        for (FileBoardDTO boardFile : boardFiles) {

            FileStore fileStore = FileStore.builder()
                    .name(boardFile.getName())
                    .path(boardFile.getPath())
                    .size(boardFile.getSize())
                    .orgName(boardFile.getOrgName())
                    .type(boardFile.getType())
                    .renderType(RenderType.valueOf(boardFile.getRenderType()))
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

    // 파일이 존재하면 삭제 처리
    public void deleteFiles(List<FileStore> deleteEntityList) {
        log.info("파일이 삭제 됩니다.");
        fileStoreRepository.deleteAll(deleteEntityList);
        s3Service.s3Delete(deleteEntityList);
    }
}
