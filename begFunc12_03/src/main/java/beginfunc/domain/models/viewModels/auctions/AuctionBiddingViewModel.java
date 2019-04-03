package beginfunc.domain.models.viewModels.auctions;

import java.math.BigDecimal;

public class AuctionBiddingViewModel {
    private String participantId;
    private String participantUsername;
    private String submittedOn;
    private BigDecimal biddingStep;
    private BigDecimal reachedPrice;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantUsername() {
        return participantUsername;
    }

    public void setParticipantUsername(String participantUsername) {
        this.participantUsername = participantUsername;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
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
