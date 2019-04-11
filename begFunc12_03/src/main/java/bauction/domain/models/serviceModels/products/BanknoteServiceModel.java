package bauction.domain.models.serviceModels.products;

public class BanknoteServiceModel extends BaseCollectionServiceModel{
    private Integer length;
    private Integer width;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
