package bauction.domain.entities;

import bauction.domain.entities.enums.Estimation;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity{
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "estimation", nullable = false)
    private Estimation estimation;

    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id", nullable = false)
    private User author;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Estimation getEstimation() {
        return estimation;
    }

    public void setEstimation(Estimation estimation) {
        this.estimation = estimation;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
