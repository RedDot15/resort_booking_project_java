package t3h.bigproject.event;

import org.apache.catalina.User;
import org.springframework.context.ApplicationEvent;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.entities.BillEntity;
import t3h.bigproject.entities.UserEntity;

public class OnRegistrationSuccessEvent extends ApplicationEvent {
    private String appUrl;
    private UserEntity userEntity;

    public OnRegistrationSuccessEvent(UserEntity userEntity, String appUrl) {
        super(userEntity);
        this.userEntity = userEntity;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
    public UserEntity getUserEntity() {
        return userEntity;
    }
    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
