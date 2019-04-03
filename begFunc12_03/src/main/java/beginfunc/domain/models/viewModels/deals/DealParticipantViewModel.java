package beginfunc.domain.models.viewModels.deals;

import beginfunc.domain.models.viewModels.BaseViewModel;

public class DealParticipantViewModel extends BaseViewModel {
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
