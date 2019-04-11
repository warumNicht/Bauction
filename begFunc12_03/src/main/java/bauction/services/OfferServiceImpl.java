package bauction.services;

import bauction.constants.ErrorMessagesConstants;
import bauction.domain.entities.auctionRelated.Offer;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.participations.OfferServiceModel;
import bauction.error.NoPositiveOfferException;
import bauction.repositories.OfferRepository;
import bauction.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper, ModelMapper modelMapper2) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerOffer(OfferServiceModel offerServiceModel) {
        if(offerServiceModel.getOfferedPrice().compareTo(BigDecimal.ZERO)<0){
            throw new NoPositiveOfferException(ErrorMessagesConstants.NO_POSITIVE_OFFER_MESSAGE);
        }
        Offer offer = this.modelMapper.map(offerServiceModel, Offer.class);
        offer.setExpirationTime(this.getTimeAfter24H());
        this.offerRepository.saveAndFlush(offer);
    }

    private Date getTimeAfter24H() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    @Override
    public List<OfferServiceModel> findAllOffersOfAuction(String auctionId) {
        return this.offerRepository.findAllOffersOfAuction(auctionId).stream()
                .map(o -> this.modelMapper.map(o, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferServiceModel> findAllActiveOffersToUser(String userId) {
        List<Offer> offers = this.offerRepository.findAllActiveOffersToUser(userId);
        return offers.stream().map(o -> this.modelMapper
                .map(o, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getAuctionOffersCount(String id) {
        Long auctionOfferCount = this.offerRepository.getAuctionOfferCount(id);
        return auctionOfferCount == null ? 0 : auctionOfferCount;
    }

    @Override
    public OfferServiceModel acceptOffer(String offerId) {
        Offer offer = this.offerRepository.findById(offerId).orElse(null);
        offer.setAccepted(true);
        Offer accepted = this.offerRepository.save(offer);
        this.offerRepository.invalidateOffersOfAuctionById(offer.getAuction().getId());

        AuctionServiceModel auction = this.modelMapper.map(accepted.getAuction(), AuctionServiceModel.class);
        OfferServiceModel map = this.modelMapper.map(accepted, OfferServiceModel.class);
        map.setAuction(auction);
        return map;
    }

    @Override
    public void invalidateOffersOfAuction(String id) {
        this.offerRepository.invalidateOffersOfAuctionById(id);
    }

    @Override
    public void deleteOffersOfAuctionById(String id) {
        this.offerRepository.deleteOffersOfAuctionById(id);
    }


}
