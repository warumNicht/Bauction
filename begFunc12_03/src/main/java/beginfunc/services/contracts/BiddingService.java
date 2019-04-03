package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.participations.BiddingServiceModel;

import java.util.List;

public interface BiddingService {
    boolean registerBidding(BiddingServiceModel biddingServiceModel);

    List<BiddingServiceModel> findAllBiddingsOfAuction(Integer auctionId);

    Long getAuctionBiddingCount(Integer id);
}
