package kr.co.pei.pei_app.domain.repository.notify;

import kr.co.pei.pei_app.domain.entity.notify.Notify;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotifyRepository extends MongoRepository<Notify, Long> {

    List<Notify> findByReceiverIdAndIsReadFalse(Long receiverId);
}
