package beginfunc.domain.models.viewModels.home;

import beginfunc.domain.models.viewModels.BaseViewModel;

import java.math.BigDecimal;

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
