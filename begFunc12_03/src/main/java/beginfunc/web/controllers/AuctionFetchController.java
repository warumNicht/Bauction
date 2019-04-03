package beginfunc.web.controllers;

import beginfunc.constants.AppConstants;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.viewModels.auctions.AuctionBiddingViewModel;
import beginfunc.domain.models.viewModels.auctions.AuctionOfferViewModel;
import beginfunc.domain.models.viewModels.home.AuctionHomeMoreViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.BiddingService;
import beginfunc.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auctions/fetch")
public class AuctionFetchController {
    private final AuctionService auctionService;
    private final BiddingService biddingService;
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionFetchController(AuctionService auctionService, BiddingService biddingService, OfferService offerService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.biddingService = biddingService;
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchAuctionMoreView(@PathVariable(name = "id") Integer id){
        AuctionServiceModel found = this.auctionService.findById(id);
        if(found!=null){
            AuctionHomeMoreViewModel model = this.modelMapper.map(found, AuctionHomeMoreViewModel.class);
            model.setName(found.getProduct().getName());
            if(found.getEndDate()!=null){
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                String endDate = format.format(found.getEndDate());
                model.setExpiresAt(endDate);}

            if(found.getReachedPrice()==null){
                model.setCurrentPrice("0.00 "+'\u20ac');
            }else {
                model.setCurrentPrice(String.format("%.2f ", found.getReachedPrice())+'\u20ac');
            }
            if(found.getProduct().getMainPicture()!=null){
                model.setMainImageUrl(found.getProduct().getMainPicture().getPath());
            }else {
                model.setMainImageUrl(AppConstants.DEFAULT_AUCTION_MAIN_IMAGE_PATH);
            }
            model.setSeller(found.getSeller().getUsername());
            model.setTown(found.getProduct().getTown().getName());
            return model;
        }
        return null;
    }


    @GetMapping(value = "/biddings/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchAuctionBiddings(@PathVariable(name = "id") Integer id){
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

        List<AuctionBiddingViewModel> biddingViewModels = this.biddingService.findAllBiddingsOfAuction(id).stream()
                .map(b -> {
                    AuctionBiddingViewModel biddingView = this.modelMapper.map(b, AuctionBiddingViewModel.class);
                    String formedTime = format.format(b.getSubmittedOn());
                    biddingView.setSubmittedOn(formedTime);
                    return biddingView;
                })
                .collect(Collectors.toList());
        return biddingViewModels;
    }

    @GetMapping(value = "/offers/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchAuctionOffers(@PathVariable(name = "id") Integer id){
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

        List<AuctionOfferViewModel> offers = this.offerService.findAllOffersOfAuction(id).stream()
                .map(offer -> {
                    AuctionOfferViewModel model = this.modelMapper.map(offer, AuctionOfferViewModel.class);
                    model.setSubmittedOn(format.format(offer.getSubmittedOn()));
                    return model;
                }).collect(Collectors.toList());
        return offers;
    }

    @GetMapping(value = "/biddings/count/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchAuctionBiddingsCount(@PathVariable(name = "id") Integer id){
        return this.biddingService.getAuctionBiddingCount(id);
    }

    @GetMapping(value = "/offers/count/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchAuctionOffersCount(@PathVariable(name = "id") Integer id){
        Long offersCount=this.offerService.getAuctionOffersCount(id);
        return offersCount;
    }
}
