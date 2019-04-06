package beginfunc.web.controllers;

import beginfunc.constants.StaticImagesConstants;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.participations.OfferServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.domain.models.serviceModels.participations.BiddingServiceModel;
import beginfunc.domain.models.viewModels.auctions.AuctionDetailsViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.BiddingService;
import beginfunc.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/auctions")
public class AuctionController extends BaseController{
    private final AuctionService auctionService;
    private final BiddingService biddingService;
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionController(AuctionService auctionService, BiddingService biddingService, OfferService offerService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.biddingService = biddingService;
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/details/{id}")
    public ModelAndView  auctionDetails(@PathVariable(name = "id") String id, ModelAndView modelAndView){
        this.auctionService.increaseAuctionViews(id);

        AuctionServiceModel found = this.auctionService.findById(id);
        AuctionDetailsViewModel model = this.modelMapper.map(found, AuctionDetailsViewModel.class);

        model.setName(found.getProduct().getName());
        model.setDescription(found.getProduct().getDescription());
        model.setProductId(found.getProduct().getId());
        if(found.getEndDate()!=null){
            model.setRemainingTime(this.getFormedDateDifference(found.getEndDate()));
        }
        if(found.getProduct().getMainPicture()!=null){
            model.setMainImageUrl(found.getProduct().getMainPicture().getPath());
        }else {
            model.setMainImageUrl("/"+StaticImagesConstants.DEFAULT_AUCTION_MAIN_IMAGE);
        }
        model.setTown(found.getProduct().getTown().getName());

        modelAndView.addObject("auctionDetails",model);
        modelAndView.setViewName("auction/auction-details");
        return modelAndView;
    }



    @PostMapping("/bidding/{id}")
    public ModelAndView makeBidding(@PathVariable(name = "id") String id, ModelAndView modelAndView,
                                    @RequestParam("price") BigDecimal biddingStep){
        AuctionServiceModel auction = this.auctionService.findById(id);
        UserServiceModel participant = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);

        BiddingServiceModel bidding=new BiddingServiceModel(new Date(),participant,auction,
                biddingStep, auction.getReachedPrice().add(biddingStep));

        if(this.biddingService.registerBidding(bidding)){
           this.auctionService.increaseCurrentPrice(id, biddingStep);
        }

        modelAndView.setViewName("redirect:/auctions/details/" + id);
        return modelAndView;
    }

    @PostMapping("/offers/{id}")
    public ModelAndView makeOffer(@PathVariable(name = "id") String id, ModelAndView modelAndView,
                                    @RequestParam("offerPrice") BigDecimal offeredPrice){
        AuctionServiceModel auction = this.auctionService.findById(id);
        UserServiceModel participant = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);
        OfferServiceModel offer=new OfferServiceModel(new Date(),participant,auction,offeredPrice);
        this.offerService.registerOffer(offer);

        modelAndView.setViewName("redirect:/auctions/details/" + id);
        return modelAndView;
    }

    private String getFormedDateDifference(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d', 'yyyy HH:mm:ss");
        return format.format(date);
    }
}
