package bauction.services;

import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.productRelated.Picture;
import bauction.domain.entities.productRelated.products.BaseProduct;
import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.PictureServiceModel;
import bauction.domain.models.serviceModels.products.BanknoteServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.products.CoinServiceModel;
import bauction.repositories.BaseProductRepository;
import bauction.services.contracts.CloudinaryService;
import bauction.services.contracts.PictureService;
import bauction.services.contracts.ProductService;
import bauction.services.contracts.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final BaseProductRepository baseProductRepository;
    private final TownService townService;
    private final PictureService pictureService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(BaseProductRepository baseProductRepository,
                              TownService townService, PictureService pictureService,
                              CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.baseProductRepository = baseProductRepository;
        this.townService = townService;
        this.pictureService = pictureService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public BaseProductServiceModel createProduct(AuctionCreateBindingModel model,
                                                 BaseCollectionBindingModel collectionBindingModel) throws IOException {
        BaseProductServiceModel product;
        if(collectionBindingModel==null){
            product=new BaseProductServiceModel();
        }else if(collectionBindingModel instanceof CoinBindingModel){
            product=this.setCoinProperties((CoinBindingModel)collectionBindingModel);
        }else {
            product=this.setBanknoteProperties((BanknoteBindingModel)collectionBindingModel);
        }
        this.setBaseProductProperties(product,model);
        return product;
    }

    @Override
    public BaseProductServiceModel getChangedProduct(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model,
                                                     CoinBindingModel coin, BanknoteBindingModel banknote) throws IOException {
        BaseProductServiceModel product;
        if(model.getCategory().equals("Coins")){
            product = this.modelMapper.map(coin, CoinServiceModel.class);
        }else if(model.getCategory().equals("Banknotes")){
            product = this.modelMapper.map(banknote, BanknoteServiceModel.class);
        }else {
            product= auctionToEdit.getProduct();
        }
        product.setTown(this.townService.findByNameOrElseCreateByName(model.getTown()));
        product.setMainPicture(auctionToEdit.getProduct().getMainPicture());
        product.setPictures(new ArrayList<>());
        product.getPictures().addAll(auctionToEdit.getProduct().getPictures());
        product.setName(model.getName());
        product.setDescription(model.getDescription());

        if(model.getMainImage()!=null&&!model.getMainImage().getOriginalFilename().equals("")){
            this.changeProductMainPicture(product, auctionToEdit, model.getMainImage());
        }
        if(model.getImages()!=null&&!model.getImages()[0].getOriginalFilename().equals("")){
            Auction auction = this.modelMapper.map(auctionToEdit, Auction.class);
            BaseProduct currentProduct = auction.getProduct();
            List<PictureServiceModel> productPictures = this.createProductPictures(model.getImages());
            for (PictureServiceModel picture : productPictures) {
                Picture picture1 = this.modelMapper.map(picture, Picture.class);
                picture1.setProduct(currentProduct);
                this.pictureService.savePicture(this.modelMapper.map(picture1,PictureServiceModel.class));
            }
        }
        List<PictureServiceModel> allByProductId = this.pictureService.findAllByProductId(auctionToEdit.getProduct().getId());
        product.setPictures(allByProductId);
        return product;
    }

    private void changeProductMainPicture(BaseProductServiceModel product, AuctionServiceModel auctionToEdit,
                                          MultipartFile mainImage) throws IOException {
        Auction auction = this.modelMapper.map(auctionToEdit, Auction.class);
        BaseProduct currentProduct = auction.getProduct();
        String pictureToDelId=null;
        if(currentProduct.getMainPicture()!=null){
            pictureToDelId=currentProduct.getMainPicture().getId();
        }
        if(pictureToDelId!=null){
            currentProduct.getMainPicture().setProduct(null);
            currentProduct.setMainPicture(null);
            String del=pictureToDelId;
            List<Picture> remainingPictures = currentProduct.getPictures().stream()
                    .filter(p -> !p.getId().equals(del))
                    .collect(Collectors.toList());

            currentProduct.setPictures(remainingPictures);
            this.baseProductRepository.save(currentProduct);
        }
        PictureServiceModel productMainImage = this.createPicture(mainImage);
        product.setMainPicture(productMainImage);
        productMainImage.setProduct(product);
        String productDeletedMainId=pictureToDelId;
        List<PictureServiceModel> remainingImages = product.getPictures().stream()
                .filter(p -> !p.getId().equals(productDeletedMainId)).collect(Collectors.toList());
        product.setPictures(remainingImages);
    }

    private BanknoteServiceModel setBanknoteProperties(BanknoteBindingModel banknoteBindingModel) {
        return  this.modelMapper.map(banknoteBindingModel,BanknoteServiceModel.class);
    }

    private CoinServiceModel setCoinProperties(CoinBindingModel coinBindingModel) {
        return this.modelMapper.map(coinBindingModel, CoinServiceModel.class);
    }

    private void setBaseProductProperties(BaseProductServiceModel product,
                                          AuctionCreateBindingModel model) throws IOException {
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        String townName = model.getTown().replaceAll("\\s+", " ").trim();
        product.setTown(this.townService.findByNameOrElseCreateByName(townName));


        if(model.getMainImage()!=null&&!model.getMainImage().getOriginalFilename().equals("")){
            PictureServiceModel productMainImage = this.createPicture(model.getMainImage());
            product.setMainPicture(productMainImage);
            productMainImage.setProduct(product);
        }

        if(model.getImages()!=null&&!model.getImages()[0].getOriginalFilename().equals("")){
            List<PictureServiceModel> productImages = this.createProductPictures(model.getImages());
            product.setPictures(productImages);
            for (PictureServiceModel productImage : productImages) {
                productImage.setProduct(product);
            }
        }
    }

    private List<PictureServiceModel> createProductPictures(MultipartFile[] productImages) throws IOException {
        List<PictureServiceModel> images=new ArrayList<>();
        for (MultipartFile file : productImages) {
            PictureServiceModel pictureServiceModel = new PictureServiceModel(this.cloudinaryService.uploadImage(file));
            images.add(pictureServiceModel);
        }
        return images;
    }

    private PictureServiceModel createPicture(MultipartFile mainImage) throws IOException {
        String imageUrl = this.cloudinaryService.uploadImage(mainImage);
        return new PictureServiceModel(imageUrl);
    }


}
