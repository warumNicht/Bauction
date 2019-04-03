package beginfunc.domain.entities.productRelated.products;

import beginfunc.domain.entities.enums.PreservationGrade;
import beginfunc.domain.entities.productRelated.Country;
import javax.persistence.*;

@Entity
@Table(name = "collection_product")
public abstract class BaseCollectionProduct extends BaseProduct {

    @Column(name = "year_of_issue")
    private Integer yearOfIssue;

    @Column(name = "exemplar_count")
    private Long exemplarCount;

    @Column(name = "grade")
    private PreservationGrade preservationGrade;

    @ManyToOne
    @JoinColumn(name = "country_id",referencedColumnName = "id")
    private Country country;

    public BaseCollectionProduct() {
    }

    public BaseCollectionProduct(String name) {
        super(name);
    }
}
