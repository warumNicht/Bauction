package beginfunc.domain.models.viewModels.auctions.collectionDetails;

import beginfunc.domain.entities.enums.PreservationGrade;

public abstract class AuctionViewCollectionDetails {

    private Integer yearOfIssue;
    private PreservationGrade grade;
    private String country;
    private Double nominalValue;

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

    public Double getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(Double nominalValue) {
        this.nominalValue = nominalValue;
    }
}
