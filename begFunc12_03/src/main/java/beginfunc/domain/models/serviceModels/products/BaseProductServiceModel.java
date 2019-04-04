package beginfunc.domain.models.serviceModels.products;

import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.models.serviceModels.BaseServiceModel;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.domain.models.serviceModels.TownServiceModel;

import java.util.List;

public class BaseProductServiceModel extends BaseServiceModel {
    private String name;
    private String description;
    private TownServiceModel town;
    private PictureServiceModel mainPicture;
    private List<PictureServiceModel> pictures;

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

    public TownServiceModel getTown() {
        return town;
    }

    public void setTown(TownServiceModel town) {
        this.town = town;
    }

    public PictureServiceModel getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(PictureServiceModel mainPicture) {
        this.mainPicture = mainPicture;
    }

    public List<PictureServiceModel> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureServiceModel> pictures) {
        this.pictures = pictures;
    }

}
