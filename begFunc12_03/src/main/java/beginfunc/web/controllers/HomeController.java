package beginfunc.web.controllers;

import beginfunc.constants.AppConstants;
import beginfunc.constants.StaticImagesConstants;
import beginfunc.domain.models.viewModels.home.AuctionHomeViewModel;
import beginfunc.services.contracts.AuctionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(AuctionService auctionService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView getIndex(ModelAndView modelAndView){
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHome(ModelAndView modelAndView){

        List<AuctionHomeViewModel> homeAuctions = this.auctionService.findAllActivesAuctions().stream()
                .map(a -> {
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
                    homeViewModel.setCurrentPrice(String.format("%.2f ",
                            a.getReachedPrice()!=null ? a.getReachedPrice() : BigDecimal.ZERO) +'\u20ac');
                    return homeViewModel;
                }).collect(Collectors.toList());
        modelAndView.addObject("auctions",homeAuctions);
        modelAndView.setViewName("home");
        return modelAndView;
    }


}
