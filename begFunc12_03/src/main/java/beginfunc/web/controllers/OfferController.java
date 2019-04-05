package beginfunc.web.controllers;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.deals.DealServiceModel;
import beginfunc.domain.models.serviceModels.participations.OfferServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.domain.models.viewModels.offers.OfferViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.DealService;
import beginfunc.services.contracts.OfferService;
import beginfunc.services.contracts.UserService;
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
