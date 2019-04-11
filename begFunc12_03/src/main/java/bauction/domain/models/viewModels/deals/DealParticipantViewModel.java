package bauction.domain.models.viewModels.deals;

import bauction.domain.models.viewModels.BaseViewModel;

public class DealParticipantViewModel extends BaseViewModel {
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
