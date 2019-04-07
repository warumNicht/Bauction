package beginfunc.domain.models.viewModels.auctions.collectionDetails;

public class AuctionBanknoteViewDetailsModel extends AuctionViewCollectionDetails {
    private Integer length;
    private Integer width;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
