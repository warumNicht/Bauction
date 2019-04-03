package beginfunc.services;

import beginfunc.domain.entities.auctionRelated.Offer;
import beginfunc.domain.models.serviceModels.participations.OfferServiceModel;
import beginfunc.repositories.OfferRepository;
import beginfunc.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean registerOffer(OfferServiceModel offerServiceModel) {
        try {
            Offer offer = this.modelMapper.map(offerServiceModel, Offer.class);
            offer.setExpirationTime(this.getTimeAfter24H());
            this.offerRepository.saveAndFlush(offer);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Date getTimeAfter24H() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    @Override
    public List<OfferServiceModel> findAllOffersOfAuction(Integer auctionId) {
        return this.offerRepository.findAllOffersOfAuction(auctionId).stream()
                .map(o->this.modelMapper.map(o,OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OfferServiceModel> findAllActiveOffersToUser(Integer userId) {
        List<Offer> offers=this.offerRepository.findAllActiveOffersToUser(userId);
        return offers.stream().map(o->this.modelMapper
                .map(o,OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getAuctionOffersCount(Integer id) {
        Long auctionOfferCount = this.offerRepository.getAuctionOfferCount(id);
        return auctionOfferCount==null ? 0 : auctionOfferCount;
    }

    @Override
    public OfferServiceModel acceptOffer(Integer offerId) {
        Offer offer = this.offerRepository.findById(offerId).orElse(null);
        offer.setAccepted(true);
        Offer accepted = this.offerRepository.save(offer);
        this.offerRepository.invalidateOffersOfAuctionById(offer.getAuction().getId());
        return this.modelMapper.map(accepted,OfferServiceModel.class);
    }
}
