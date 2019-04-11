package bauction.domain.entities.auctionRelated;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "biddings")
public class Bidding extends BaseAuctionParticipation{
    @Column(name = "bidding_step")
    private BigDecimal biddingStep;

    @Column(name = "reached_price")
    private BigDecimal reachedPrice;


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