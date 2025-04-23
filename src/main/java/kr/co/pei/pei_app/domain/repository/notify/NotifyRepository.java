package kr.co.pei.pei_app.domain.repository.notify;

import kr.co.pei.pei_app.domain.entity.notify.Notify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends MongoRepository<Notify, String>, NotifyRepositoryCustom {
    Page<Notify> findByReceiverIdAndIsDisplayedFalse(Long receiverId, Pageable pageable);
    Page<Notify> findByReceiverId(Long receiverId, Pageable pageable);
    Page<Notify> findByReceiverIdAndIsReadFalse(Long receiverId, Pageable pageable);
}
