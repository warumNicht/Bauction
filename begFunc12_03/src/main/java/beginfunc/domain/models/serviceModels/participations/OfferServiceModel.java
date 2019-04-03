package beginfunc.domain.models.serviceModels.participations;

import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;

import java.math.BigDecimal;
import java.util.Date;

public class OfferServiceModel extends BaseParticipationServiceModel {
    private boolean isAccepted;
    private boolean isValid;
    private Date expirationTime;
    private BigDecimal offeredPrice;

    public OfferServiceModel() {
    }

    public OfferServiceModel(Date submittedOn, UserServiceModel participant,
                             AuctionServiceModel auction, BigDecimal offeredPrice) {
        super(submittedOn, participant, auction);
        this.offeredPrice = offeredPrice;
        this.isValid=true;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public BigDecimal getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(BigDecimal offeredPrice) {
        this.offeredPrice = offeredPrice;
    }
}
