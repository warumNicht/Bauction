package bauction.domain.entities.productRelated.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "banknotes")
public class BankNote extends BaseMoneyCollectionProduct {
    @Column(name = "length", nullable = false)
    private Integer length;

    @Column(name = "width", nullable = false)
    private Integer width;

    public BankNote() {
    }

    public BankNote(String name, Integer length, Integer width) {
        super(name);
        this.length = length;
        this.width = width;
    }

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
