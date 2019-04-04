package beginfunc.domain.models.serviceModels.products;

import beginfunc.domain.models.serviceModels.products.BaseCollectionServiceModel;

public class CoinServiceModel extends BaseCollectionServiceModel {
    private String metal;
    private Double weight;
    private Double diameter;

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
