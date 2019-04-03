package beginfunc.domain.models.serviceModels.deals;

import beginfunc.domain.entities.enums.Estimation;
import beginfunc.domain.models.serviceModels.BaseServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;

import java.util.Date;

public class CommentServiceModel extends BaseServiceModel {
    private Date date;
    private String content;
    private Estimation estimation;
    private UserServiceModel author;

    public CommentServiceModel() {
    }

    public CommentServiceModel(Date date, String content, Estimation estimation, UserServiceModel author) {
        this.date = date;
        this.content = content;
        this.estimation = estimation;
        this.author = author;
    }

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

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }
}
