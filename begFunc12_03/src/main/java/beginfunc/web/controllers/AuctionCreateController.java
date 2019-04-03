package beginfunc.web.controllers;


import beginfunc.constants.AppConstants;
import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.entities.enums.AuctionType;
import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.BaseProductServiceModel;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.services.contracts.*;
import beginfunc.util.ImageStorageUtil;
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
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class AuctionCreateController extends BaseController{
    private final AuctionService auctionService;
    private final CategoryService categoryService;
    private final TownService townService;
    private final PictureService pictureService;
    private final ModelMapper modelMapper;
    private final ImageStorageUtil imageStorageUtil;

    @Autowired
    public AuctionCreateController(AuctionService auctionService, CategoryService categoryService,
                                  TownService townService, PictureService pictureService,
                                   ModelMapper modelMapper, ImageStorageUtil imageStorageUtil) {
        this.auctionService = auctionService;
        this.categoryService = categoryService;
        this.townService = townService;
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
        this.imageStorageUtil = imageStorageUtil;
    }

    @GetMapping("/auctions/create")
    public ModelAndView createAuction(ModelAndView modelAndView,
                       @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                               HttpSession session) throws IOException, ServletException {

        session.setAttribute("productImages", null);
        session.setAttribute("productMainImage", null);

        modelAndView.addObject("auctionCreateModel",model);
        modelAndView.addObject("categories",this.categoryService.findAllCategories());
        modelAndView.setViewName("auction-create");

        return modelAndView;
    }

    @PostMapping("/auctions/create")
    public ModelAndView createAuctionPost(ModelAndView modelAndView,
                                   @Valid @ModelAttribute(name = "auctionCreateModel") AuctionCreateBindingModel model,
                                   BindingResult bindingResult,
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
        if(bindingResult.hasErrors()){
            modelAndView.addObject("auctionCreateModel",model);
            modelAndView.addObject("categories",this.categoryService.findAllCategories());
            modelAndView.setViewName("auction-create");
            return modelAndView;
        }


        AuctionServiceModel savedAuction = this.createAuction(model, session);
        if(savedAuction==null){
            this.imageStorageUtil.deleteTempFolder(model.getCategory());
        }else {
            this.renamePictures(savedAuction.getProduct().getId(),
                    ""+ savedAuction.getId());
            this.imageStorageUtil.renameAuctionImagesTempFolder(
                    savedAuction.getCategory().getName(),""+savedAuction.getId());
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

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void renamePictures(Integer productId,String auctionId) {
        List<PictureServiceModel> pictures = this.pictureService.findAllByProductId(productId);
        for (PictureServiceModel picture : pictures) {
            String replaced = picture.getPath().replace(AppConstants.AUCTION_PICTURES_TEMP_FOLDER_NAME, auctionId);
            picture.setPath(replaced);
            this.pictureService.renamePicture(picture);
        }
    }

    private AuctionServiceModel createAuction(AuctionCreateBindingModel model, HttpSession session) {

        UserServiceModel loggedUser = this.modelMapper.map(super.getLoggedInUser(),UserServiceModel.class);
        if(loggedUser==null){
            throw new IllegalArgumentException("User does not exists!");
        }
        BaseProductServiceModel product=this.createProduct(model,session);
        AuctionServiceModel auctionToSave = this.modelMapper.map(model, AuctionServiceModel.class);
        auctionToSave.setSeller(loggedUser);
        auctionToSave.setProduct(product);
        auctionToSave.setCategory(this.categoryService.findByName(model.getCategory()));

        this.setAuctionsPrices(auctionToSave, model);

        if(model.isStartLater()){
            auctionToSave.setStatus(AuctionStatus.Waiting);
        }else {
            auctionToSave.setStatus(AuctionStatus.Active);
            auctionToSave.setStartDate(new Date());
        }
        AuctionServiceModel savedAuction = this.auctionService.createAuction(auctionToSave);
        return savedAuction;
    }

    private void setAuctionsPrices(AuctionServiceModel auctionToSave, AuctionCreateBindingModel model) {
        BigDecimal wantedPrice = model.getWantedPrice();
        if(wantedPrice!=null){
            auctionToSave.setWantedPrice(wantedPrice);
            if(!auctionToSave.getType().equals(AuctionType.Preserved_Price)){
                auctionToSave.setReachedPrice(wantedPrice);
            }
        }
    }


    private BaseProductServiceModel createProduct(AuctionCreateBindingModel model, HttpSession session) {
        BaseProductServiceModel product=new BaseProductServiceModel();
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        String townName = model.getTown().replaceAll("\\s+", " ").trim();
        product.setTown(this.townService.findByNameOrElseCreateByName(townName));

        this.imageStorageUtil.setAuctionImagesTempFolderPath(model.getCategory());
        this.imageStorageUtil.createAuctionImagesTempFolder();

        File mainImage = (File) session.getAttribute("productMainImage");
        if(mainImage!=null){
            PictureServiceModel productMainImage = this.imageStorageUtil.createProductMainPicture(mainImage);
            product.setMainPicture(productMainImage);
            productMainImage.setProduct(product);
        }
        File[] productListImages = (File[]) session.getAttribute("productImages");
        if(productListImages!=null){
            List<PictureServiceModel> productImages = this.imageStorageUtil.createProductPictures(productListImages);
            product.setPictures(productImages);
            for (PictureServiceModel productImage : productImages) {
                productImage.setProduct(product);
            }
        }
        return product;
    }
}
