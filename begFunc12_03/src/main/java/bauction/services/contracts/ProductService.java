package bauction.services.contracts;

import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public interface ProductService {

    BaseProductServiceModel createProduct(AuctionCreateBindingModel model, BaseCollectionBindingModel collectionBindingModel) throws IOException;

    BaseProductServiceModel getChangedProduct(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model,
                                              CoinBindingModel coin, BanknoteBindingModel banknote) throws IOException ;

}
