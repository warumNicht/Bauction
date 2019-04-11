package bauction.domain.models.bindingModels;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CategoryBindingModel {
    @NotNull
    @NotEmpty
    @Size(min = 2,max = 30)
    @Pattern(regexp = "^[A-ZА-Я].*$",message = "Category must begin with uppercase letter!")
    @Pattern(regexp = "^[\\w\\s-\\u0400-\\u04FF]+$",message = "Category contains only letters, digits, spaces, -, or _ !")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}
