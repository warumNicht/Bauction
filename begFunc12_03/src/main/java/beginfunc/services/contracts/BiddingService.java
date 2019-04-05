package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.participations.BiddingServiceModel;

import java.util.List;

public interface BiddingService {
    boolean registerBidding(BiddingServiceModel biddingServiceModel);

    List<BiddingServiceModel> findAllBiddingsOfAuction(String auctionId);

    Long getAuctionBiddingCount(String id);
}
