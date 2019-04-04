package beginfunc.domain.models.bindingModels.collectionProducts;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BanknoteBindingModel extends BaseCollectionBindingModel {
    @NotNull
    @Min(value = 1)
    @Max(value = 400)
    private Integer length;

    @NotNull
    @Min(value = 1)
    @Max(value = 400)
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
