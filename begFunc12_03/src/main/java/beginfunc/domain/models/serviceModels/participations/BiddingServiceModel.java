package beginfunc.domain.models.serviceModels.participations;

import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;

import java.math.BigDecimal;
import java.util.Date;

public class BiddingServiceModel extends BaseParticipationServiceModel {
    private BigDecimal biddingStep;
    private BigDecimal reachedPrice;

    public BiddingServiceModel() {
    }

    public BiddingServiceModel(Date submittedOn, UserServiceModel participant, AuctionServiceModel auction,
                               BigDecimal biddingStep, BigDecimal reachedPrice) {
        super(submittedOn, participant, auction);
        this.biddingStep = biddingStep;
        this.reachedPrice = reachedPrice;
    }

    public BigDecimal getBiddingStep() {
        return biddingStep;
    }

    public void setBiddingStep(BigDecimal biddingStep) {
        this.biddingStep = biddingStep;
    }

    public BigDecimal getReachedPrice() {
        return reachedPrice;
    }

    public void setReachedPrice(BigDecimal reachedPrice) {
        this.reachedPrice = reachedPrice;
    }
}
