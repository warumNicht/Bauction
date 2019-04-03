package beginfunc.domain.entities.productRelated.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coins")
public class Coin extends BaseMoneyCollectionProduct {
    @Column(name = "metal")
    private String metal;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "diameter")
    private Double diameter;

    public Coin() {
    }

    public Coin(String name, Double diameter) {
        super(name);
        this.diameter = diameter;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }
}
