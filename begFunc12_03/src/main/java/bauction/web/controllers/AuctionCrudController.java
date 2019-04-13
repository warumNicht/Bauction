package bauction.web.controllers;

import bauction.constants.AppConstants;
import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.CoinBindingModel;

import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.products.BanknoteServiceModel;
import bauction.domain.models.serviceModels.products.CoinServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.services.contracts.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/auctions")
public class AuctionCrudController extends BaseController{
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionCrudController(AuctionService auctionService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createAuction(ModelAndView modelAndView,
                       @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                                      @ModelAttribute(name = "coin") CoinBindingModel coin,
                                      @ModelAttribute(name = "banknote") BanknoteBindingModel banknote) throws IOException, ServletException {

        modelAndView.addObject("auctionCreateModel",model);
        modelAndView.addObject("coin",coin);
        modelAndView.setViewName("auction/auction-create");
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createAuctionPost(ModelAndView modelAndView,
                                   @Valid @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                                   BindingResult bindingResult,
                                          @Valid @ModelAttribute(name = "coin") CoinBindingModel coin,
                                          BindingResult coinBindingResult,
                                          @Valid @ModelAttribute(name = "banknote") BanknoteBindingModel banknote,
                                          BindingResult banknoteBindingResult) throws IOException, ServletException {

        UserServiceModel loggedInUser = super.getLoggedInUserService();
        if(bindingResult.hasErrors()||model.getCategory().equals("Coins")&&coinBindingResult.hasErrors()||
                model.getCategory().equals("Banknotes")&&banknoteBindingResult.hasErrors()){
            modelAndView.addObject("auctionCreateModel",model);
            modelAndView.addObject("coin",coin);
            modelAndView.addObject("banknote",banknote);
            modelAndView.setViewName("auction/auction-create");
            return modelAndView;
        }else {
            if(model.getCategory().equals(AppConstants.CATEGORY_COINS)){
                this.auctionService.createAuction(model,coin,loggedInUser);
            }else if(model.getCategory().equals(AppConstants.CATEGORY_BANKNOTES)){
                this.auctionService.createAuction(model,banknote, loggedInUser);
            }else {
                this.auctionService.createAuction(model, null,loggedInUser);
            }
        }
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteAuction(ModelAndView modelAndView, @PathVariable(value = "id") String id,
                                      @ModelAttribute(name = "auctionCreateModel") AuctionEditBindingModel model,
                                      @ModelAttribute(name = "coin") CoinBindingModel coin,
                                      @ModelAttribute(name = "banknote") BanknoteBindingModel banknote,
                                      HttpServletRequest request, HttpSession session){
        AuctionServiceModel found = this.auctionService.findById(id);
        String referer = request.getHeader("Referer");
        session.setAttribute("urlBeforeDelete", referer);
        this.fillViewWithModels(found,modelAndView);
        modelAndView.addObject("auctionId",id);
        modelAndView.setViewName("auction/auction-delete");
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteAuctionPost(ModelAndView modelAndView, @PathVariable(value = "id") String id, HttpSession session){
        this.auctionService.deleteById(id);
        String urlToRedirect = this.getUrlToRedirect(session);
        modelAndView.setViewName("redirect:/" + urlToRedirect);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editAuction(ModelAndView modelAndView,
                                      @PathVariable(value = "id") String id,
                                      @ModelAttribute(name = "auctionCreateModel") AuctionEditBindingModel model,
                                      @ModelAttribute(name = "coin") CoinBindingModel coin,
                                    @ModelAttribute(name = "banknote") BanknoteBindingModel banknote) throws IOException, ServletException {

        AuctionServiceModel found = this.auctionService.findById(id);
        this.fillViewWithModels(found,modelAndView);

        modelAndView.addObject("auctionId",id);
        modelAndView.setViewName("auction/auction-edit");
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editAuctionPost(@PathVariable(value = "id") String id,
                                        @Valid @ModelAttribute(name = "auctionCreateModel") AuctionEditBindingModel model,
                                        BindingResult bindingResult,
                                        @Valid @ModelAttribute(name = "coin") CoinBindingModel coin, BindingResult coinBindingResult,
                                        @Valid @ModelAttribute(name = "banknote") BanknoteBindingModel banknote,
                                        BindingResult banknoteBindingResult, ModelAndView modelAndView)  throws IOException, ServletException {

        if(bindingResult.hasErrors()||model.getCategory().equals("Coins")&&coinBindingResult.hasErrors()||
                model.getCategory().equals("Banknotes")&&banknoteBindingResult.hasErrors()){
            modelAndView.addObject("auctionCreateModel",model);
            modelAndView.addObject("coin",coin);
            modelAndView.addObject("banknote",banknote);
            modelAndView.addObject("auctionId",id);
            modelAndView.setViewName("auction/auction-edit");
            return modelAndView;
        }
        AuctionServiceModel auctionToEdit = this.auctionService.findById(id);
        this.auctionService.editAuction(auctionToEdit, model, coin, banknote);
        modelAndView.setViewName("redirect:/auctions/edit/" + id);
        return modelAndView;
    }

    private String getUrlToRedirect(HttpSession session) {
        String urlBeforeDelete = (String) session.getAttribute("urlBeforeDelete");
        if (urlBeforeDelete==null){
            return "home";
        }
        int firstSlashIndex = urlBeforeDelete.indexOf("/", 8);
        return urlBeforeDelete.substring(firstSlashIndex+1);
    }

    private void fillViewWithModels(AuctionServiceModel found, ModelAndView modelAndView) {
        AuctionEditBindingModel model=this.modelMapper.map(found, AuctionEditBindingModel.class);
        model.setName(found.getProduct().getName());
        model.setCategory(found.getCategory().getName());
        model.setDescription(found.getProduct().getDescription());
        model.setTown(found.getProduct().getTown().getName());
        modelAndView.addObject("auctionCreateModel",model);

        if (found.getProduct() instanceof CoinServiceModel){
            CoinBindingModel coin=this.modelMapper.map(found.getProduct(),CoinBindingModel.class);
            modelAndView.addObject("coin",coin);
        }else if(found.getProduct() instanceof BanknoteServiceModel){
            BanknoteBindingModel banknote =this.modelMapper.map(found.getProduct(),BanknoteBindingModel.class);
            modelAndView.addObject("banknote",banknote);
            CoinBindingModel coin=this.modelMapper.map(found.getProduct(),CoinBindingModel.class);
            modelAndView.addObject("coin",coin);
        }
    }

}
