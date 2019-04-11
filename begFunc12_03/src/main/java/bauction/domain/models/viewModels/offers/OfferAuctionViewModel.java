package bauction.domain.models.viewModels.offers;

import bauction.domain.models.viewModels.BaseViewModel;

public class OfferAuctionViewModel extends BaseViewModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
