package bauction.domain.entities.auctionRelated;

import bauction.domain.entities.BaseEntity;
import bauction.domain.entities.User;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auction_participations")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseAuctionParticipation extends BaseEntity {
    @Column(name = "submitted_on")
    private Date submittedOn;

    @ManyToOne
    @JoinColumn(name = "participant_id",referencedColumnName = "id")
    private User participant;

    @ManyToOne
    @JoinColumn(name = "auction_id",referencedColumnName = "id")
    private Auction auction;


    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}