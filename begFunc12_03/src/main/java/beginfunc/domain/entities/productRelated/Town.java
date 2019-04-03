package beginfunc.domain.entities.productRelated;

import beginfunc.domain.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    private String name;

    public Town() {
    }

    public Town(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
