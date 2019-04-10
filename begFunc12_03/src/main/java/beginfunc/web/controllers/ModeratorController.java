package beginfunc.web.controllers;

import beginfunc.constants.StaticImagesConstants;
import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.viewModels.auctions.AuctionModeratorViewModel;
import beginfunc.services.contracts.AuctionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/moderator/auctions")
public class ModeratorController {
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    @Autowired
    public ModeratorController(AuctionService auctionService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/actives")
    public ModelAndView viewAllActivesAuctions(ModelAndView modelAndView, HttpServletRequest request){
        List<AuctionModeratorViewModel> homeAuctions =this.loadActiveViewModels();
        modelAndView.addObject("auctions",homeAuctions);
        modelAndView.setViewName("moderator/all-actives-auctions");
        return modelAndView;
    }

    @GetMapping("/finished")
    public ModelAndView viewAllFinishedAuctions(ModelAndView modelAndView, HttpServletRequest request){
        List<AuctionModeratorViewModel> homeAuctions =this.loadFinishedViewModels();
        modelAndView.addObject("auctions",homeAuctions);
        modelAndView.setViewName("moderator/all-finished-auctions");
        return modelAndView;
    }

    private List<AuctionModeratorViewModel> loadActiveViewModels() {
        List<AuctionServiceModel> auctions = this.auctionService.findAllAuctionsByStatus(AuctionStatus.Active);
        return this.mapToViewModels(auctions);
    }

    private List<AuctionModeratorViewModel> loadFinishedViewModels() {
        List<AuctionServiceModel> auctions = this.auctionService.findAllFinishedAuctionsWithoutDeal();
        return this.mapToViewModels(auctions);
    }

    private List<AuctionModeratorViewModel> mapToViewModels(List<AuctionServiceModel> auctions) {
        return auctions.stream()
                .map(a -> {
                    AuctionModeratorViewModel homeViewModel = this.modelMapper.map(a,AuctionModeratorViewModel.class);
                    String name = a.getProduct().getName();
                    if(name.length()>=19){
                        name=name.substring(0,18) + "...";
                    }
                    homeViewModel.setCategory(a.getCategory().getName());
                    homeViewModel.setName(name);
                    if(a.getProduct().getMainPicture()==null){
                        homeViewModel.setMainImageUrl(StaticImagesConstants.DEFAULT_AUCTION_MAIN_IMAGE);
                    }else {
                        homeViewModel.setMainImageUrl(a.getProduct().getMainPicture().getPath());
                    }
                    homeViewModel.setCurrentPrice(String.format("%.2f ",
                            a.getReachedPrice()!=null ? a.getReachedPrice() : BigDecimal.ZERO) +'\u20ac');
                    return homeViewModel;
                }).collect(Collectors.toList());
    }
}
