package bauction.web.controllers;

import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.deals.CommentServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.domain.models.viewModels.deals.RecentDealCommentViewModel;
import bauction.domain.models.viewModels.deals.RecentDealViewModel;
import bauction.services.contracts.AuctionService;
import bauction.services.contracts.DealService;
import bauction.services.contracts.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/deals")
public class DealController extends BaseController {
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");

    private final DealService dealService;
    private final AuctionService auctionService;
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @Autowired
    public DealController(DealService dealService, AuctionService auctionService,
                          OfferService offerService, ModelMapper modelMapper) {
        this.dealService = dealService;
        this.auctionService = auctionService;
        this.offerService = offerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ModelAndView registerDeal(@RequestParam(name = "auctionId") String auctionId,
                                     ModelAndView modelAndView) {
        AuctionServiceModel auction = this.auctionService.findById(auctionId);
        UserServiceModel buyer = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);
        DealServiceModel deal = new DealServiceModel(new Date(), auction.getReachedPrice(),
                auction.getSeller(), buyer, auction);
        this.dealService.registerDeal(deal);
        this.offerService.invalidateOffersOfAuction(auction.getId());
        auction.setStatus(AuctionStatus.Finished);
        auction.setBuyer(buyer);
        this.auctionService.updateAuction(auction);
        modelAndView.setViewName("redirect:/users/profile/" + super.getLoggedInUserId());
        return modelAndView;
    }

    @GetMapping(value = "/fetch/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchUsersDealsToInteract(@PathVariable(name = "id") String userId) {
        return this.dealService.allRecentDealsOfUser(userId)
                .stream()
                .map(d -> {
                    RecentDealViewModel dealViewModel = this.modelMapper.map(d, RecentDealViewModel.class);
                    dealViewModel.setAuctionName(d.getAuction().getProduct().getName());
                    dealViewModel.setDateTime(this.dateFormatter.format(d.getDateTime()));
                    dealViewModel.setDealPrice(String.format("%.2f",d.getDealPrice()));
                    if(d.getBuyerComment()!=null){
                        dealViewModel.setHasBuyerComment(true);
                    }
                    if(d.getSellerComment()!=null){
                        dealViewModel.setHasSellerComment(true);
                    }
                    return dealViewModel;
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/comments/fetch/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchUsersDealComments(@PathVariable(name = "id") String userId) {
        List<DealServiceModel> deals= this.dealService.allDealCommentsOfUser(userId);
        List<RecentDealCommentViewModel> commentsDeals=new ArrayList<>();
        for (DealServiceModel deal : deals) {
            RecentDealCommentViewModel dealViewModel = this.modelMapper.map(deal, RecentDealCommentViewModel.class);
            dealViewModel.setAuctionName(deal.getAuction().getProduct().getName());
            this.setDealViewModelProperties(userId, dealViewModel,deal);
            commentsDeals.add(dealViewModel);
        }
        return commentsDeals;
    }

    private void setDealViewModelProperties(String userId, RecentDealCommentViewModel dealViewModel,
                                            DealServiceModel deal) {
        CommentServiceModel sellerComment = deal.getSellerComment();
        this.checkDealViewModelPartnerComment(userId,dealViewModel, deal.getSellerComment());
        this.checkDealViewModelPartnerComment(userId,dealViewModel, deal.getBuyerComment());
    }

    private void checkDealViewModelPartnerComment(String userId, RecentDealCommentViewModel dealViewModel,
                                                  CommentServiceModel partnerComment) {
        if(partnerComment!=null){
            if(!userId.equals(partnerComment.getAuthor().getId())){
                dealViewModel.setContent(partnerComment.getContent());
                dealViewModel.setEstimation(partnerComment.getEstimation().name());
                dealViewModel.setDateTime(this.dateFormatter.format(partnerComment.getDate()));
            }
        }
    }


}
