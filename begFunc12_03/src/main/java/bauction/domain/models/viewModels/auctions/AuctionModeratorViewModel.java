package bauction.domain.models.viewModels.auctions;

import bauction.domain.models.viewModels.BaseViewModel;

public class AuctionModeratorViewModel extends BaseViewModel {
    private String name;
    private String category;
    private String mainImageUrl;
    private String currentPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
