package bauction.domain.entities.productRelated.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coins")
public class Coin extends BaseMoneyCollectionProduct {
    @Column(name = "metal", nullable = false, length = 50)
    private String metal;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "diameter", nullable = false)
    private Double diameter;

    public Coin() {
    }

    public Coin(String name, Double diameter) {
        super(name);
        this.diameter = diameter;
    }

    public String getMetal() {
        return metal;
    }

    public void setMetal(String metal) {
        this.metal = metal;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }
}
