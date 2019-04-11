package bauction.domain.models.bindingModels.collectionProducts;

import bauction.annotations.EnumValidator;
import bauction.domain.entities.enums.PreservationGrade;

import javax.validation.constraints.*;

public abstract class BaseCollectionBindingModel {
    @NotNull
    @Min(value = 1)
    @Max(value = 2019)
    private Integer yearOfIssue;

    @NotNull
    @NotEmpty
    @EnumValidator(enumClass = PreservationGrade.class,message = "Not valid")
    private String grade;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?=.*\\S).+$|^$",flags = Pattern.Flag.DOTALL,
            message = "Country of whitespaces is not valid!")
    private String country;

    @NotNull
    @DecimalMin(value = "0.01", message = "Non negative value expected!")
    private Double nominalValue;

    public Integer getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(Integer yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(Double nominalValue) {
        this.nominalValue = nominalValue;
    }
}
