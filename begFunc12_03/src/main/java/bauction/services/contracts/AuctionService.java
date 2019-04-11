package bauction.services.contracts;

import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface AuctionService {

    List<AuctionServiceModel> findAllAuctionsByStatus(AuctionStatus status);

    AuctionServiceModel findById(String id);

    void increaseAuctionViews(String id);

    void increaseCurrentPrice(String id, BigDecimal biddingStep);

    void updateAuction (AuctionServiceModel model);

    List<AuctionServiceModel> getWaitingAuctionsOfUser(String userId);

    AuctionServiceModel createAuction(AuctionCreateBindingModel model, BaseCollectionBindingModel coin,
                       HttpSession session, UserServiceModel user) throws IOException;

    void editAuction(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model, CoinBindingModel coin, BanknoteBindingModel banknote, File main, File[] files) throws IOException;

    void startAuction(AuctionServiceModel auction);

    void deleteById(String id);

    List<AuctionServiceModel> getActiveAuctionsOfUser(String userId);

    List<AuctionServiceModel> getFinishedAuctionsOfUserWithDeal(String userId);

    List<AuctionServiceModel> getFinishedAuctionsOfUserWithoutDeal(String userId);

    String getAuctionSellerId(String auctionId);

    List<AuctionServiceModel> findAllActivesAuctionsExceedingEndDate();

    List<AuctionServiceModel> findAllFinishedAuctionsWithoutDeal();

    List<AuctionServiceModel> getSortedAuctions(String category, String criteria);
}
