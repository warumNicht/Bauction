package bauction.domain.models.serviceModels.deals;

import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.BaseServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;

import java.math.BigDecimal;
import java.util.Date;

public class DealServiceModel extends BaseServiceModel {
    private Date dateTime;
    private BigDecimal dealPrice;
    private UserServiceModel seller;
    private UserServiceModel buyer;
    private AuctionServiceModel auction;
    private CommentServiceModel sellerComment;
    private CommentServiceModel buyerComment;

    public DealServiceModel() {
    }

    public DealServiceModel(Date dateTime,BigDecimal dealPrice, UserServiceModel seller,
                            UserServiceModel buyer, AuctionServiceModel auction) {
        this.dateTime = dateTime;
        this.dealPrice=dealPrice;
        this.seller = seller;
        this.buyer = buyer;
        this.auction = auction;
    }

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

    public UserServiceModel getSeller() {
        return seller;
    }

    public void setSeller(UserServiceModel seller) {
        this.seller = seller;
    }

    public UserServiceModel getBuyer() {
        return buyer;
    }

    public void setBuyer(UserServiceModel buyer) {
        this.buyer = buyer;
    }

    public AuctionServiceModel getAuction() {
        return auction;
    }

    public void setAuction(AuctionServiceModel auction) {
        this.auction = auction;
    }

    public CommentServiceModel getSellerComment() {
        return sellerComment;
    }

    public void setSellerComment(CommentServiceModel sellerComment) {
        this.sellerComment = sellerComment;
    }

    public CommentServiceModel getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(CommentServiceModel buyerComment) {
        this.buyerComment = buyerComment;
    }
}
