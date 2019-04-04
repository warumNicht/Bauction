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

    AuctionServiceModel findById(Integer id);

    boolean increaseAuctionViews(Integer id);

    void increaseCurrentPrice(Integer id, BigDecimal biddingStep);

    void updateAuction (AuctionServiceModel model);

    void updateAuctionStatus (Integer id, AuctionStatus status);

    List<AuctionServiceModel> getWaitingAuctionsOfUser(Integer userId);

    AuctionServiceModel createAuction(AuctionCreateBindingModel model, BaseCollectionBindingModel coin,
                       HttpSession session, UserServiceModel user) throws IOException;

}
