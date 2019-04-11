package bauction.domain.models.bindingModels;

import bauction.annotations.EnumValidator;
import bauction.domain.entities.enums.Estimation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CommentBindingModel {
    private String dealId;
    private String dealMoment;
    private String auctionName;
    private String partnerRole;

    public CommentBindingModel() {
    }

    public CommentBindingModel(String dealId, String dealMoment, String auctionName, String partnerRole) {
        this.dealId = dealId;
        this.dealMoment = dealMoment;
        this.auctionName = auctionName;
        this.partnerRole = partnerRole;
    }

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 200)
    @Pattern(regexp = "^(?=.*\\S).+$|^$",flags = Pattern.Flag.DOTALL,
            message = "Comment of whitespaces is not valid!")
    private String content;

    @NotNull
    @NotEmpty
    @EnumValidator(enumClass = Estimation.class,message = "Estimation not valid!")
    private String estimation;

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getDealMoment() {
        return dealMoment;
    }

    public void setDealMoment(String dealMoment) {
        this.dealMoment = dealMoment;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getPartnerRole() {
        return partnerRole;
    }

    public void setPartnerRole(String partnerRole) {
        this.partnerRole = partnerRole;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }
}
