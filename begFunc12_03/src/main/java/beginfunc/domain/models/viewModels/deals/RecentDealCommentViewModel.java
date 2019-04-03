package beginfunc.domain.models.viewModels.deals;


import beginfunc.domain.models.viewModels.BaseViewModel;

public class RecentDealCommentViewModel extends BaseViewModel {

    private String dateTime;
    private DealParticipantViewModel seller;
    private DealParticipantViewModel buyer;
    private String auctionId;
    private String auctionName;
    private String estimation;
    private String content;



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
