package bauction.web.controllers;

import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.participations.OfferServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.domain.models.viewModels.offers.OfferViewModel;
import bauction.services.contracts.AuctionService;
import bauction.services.contracts.DealService;
import bauction.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/offers")
public class OfferController extends BaseController {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

    private final OfferService offerService;
    private final DealService dealService;
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferController(OfferService offerService, DealService dealService, AuctionService auctionService, ModelMapper modelMapper) {
        this.offerService = offerService;
        this.dealService = dealService;
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/fetch/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchUsersOffersToInteract(@PathVariable(name = "id") String  userId) {

        return this.offerService.findAllActiveOffersToUser(userId).stream()
                .map(o->{
                    OfferViewModel offerViewModel = this.modelMapper.map(o, OfferViewModel.class);
                    offerViewModel.getAuction().setName(o.getAuction().getProduct().getName());
                    offerViewModel.setExpirationTime(this.dateFormatter.format(o.getExpirationTime()));
                    return offerViewModel;
                }).collect(Collectors.toList());
    }

    @GetMapping("/accept/{id}")
    public ModelAndView acceptOffer(@PathVariable(name = "id") String  offerId, ModelAndView modelAndView){
        OfferServiceModel acceptedOffer = this.offerService.acceptOffer(offerId);
        UserServiceModel seller = this.modelMapper.map(super.getLoggedInUser(), UserServiceModel.class);
        DealServiceModel deal=new DealServiceModel(new Date(),acceptedOffer.getOfferedPrice(),
                seller,acceptedOffer.getParticipant(), acceptedOffer.getAuction());

        this.dealService.registerDeal(deal);
        AuctionServiceModel auction = acceptedOffer.getAuction();
        auction.setBuyer(acceptedOffer.getParticipant());
        auction.setStatus(AuctionStatus.Finished);
        this.auctionService.updateAuction(auction);

        modelAndView.setViewName("redirect:/users/profile/"+super.getLoggedInUserId());
        return modelAndView;
    }

}
