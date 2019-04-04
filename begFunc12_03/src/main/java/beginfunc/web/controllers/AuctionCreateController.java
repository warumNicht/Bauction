package beginfunc.web.controllers;

import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;

import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.services.contracts.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class AuctionCreateController extends BaseController{
    private final AuctionService auctionService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionCreateController(AuctionService auctionService, CategoryService categoryService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/auctions/create")
    public ModelAndView createAuction(ModelAndView modelAndView,
                       @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                                      @ModelAttribute(name = "coin") CoinBindingModel coin,
                                      @ModelAttribute(name = "banknote") BanknoteBindingModel banknote,
                               HttpSession session) throws IOException, ServletException {

        session.setAttribute("productImages", null);
        session.setAttribute("productMainImage", null);

        modelAndView.addObject("auctionCreateModel",model);
        modelAndView.addObject("coin",coin);
        modelAndView.addObject("categories",this.categoryService.findAllCategories());
        modelAndView.setViewName("auction-create");
        return modelAndView;
    }

    @PostMapping("/auctions/create")
    public ModelAndView createAuctionPost(ModelAndView modelAndView,
                                   @Valid @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                                   BindingResult bindingResult,
                                          @Valid @ModelAttribute(name = "coin") CoinBindingModel coin,
                                          BindingResult coinBindingResult,
                                          @Valid @ModelAttribute(name = "banknote") BanknoteBindingModel banknote,
                                          BindingResult banknoteBindingResult,
                                   @RequestParam("mainImage") MultipartFile main,
                                   @RequestParam("files") MultipartFile[] files,
                                   HttpSession session) throws IOException, ServletException {
        if(!files[0].isEmpty()){
            session.setAttribute("productImages", this.convertMultipartArray(files));
        }
        if(!main.isEmpty()){
            String originalFilename = main.getOriginalFilename();
            session.setAttribute("productMainImage", this.convert(main));
        }
        UserServiceModel loggedInUser = this.modelMapper.map(super.getLoggedInUser(), UserServiceModel.class);

        if(bindingResult.hasErrors()||model.getCategory().equals("Coins")&&coinBindingResult.hasErrors()||
                model.getCategory().equals("Banknotes")&&banknoteBindingResult.hasErrors()){
            modelAndView.addObject("auctionCreateModel",model);
            modelAndView.addObject("coin",coin);
            modelAndView.addObject("banknote",banknote);
            modelAndView.addObject("categories",this.categoryService.findAllCategories());
            modelAndView.setViewName("auction-create");
            return modelAndView;
        }else {
            if(model.getCategory().equals("Coins")){
                this.auctionService.createAuction(model,coin, session,loggedInUser);
            }else if(model.getCategory().equals("Banknotes")){
                this.auctionService.createAuction(model,banknote, session,loggedInUser);
            }else {
                this.auctionService.createAuction(model, null, session,loggedInUser);
            }
        }
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }




    private File[] convertMultipartArray(MultipartFile[] files) throws IOException {
        File[] res=new File[files.length];
        for (int i = 0; i < files.length; i++) {
            res[i]=this.convert(files[i]);
        }
        return res;
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }








}
