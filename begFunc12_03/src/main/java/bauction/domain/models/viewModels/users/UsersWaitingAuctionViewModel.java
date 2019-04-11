package bauction.domain.models.viewModels.users;

import bauction.domain.models.viewModels.BaseViewModel;

public class UsersWaitingAuctionViewModel extends BaseViewModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
