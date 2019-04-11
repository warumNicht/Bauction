package bauction.services.contracts;

import bauction.domain.models.serviceModels.participations.BiddingServiceModel;

import java.util.List;

public interface BiddingService {
    void registerBidding(BiddingServiceModel biddingServiceModel);

    List<BiddingServiceModel> findAllBiddingsOfAuction(String auctionId);

    Long getAuctionBiddingCount(String id);

    BiddingServiceModel findHighestBiddingOfAuction(String id);

    void deleteBiddingsOfAuctionById(String id);
}
