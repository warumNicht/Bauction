package beginfunc.domain.models.viewModels.offers;

import beginfunc.domain.models.viewModels.BaseViewModel;
import java.math.BigDecimal;

public class OfferViewModel extends BaseViewModel {
    private OfferAuctionViewModel auction;
    private OfferParticipantViewModel participant;
    private BigDecimal offeredPrice;
    private boolean isAccepted;
    private boolean isValid;
    private String expirationTime;



    public OfferAuctionViewModel getAuction() {
        return auction;
    }

    public void setAuction(OfferAuctionViewModel auction) {
        this.auction = auction;
    }

    public OfferParticipantViewModel getParticipant() {
        return participant;
    }

    public void setParticipant(OfferParticipantViewModel participant) {
        this.participant = participant;
    }

    public BigDecimal getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(BigDecimal offeredPrice) {
        this.offeredPrice = offeredPrice;
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

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}
