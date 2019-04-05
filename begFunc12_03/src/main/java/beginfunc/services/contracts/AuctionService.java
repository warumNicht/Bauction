package beginfunc.services.contracts;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface AuctionService {

    List<AuctionServiceModel> findAllActivesAuctions();

    AuctionServiceModel findById(String id);

    boolean increaseAuctionViews(String id);

    void increaseCurrentPrice(String id, BigDecimal biddingStep);

    void updateAuction (AuctionServiceModel model);

    void updateAuctionStatus (String id, AuctionStatus status);

    List<AuctionServiceModel> getWaitingAuctionsOfUser(String userId);

    AuctionServiceModel createAuction(AuctionCreateBindingModel model, BaseCollectionBindingModel coin,
                       HttpSession session, UserServiceModel user) throws IOException;

}
