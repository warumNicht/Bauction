package bauction.domain.models.viewModels.home;

import bauction.domain.models.viewModels.BaseViewModel;

public class AuctionHomeViewModel extends BaseViewModel {
    private String name;
    private String mainImageUrl;
    private String currentPrice;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }


    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }
}
