package beginfunc.domain.entities.productRelated.products;

import beginfunc.domain.entities.enums.PreservationGrade;
import beginfunc.domain.entities.productRelated.Country;
import javax.persistence.*;

@Entity
@Table(name = "collection_product")
public abstract class BaseCollectionProduct extends BaseProduct {

    @Column(name = "year_of_issue")
    private Integer yearOfIssue;


    @Column(name = "grade")
    @Enumerated(value = EnumType.STRING)
    private PreservationGrade grade;

    @ManyToOne
    @JoinColumn(name = "country_id",referencedColumnName = "id")
    private Country country;

    public BaseCollectionProduct() {
    }

    public BaseCollectionProduct(String name) {
        super(name);
    }

    public Integer getYearOfIssue() {
        return yearOfIssue;
    }

    public void setYearOfIssue(Integer yearOfIssue) {
        this.yearOfIssue = yearOfIssue;
    }

    public PreservationGrade getGrade() {
        return grade;
    }

    public void setGrade(PreservationGrade grade) {
        this.grade = grade;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
