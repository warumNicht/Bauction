package beginfunc.domain.models.serviceModels;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.entities.enums.AuctionType;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;

import java.math.BigDecimal;
import java.util.Date;

public class AuctionServiceModel extends BaseServiceModel {
    private AuctionType type;
    private AuctionStatus status;
    private Date startDate;
    private Date endDate;
    private BigDecimal wantedPrice;
    private BigDecimal reachedPrice=BigDecimal.ZERO;
    private Long views;
    private CategoryServiceModel category;
    private BaseProductServiceModel product;
    private UserServiceModel seller;
    private UserServiceModel buyer;


    public AuctionType getType() {
        return type;
    }

    public void setType(AuctionType type) {
        this.type = type;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getWantedPrice() {
        return wantedPrice;
    }

    public void setWantedPrice(BigDecimal wantedPrice) {
        if(wantedPrice==null){
            this.wantedPrice=BigDecimal.ZERO;
        }else {
            this.wantedPrice = wantedPrice;
        }
    }

    public BigDecimal getReachedPrice() {
        return reachedPrice;
    }

    public void setReachedPrice(BigDecimal reachedPrice) {
        this.reachedPrice = reachedPrice;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public CategoryServiceModel getCategory() {
        return category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }

    public BaseProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(BaseProductServiceModel product) {
        this.product = product;
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
}
