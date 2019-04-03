package beginfunc.domain.models.serviceModels.participations;

import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.BaseServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import java.util.Date;

public abstract class BaseParticipationServiceModel extends BaseServiceModel {
    private Date submittedOn;
    private UserServiceModel participant;
    private AuctionServiceModel auction;

    protected BaseParticipationServiceModel() {
    }

    protected BaseParticipationServiceModel(Date submittedOn, UserServiceModel participant, AuctionServiceModel auction) {
        this.submittedOn = submittedOn;
        this.participant = participant;
        this.auction = auction;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public UserServiceModel getParticipant() {
        return participant;
    }

    public void setParticipant(UserServiceModel participant) {
        this.participant = participant;
    }

    public AuctionServiceModel getAuction() {
        return auction;
    }

    public void setAuction(AuctionServiceModel auction) {
        this.auction = auction;
    }
}
