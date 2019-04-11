package bauction.domain.entities.productRelated.products;

import bauction.domain.entities.enums.PreservationGrade;

import javax.persistence.*;

@Entity
@Table(name = "collection_product")
public abstract class BaseCollectionProduct extends BaseProduct {

    @Column(name = "year_of_issue", nullable = false)
    private Integer yearOfIssue;


    @Column(name = "grade",nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PreservationGrade grade;

    @Column(name = "country", nullable = false)
    private String country;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
