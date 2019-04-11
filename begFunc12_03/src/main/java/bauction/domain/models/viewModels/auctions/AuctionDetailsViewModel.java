package bauction.domain.models.viewModels.auctions;

import bauction.domain.models.viewModels.BaseViewModel;

import java.math.BigDecimal;

public class AuctionDetailsViewModel extends BaseViewModel {
    private String name;
    private String mainImageUrl;
    private BigDecimal reachedPrice;
    private BigDecimal wantedPrice;
    private AuctionDetailsBuyerViewModel seller;
    private AuctionDetailsBuyerViewModel buyer;
    private String town;
    private String remainingTime;
    private Integer views;
    private String description;
    private String status;
    private String type;
    private String productId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public BigDecimal getReachedPrice() {
        return reachedPrice;
    }

    public void setReachedPrice(BigDecimal reachedPrice) {
        this.reachedPrice = reachedPrice;
    }

    public BigDecimal getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(BigDecimal wantedPrice) {
        this.wantedPrice = wantedPrice;
    }


    public AuctionDetailsBuyerViewModel getSeller() {
        return seller;
    }

    public void setSeller(AuctionDetailsBuyerViewModel seller) {
        this.seller = seller;
    }

    public AuctionDetailsBuyerViewModel getBuyer() {
        return buyer;
    }

    public void setBuyer(AuctionDetailsBuyerViewModel buyer) {
        this.buyer = buyer;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
