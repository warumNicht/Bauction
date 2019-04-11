package bauction.domain.models.viewModels.auctions;

import bauction.domain.models.viewModels.BaseViewModel;

public class AuctionDetailsBuyerViewModel extends BaseViewModel {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
