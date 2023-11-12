package t3h.bigproject.event;

import org.springframework.context.ApplicationEvent;
import t3h.bigproject.dto.BillDto;
import t3h.bigproject.entities.BillEntity;

public class OnRegistrationSuccessEvent extends ApplicationEvent {
    private String appUrl;
    private BillEntity billEntity;

    public OnRegistrationSuccessEvent(BillEntity billEntity, String appUrl) {
        super(billEntity);
        this.billEntity = billEntity;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
    public BillEntity getBillEntity() {
        return billEntity;
    }
    public void setBillEntity(BillEntity billEntity) {
        this.billEntity = billEntity;
    }
}
