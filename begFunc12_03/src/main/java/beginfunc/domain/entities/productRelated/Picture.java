package beginfunc.domain.entities.productRelated;

import beginfunc.domain.entities.BaseEntity;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    @Column(name = "path")
    private String path;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private BaseProduct product;

    public Picture() {
    }

    public Picture(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BaseProduct getProduct() {
        return product;
    }

    public void setProduct(BaseProduct product) {
        this.product = product;
    }
}
