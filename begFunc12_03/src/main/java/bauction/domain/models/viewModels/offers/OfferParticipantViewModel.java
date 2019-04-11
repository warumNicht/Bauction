package bauction.domain.models.viewModels.offers;

import bauction.domain.models.viewModels.BaseViewModel;

public class OfferParticipantViewModel extends BaseViewModel {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
