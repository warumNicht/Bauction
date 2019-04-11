package bauction.services.contracts;

import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import bauction.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public interface ProductService {

    BaseProductServiceModel createProduct(AuctionCreateBindingModel model, BaseCollectionBindingModel collectionBindingModel, HttpSession session) throws IOException;

    BaseProductServiceModel getChangedProduct(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model,
                                              CoinBindingModel coin, BanknoteBindingModel banknote,
                                              File mainImage, File[] files) throws IOException ;

}
