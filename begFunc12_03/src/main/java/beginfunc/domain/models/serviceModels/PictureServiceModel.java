package beginfunc.domain.models.serviceModels;

public class PictureServiceModel extends BaseProductServiceModel{
    private String path;
    private String extension;
    private Double size;
    private String contentType;
    private BaseProductServiceModel product;

    public PictureServiceModel() {
    }

    public PictureServiceModel(String path, String extension, Double size, String contentType) {
        this.path = path;
        this.extension = extension;
        this.size = size;
        this.contentType = contentType;
        this.product=new BaseProductServiceModel();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public BaseProductServiceModel getProduct() {
        return product;
    }

    public void setProduct(BaseProductServiceModel product) {
        this.product = product;
    }
}
