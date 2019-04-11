package bauction.web.controllers;

import bauction.constants.StaticImagesConstants;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.products.BanknoteServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.products.CoinServiceModel;
import bauction.domain.models.viewModels.auctions.AuctionBiddingViewModel;
import bauction.domain.models.viewModels.auctions.AuctionOfferViewModel;
import bauction.domain.models.viewModels.auctions.collectionDetails.AuctionBanknoteViewDetailsModel;
import bauction.domain.models.viewModels.auctions.collectionDetails.AuctionCoinViewDetailsModel;
import bauction.domain.models.viewModels.home.AuctionHomeMoreViewModel;
import bauction.domain.models.viewModels.home.AuctionHomeViewModel;
import bauction.services.contracts.AuctionService;
import bauction.services.contracts.BiddingService;
import bauction.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
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
    public Object fetchAuctionMoreView(@PathVariable(name = "id") String id) {
        AuctionServiceModel found = this.auctionService.findById(id);
        AuctionHomeMoreViewModel model = this.modelMapper.map(found, AuctionHomeMoreViewModel.class);
        model.setName(found.getProduct().getName());
        if (found.getEndDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String endDate = format.format(found.getEndDate());
            model.setExpiresAt(endDate);
        }
        model.setCurrentPrice(String.format("%.2f ", found.getReachedPrice()) + '\u20ac');

        if (found.getProduct().getMainPicture() != null) {
            model.setMainImageUrl(found.getProduct().getMainPicture().getPath());
        } else {
            model.setMainImageUrl(StaticImagesConstants.DEFAULT_AUCTION_MAIN_IMAGE);
        }
        model.setSeller(found.getSeller().getUsername());
        model.setTown(found.getProduct().getTown().getName());
        return model;
    }


    @GetMapping(value = "/biddings/{id}", produces = "application/json")
    public Object fetchAuctionBiddings(@PathVariable(name = "id") String id) {
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
    public Object fetchAuctionOffers(@PathVariable(name = "id") String id) {
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
    public Object fetchAuctionBiddingsCount(@PathVariable(name = "id") String id) {
        return this.biddingService.getAuctionBiddingCount(id);
    }

    @GetMapping(value = "/offers/count/{id}", produces = "application/json")
    public Object fetchAuctionOffersCount(@PathVariable(name = "id") String id) {
        return this.offerService.getAuctionOffersCount(id);
    }

    @GetMapping(value = "/collectionDetails/{id}", produces = "application/json")
    public Object fetchAuctionCollectionDetails(@PathVariable(name = "id") String id) {
        AuctionServiceModel found = this.auctionService.findById(id);
        BaseProductServiceModel product = found.getProduct();
        if(product instanceof CoinServiceModel){
            AuctionCoinViewDetailsModel coinViewDetailsModel = this.modelMapper.map(product, AuctionCoinViewDetailsModel.class);
            return coinViewDetailsModel;
        }else if(product instanceof BanknoteServiceModel){
            AuctionBanknoteViewDetailsModel banknoteViewDetailsModel = this.modelMapper.map(product, AuctionBanknoteViewDetailsModel.class);
            return banknoteViewDetailsModel;
        }
        return null;
    }

    @GetMapping(value = "/user/{id}/actives", produces = "application/json")
    public Object fetchActiveAuctionsOfUser(@PathVariable(name = "id") String userId) {
        List<AuctionServiceModel> actives= this.auctionService.getActiveAuctionsOfUser(userId);
        return this.mapServiceToViewModels(actives);
    }

    @GetMapping(value = "/user/{id}/finishedWithDeal", produces = "application/json")
    public Object fetchFinishedAuctionsOfUserWithDeal(@PathVariable(name = "id") String userId) {
        List<AuctionServiceModel> actives= this.auctionService.getFinishedAuctionsOfUserWithDeal(userId);
        return this.mapServiceToViewModels(actives);
    }

    @GetMapping(value = "/user/{id}/finishedWithoutDeal", produces = "application/json")
    public Object fetchFinishedAuctionsOfUserWithoutDeal(@PathVariable(name = "id") String userId) {
        List<AuctionServiceModel> actives= this.auctionService.getFinishedAuctionsOfUserWithoutDeal(userId);
        return this.mapServiceToViewModels(actives);
    }

    @GetMapping(value = "/sort/{category}/{criteria}", produces = "application/json")
    public Object fetchSortedAuctions(@PathVariable(name = "category") String category,
                                      @PathVariable(name = "criteria") String criteria) {

        List<AuctionServiceModel> actives=this.auctionService.getSortedAuctions(category, criteria);
        return this.mapServiceToViewModels(actives);
    }

    private List<AuctionHomeViewModel> mapServiceToViewModels(List<AuctionServiceModel> auctionServiceModels){
        return auctionServiceModels.stream()
                .map(a->{
                    AuctionHomeViewModel homeViewModel = this.modelMapper.map(a,AuctionHomeViewModel.class);
                    String name = a.getProduct().getName();
                    if(name.length()>=19){
                        name=name.substring(0,18) + "...";
                    }
                    homeViewModel.setName(name);
                    if(a.getProduct().getMainPicture()==null){
                        homeViewModel.setMainImageUrl(StaticImagesConstants.DEFAULT_AUCTION_MAIN_IMAGE);
                    }else {
                        homeViewModel.setMainImageUrl(a.getProduct().getMainPicture().getPath());
                    }
                    homeViewModel.setCurrentPrice(String.format("%.2f ", a.getReachedPrice()) +'\u20ac');
                    return homeViewModel;
                }).collect(Collectors.toList());
    }
}
