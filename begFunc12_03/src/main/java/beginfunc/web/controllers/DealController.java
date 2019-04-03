package beginfunc.web.controllers;

import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.deals.CommentServiceModel;
import beginfunc.domain.models.serviceModels.deals.DealServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.domain.models.viewModels.deals.RecentDealCommentViewModel;
import beginfunc.domain.models.viewModels.deals.RecentDealViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.DealService;
import beginfunc.services.contracts.UserService;
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
    private final ModelMapper modelMapper;

    @Autowired
    public DealController(DealService dealService, AuctionService auctionService,
                          ModelMapper modelMapper) {
        this.dealService = dealService;
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ModelAndView registerDeal(@RequestParam(name = "auctionId") Integer auctionId,
                                     ModelAndView modelAndView) {
        AuctionServiceModel auction = this.auctionService.findById(auctionId);
        UserServiceModel buyer = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);
        DealServiceModel deal = new DealServiceModel(new Date(), auction.getReachedPrice(),
                auction.getSeller(), buyer, auction);
        this.dealService.registerDeal(deal);
        auction.setStatus(AuctionStatus.Finished);
        auction.setBuyer(buyer);
        this.auctionService.updateAuction(auction);
        modelAndView.setViewName("redirect:/users/profile/" + super.getLoggedInUserId());
        return modelAndView;
    }

    @GetMapping(value = "/fetch/{id}", produces = "application/json")
    @ResponseBody
    public Object fetchUsersDealsToInteract(@PathVariable(name = "id") Integer userId) {
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
    public Object fetchUsersDealComments(@PathVariable(name = "id") Integer userId) {
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

    private void setDealViewModelProperties(Integer userId, RecentDealCommentViewModel dealViewModel,
                                            DealServiceModel deal) {
        CommentServiceModel sellerComment = deal.getSellerComment();
        this.checkDealViewModelPartnerComment(userId,dealViewModel, deal.getSellerComment());
        this.checkDealViewModelPartnerComment(userId,dealViewModel, deal.getBuyerComment());
    }

    private void checkDealViewModelPartnerComment(Integer userId, RecentDealCommentViewModel dealViewModel,
                                                  CommentServiceModel partnerComment) {
        if(partnerComment!=null){
            if(userId!=partnerComment.getAuthor().getId()){
                dealViewModel.setContent(partnerComment.getContent());
                dealViewModel.setEstimation(partnerComment.getEstimation().name());
                dealViewModel.setDateTime(this.dateFormatter.format(partnerComment.getDate()));
            }
        }
    }


}
