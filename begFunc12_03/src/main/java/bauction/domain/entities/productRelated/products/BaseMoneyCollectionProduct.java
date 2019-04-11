package bauction.domain.entities.productRelated.products;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseMoneyCollectionProduct extends BaseCollectionProduct {
    @Column(name = "nominal_value")
    private Double nominalValue;

    public BaseMoneyCollectionProduct() {
    }

    public BaseMoneyCollectionProduct(String name) {
        super(name);
    }

    public Double getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(Double nominalValue) {
        this.nominalValue = nominalValue;
    }
}
