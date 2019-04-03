package beginfunc.domain.entities.auctionRelated;

import beginfunc.domain.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
