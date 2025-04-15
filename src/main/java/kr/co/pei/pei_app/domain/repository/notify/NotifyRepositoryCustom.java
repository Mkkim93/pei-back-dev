package kr.co.pei.pei_app.domain.repository.notify;


import java.util.List;

public interface NotifyRepositoryCustom {
    void markAsDisplayed(List<String> ids);
    void markAsRead(List<String> ids);
}
