package beginfunc.domain.models.serviceModels;

import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;

public class PictureServiceModel extends BaseProductServiceModel {
    private String path;
    private BaseProductServiceModel product;

    public PictureServiceModel() {
    }

    public PictureServiceModel(String path) {
        this.path = path;
        this.product=new BaseProductServiceModel();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BaseProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(BaseProductServiceModel product) {
        this.product = product;
    }
}
