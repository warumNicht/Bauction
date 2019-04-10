package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.participations.OfferServiceModel;

import java.util.List;

public interface OfferService {
    void registerOffer(OfferServiceModel offerServiceModel);

    List<OfferServiceModel> findAllOffersOfAuction(String auctionId);

    List<OfferServiceModel> findAllActiveOffersToUser(String userId);

    Long getAuctionOffersCount(String id);

    OfferServiceModel acceptOffer(String offerId);

    void invalidateOffersOfAuction(String id);

    void deleteOffersOfAuctionById(String id);
}
