package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.participations.OfferServiceModel;

import java.util.List;

public interface OfferService {
    boolean registerOffer(OfferServiceModel offerServiceModel);

    List<OfferServiceModel> findAllOffersOfAuction(Integer auctionId);

    List<OfferServiceModel> findAllActiveOffersToUser(Integer userId);

    Long getAuctionOffersCount(Integer id);

    OfferServiceModel acceptOffer(Integer offerId);
}
