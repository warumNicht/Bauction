package bauction.domain.entities.auctionRelated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "offers")
public class Offer extends BaseAuctionParticipation{
    @Column(name = "is_accepted")
    private boolean isAccepted;

    @Column(name = "is_valid")
    private boolean isValid;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "offered_price")
    private BigDecimal offeredPrice;


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
