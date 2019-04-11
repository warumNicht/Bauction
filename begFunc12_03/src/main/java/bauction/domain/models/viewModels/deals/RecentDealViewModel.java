package bauction.domain.models.viewModels.deals;


import bauction.domain.models.viewModels.BaseViewModel;

public class RecentDealViewModel extends BaseViewModel {

    private String dateTime;
    private String dealPrice;
    private DealParticipantViewModel seller;
    private DealParticipantViewModel buyer;
    private String auctionId;
    private String auctionName;
    private boolean hasSellerComment;
    private boolean hasBuyerComment;


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public DealParticipantViewModel getSeller() {
        return seller;
    }

    public void setSeller(DealParticipantViewModel seller) {
        this.seller = seller;
    }

    public DealParticipantViewModel getBuyer() {
        return buyer;
    }

    public void setBuyer(DealParticipantViewModel buyer) {
        this.buyer = buyer;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public boolean isHasSellerComment() {
        return hasSellerComment;
    }

    public void setHasSellerComment(boolean hasSellerComment) {
        this.hasSellerComment = hasSellerComment;
    }

    public boolean isHasBuyerComment() {
        return hasBuyerComment;
    }

    public void setHasBuyerComment(boolean hasBuyerComment) {
        this.hasBuyerComment = hasBuyerComment;
    }
}
