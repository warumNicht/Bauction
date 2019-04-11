package bauction.domain.models.viewModels.users;


import bauction.domain.models.viewModels.BaseViewModel;

public class UserProfileViewModel extends BaseViewModel {
    private String username;
    private String registrationDate;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}
