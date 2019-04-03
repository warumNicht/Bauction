package beginfunc.domain.entities;


import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false,unique = true, updatable = false)
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
