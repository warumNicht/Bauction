package beginfunc.domain.entities;

import beginfunc.domain.entities.auctionRelated.Auction;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "deals")
public class Deal extends BaseEntity{
    @Column(name = "deal_date_time")
    private Date dateTime;

    @Column(name = "deal_price")
    private BigDecimal dealPrice;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;
    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private User buyer;

    @OneToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "id")
    private Auction auction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_comment_id" ,referencedColumnName = "id")
    private Comment sellerComment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_comment_id" ,referencedColumnName = "id")
    private Comment buyerComment;


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Comment getSellerComment() {
        return sellerComment;
    }

    public void setSellerComment(Comment sellerComment) {
        this.sellerComment = sellerComment;
    }

    public Comment getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(Comment buyerComment) {
        this.buyerComment = buyerComment;
    }
}
