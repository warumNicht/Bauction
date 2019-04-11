package bauction.domain.models.bindingModels.collectionProducts;

import javax.validation.constraints.*;

public class CoinBindingModel extends BaseCollectionBindingModel {
    @NotNull
    @Size(min = 2,max = 50)
    @Pattern(regexp = "^(?=.*\\S).+$|^$",flags = Pattern.Flag.DOTALL,
            message = "Metal of whitespaces is not valid!")
    private String metal;

    @NotNull
    @DecimalMin(value = "0.01", message = "Non negative value expected!")
    @DecimalMax(value = "150")
    private Double weight;

    @NotNull
    @DecimalMin(value = "0.01", message = "Non negative value expected!")
    @DecimalMax(value = "150")
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
