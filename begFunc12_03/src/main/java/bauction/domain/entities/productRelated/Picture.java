package bauction.domain.entities.productRelated;

import bauction.domain.entities.BaseEntity;
import bauction.domain.entities.productRelated.products.BaseProduct;
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
