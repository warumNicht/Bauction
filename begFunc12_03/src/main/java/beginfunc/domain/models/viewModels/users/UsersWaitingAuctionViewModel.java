package beginfunc.domain.models.viewModels.users;

import beginfunc.domain.models.viewModels.BaseViewModel;

public class UsersWaitingAuctionViewModel extends BaseViewModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
