package beginfunc.domain.entities.productRelated;

import beginfunc.domain.entities.BaseEntity;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    @Column(name = "path")
    private String path;

    @Column(name = "extension")
    private String extension;

    @Column(name = "size")
    private Double size;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private BaseProduct product;



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

    public BaseProduct getProduct() {
        return product;
    }

    public void setProduct(BaseProduct product) {
        this.product = product;
    }
}
