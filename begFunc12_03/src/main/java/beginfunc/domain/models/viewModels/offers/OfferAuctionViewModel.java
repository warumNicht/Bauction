package beginfunc.domain.models.viewModels.offers;

import beginfunc.domain.models.viewModels.BaseViewModel;

public class OfferAuctionViewModel extends BaseViewModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
