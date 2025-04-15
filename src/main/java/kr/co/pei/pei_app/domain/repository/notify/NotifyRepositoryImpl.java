package kr.co.pei.pei_app.domain.repository.notify;

import kr.co.pei.pei_app.domain.entity.notify.Notify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import static org.springframework.data.mongodb.core.query.Criteria.where;


import java.util.List;

@Repository
public class NotifyRepositoryImpl implements NotifyRepositoryCustom{

    private final MongoTemplate template;

    public NotifyRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public void markAsDisplayed(List<String> ids) {
        template.update(Notify.class)
                .matching(where("_id").in(ids))
                .apply(new Update().set("isDisplayed", true)).all();
    }

    @Override
    public void markAsRead(List<String> ids) {
        template.update(Notify.class)
                .matching(where("_id").in(ids))
                .apply(new Update().set("isRead", true)).all();
    }
}
