package beginfunc.domain.entities.productRelated;

import beginfunc.domain.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "countries")
public class Country extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
