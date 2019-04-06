package beginfunc.web.controllers;

import beginfunc.constants.StaticImagesConstants;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.viewModels.PictureEditViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/images")
public class PictureController {
    private final PictureService pictureService;
    private final AuctionService auctionService;

    @Autowired
    public PictureController(PictureService pictureService, AuctionService auctionService) {
        this.pictureService = pictureService;
        this.auctionService = auctionService;
    }

    @ResponseBody
    @GetMapping(value = "/fetch/product/{id}", produces = "application/json")
    public Object getAllImagesOfAuction(@PathVariable(name = "id") String  id){
        List<String> urls = this.pictureService.findAllByProductId(id).stream()
                .map(image -> image.getPath())
                .collect(Collectors.toList());
        return urls;
    }

    @ResponseBody
    @GetMapping(value = "/fetch/auction/{id}", produces = "application/json")
    public Object getImagesOfAuctionWithoutTheMain(@PathVariable(name = "id") String  id){
        AuctionServiceModel found = this.auctionService.findById(id);
        String productId = found.getProduct().getId();
        List<PictureEditViewModel> editViewModels = this.pictureService.findAllByWithoutMain(productId).stream()
                .map(image -> new PictureEditViewModel(image.getId(), image.getPath()))
                .collect(Collectors.toList());
        return editViewModels;
    }

    @ResponseBody
    @GetMapping(value = "/fetch/main/{id}", produces = "application/json")
    public Object getAuctionMainPicture(@PathVariable(name = "id") String  id){
        AuctionServiceModel found = this.auctionService.findById(id);
        String mainImageUrl=null;
        if(found.getProduct().getMainPicture()!=null){
            mainImageUrl =  found.getProduct().getMainPicture().getPath();
        }else {
            mainImageUrl = StaticImagesConstants.DEFAULT_AUCTION_MAIN_IMAGE;
        }
        String[] res=new String[1];
        res[0]=mainImageUrl;
        return res;
    }

    @GetMapping("/delete/{imageId}/{auctionId}")
    public ModelAndView deleteImage(@PathVariable(name = "imageId") String  imageId,
                                    @PathVariable(name = "auctionId") String  auctionId, ModelAndView modelAndView){

        this.pictureService.deleteImage(imageId);
        modelAndView.setViewName("redirect:/auctions/edit/"+ auctionId);
        return modelAndView;
    }
}
