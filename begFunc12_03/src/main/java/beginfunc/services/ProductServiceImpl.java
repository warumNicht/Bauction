package beginfunc.services;

import beginfunc.domain.entities.auctionRelated.Auction;
import beginfunc.domain.entities.productRelated.Picture;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.AuctionEditBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.domain.models.serviceModels.products.BanknoteServiceModel;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;
import beginfunc.domain.models.serviceModels.products.CoinServiceModel;
import beginfunc.repositories.BaseProductRepository;
import beginfunc.services.contracts.CloudinaryService;
import beginfunc.services.contracts.PictureService;
import beginfunc.services.contracts.ProductService;
import beginfunc.services.contracts.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.io.File;
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
                                                 BaseCollectionBindingModel collectionBindingModel,
                                                 HttpSession session) throws IOException {
        BaseProductServiceModel product;
        if(collectionBindingModel==null){
            product=new BaseProductServiceModel();
        }else if(collectionBindingModel instanceof CoinBindingModel){
            product=this.setCoinProperties((CoinBindingModel)collectionBindingModel);
        }else {
            product=this.setBanknoteProperties((BanknoteBindingModel)collectionBindingModel);
        }
        this.setBaseProductProperties(product,model, session);
        return product;
    }

    @Override
    public BaseProductServiceModel getChangedProduct(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model, CoinBindingModel coin, BanknoteBindingModel banknote,
                                                     File mainImage, File[] files) throws IOException {
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

        if(mainImage!=null){
            this.changeProductMainPicture(product, auctionToEdit, mainImage);
        }
        if(files!=null){
            Auction auction = this.modelMapper.map(auctionToEdit, Auction.class);
            BaseProduct currentProduct = auction.getProduct();
            List<PictureServiceModel> productPictures = this.createProductPictures(files);
            for (PictureServiceModel picture : productPictures) {
                Picture picture1 = this.modelMapper.map(picture, Picture.class);
                picture1.setProduct(currentProduct);
                this.pictureService.savePicture(this.modelMapper.map(picture1,PictureServiceModel.class));
            }
        }
        return product;
    }

    private void changeProductMainPicture(BaseProductServiceModel product, AuctionServiceModel auctionToEdit,
                                          File mainImage) throws IOException {
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
//            this.pictureService.deleteImage(pictureToDelId);
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
                                          AuctionCreateBindingModel model,HttpSession session) throws IOException {
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        String townName = model.getTown().replaceAll("\\s+", " ").trim();
        product.setTown(this.townService.findByNameOrElseCreateByName(townName));

        File mainImage = (File) session.getAttribute("productMainImage");
        if(mainImage!=null){
            PictureServiceModel productMainImage = this.createPicture(mainImage);
            product.setMainPicture(productMainImage);
            productMainImage.setProduct(product);
        }
        File[] productImagesFiles = (File[]) session.getAttribute("productImages");
        if(productImagesFiles!=null){
            List<PictureServiceModel> productImages = this.createProductPictures(productImagesFiles);
            product.setPictures(productImages);
            for (PictureServiceModel productImage : productImages) {
                productImage.setProduct(product);
            }
        }
    }

    private List<PictureServiceModel> createProductPictures(File[] productImages) throws IOException {
        List<PictureServiceModel> images=new ArrayList<>();
        for (File file : productImages) {
            PictureServiceModel pictureServiceModel = new PictureServiceModel(this.cloudinaryService.uploadImage(file));
            images.add(pictureServiceModel);
        }
        return images;
    }

    private PictureServiceModel createPicture(File mainImage) throws IOException {
        String imageUrl = this.cloudinaryService.uploadImage(mainImage);
        return new PictureServiceModel(imageUrl);
    }
}
