package beginfunc.domain.entities.productRelated.products;

import beginfunc.domain.entities.BaseEntity;
import beginfunc.domain.entities.auctionRelated.Auction;
import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.entities.productRelated.Town;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseProduct extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "town_id",referencedColumnName = "id")
    private Town town;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "main_picture_id")
    private Picture mainPicture;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Picture> pictures;


    public BaseProduct() {
        this.pictures=new ArrayList<>();
    }

    public BaseProduct(String name) {
        this.name = name;
        this.pictures=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Picture getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Picture mainPicture) {
        this.mainPicture = mainPicture;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

}
