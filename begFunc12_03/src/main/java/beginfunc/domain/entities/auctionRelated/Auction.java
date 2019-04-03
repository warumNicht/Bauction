package beginfunc.domain.entities.auctionRelated;

import beginfunc.domain.entities.BaseEntity;
import beginfunc.domain.entities.User;
import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.entities.enums.AuctionType;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "auctions")
public  class Auction extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private AuctionType type;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private AuctionStatus status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "wanted_price")
    private BigDecimal wantedPrice;

    @Column(name = "reached_price")
    private BigDecimal reachedPrice;

    @Column(name = "views")
    private int views;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private BaseProduct product;

    @ManyToOne
    @JoinColumn(name = "seller_id",referencedColumnName = "id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id",referencedColumnName = "id")
    private User buyer;


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
        this.wantedPrice = wantedPrice;
    }

    public BigDecimal getReachedPrice() {
        return reachedPrice;
    }

    public void setReachedPrice(BigDecimal reachedPrice) {
        this.reachedPrice = reachedPrice;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BaseProduct getProduct() {
        return product;
    }

    public void setProduct(BaseProduct product) {
        this.product = product;
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
}
