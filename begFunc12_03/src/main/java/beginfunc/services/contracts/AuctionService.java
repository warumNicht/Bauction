package beginfunc.services.contracts;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface AuctionService {
    AuctionServiceModel createAuction(AuctionServiceModel model);

    List<AuctionServiceModel> findAllActivesAuctions();

    AuctionServiceModel findById(Integer id);

    boolean increaseAuctionViews(Integer id);

    void increaseCurrentPrice(Integer id, BigDecimal biddingStep);

    void updateAuction (AuctionServiceModel model);

    void updateAuctionStatus (Integer id, AuctionStatus status);
}
