package beginfunc.services;

import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.domain.models.serviceModels.products.BanknoteServiceModel;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;
import beginfunc.domain.models.serviceModels.products.CoinServiceModel;
import beginfunc.services.contracts.CategoryService;
import beginfunc.services.contracts.CloudinaryService;
import beginfunc.services.contracts.ProductService;
import beginfunc.services.contracts.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final TownService townService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(TownService townService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.townService = townService;
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
