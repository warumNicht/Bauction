package bauction.web.controllers;

import bauction.domain.models.bindingModels.CommentBindingModel;
import bauction.domain.models.serviceModels.deals.CommentServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.DealNotFoundException;
import bauction.error.UserNotFoundException;
import bauction.services.contracts.DealService;
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

    @PostMapping("/write")
    public ModelAndView writeCommentPost(@Valid @ModelAttribute(name = "model") CommentBindingModel model,
                                         BindingResult bindingResult,
                                         @Param(value = "dealId") String dealId,
                                         @Param(value = "partnerRole") String partnerRole,
                                         @Param(value = "auctionName") String auctionName,
                                         @Param(value = "dealMoment") String dealMoment,
                                         ModelAndView modelAndView) {
        if(bindingResult.hasErrors()){
            model.setDealId(dealId);
            model.setAuctionName(auctionName);
            model.setDealMoment(dealMoment);
            model.setPartnerRole(partnerRole);
            modelAndView.addObject("model", model);
            modelAndView.setViewName("comments/write-comment");
            return modelAndView;
        }
        UserServiceModel loggedInUser = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);
        this.dealService.registerComment(model,loggedInUser);

        modelAndView.setViewName("redirect:/users/profile/"+super.getLoggedInUserId());
        return modelAndView;
    }

    @ExceptionHandler({DealNotFoundException.class})
    public ModelAndView handleUserNotFound( UserNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }
}
