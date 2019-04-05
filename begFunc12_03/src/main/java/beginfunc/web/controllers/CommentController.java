package beginfunc.web.controllers;

import beginfunc.domain.models.bindingModels.CommentBindingModel;
import beginfunc.domain.models.serviceModels.deals.CommentServiceModel;
import beginfunc.domain.models.serviceModels.deals.DealServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.services.contracts.DealService;
import beginfunc.services.contracts.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/comments")
public class CommentController extends BaseController {
    private final DealService dealService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(DealService dealService, ModelMapper modelMapper) {
        this.dealService = dealService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/write/{id}")
    public ModelAndView writeComment(@PathVariable(name = "id") String dealId, @ModelAttribute(name = "model") CommentBindingModel model,
                                     @Param(value = "partnerRole") String partnerRole, ModelAndView modelAndView) {
        DealServiceModel deal = this.dealService.findDealById(dealId);
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        String formedDate = format.format(deal.getDateTime());
        CommentBindingModel bindingModel=new CommentBindingModel(""+dealId,formedDate,
                deal.getAuction().getProduct().getName(),partnerRole );

        modelAndView.addObject("model",bindingModel);
        modelAndView.setViewName("comments/write-comment");
        return modelAndView;
    }

    @PostMapping("/write/{id}")
    public ModelAndView writeCommentPost(@PathVariable(name = "id") String dealId,
                                         @Valid @ModelAttribute(name = "model") CommentBindingModel model,
                                         BindingResult bindingResult,
                                         @Param(value = "partnerRole") String partnerRole,
                                         @Param(value = "auctionName") String auctionName,
                                         @Param(value = "dealMoment") String dealMoment,
                                         ModelAndView modelAndView) {
        if(bindingResult.hasErrors()){
            model.setDealId(""+dealId);
            model.setAuctionName(auctionName);
            model.setDealMoment(dealMoment);
            model.setPartnerRole(partnerRole);
            modelAndView.addObject("model", model);
            modelAndView.setViewName("comments/write-comment");
            return modelAndView;
        }
        DealServiceModel deal = this.dealService.findDealById(dealId);
        UserServiceModel loggedInUser = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);

        CommentServiceModel comment = this.modelMapper.map(model, CommentServiceModel.class);
        comment.setAuthor(loggedInUser);
        comment.setDate(new Date());

        if(partnerRole.equals("Seller")){
            deal.setBuyerComment(comment);
        }else {
            deal.setSellerComment(comment);
        }
        this.dealService.updateDeal(deal);
        modelAndView.setViewName("redirect:/users/profile/"+super.getLoggedInUserId());
        return modelAndView;
    }
}
