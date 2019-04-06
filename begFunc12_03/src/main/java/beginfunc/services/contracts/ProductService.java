package beginfunc.services.contracts;

import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.AuctionEditBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public interface ProductService {

    BaseProductServiceModel createProduct(AuctionCreateBindingModel model, BaseCollectionBindingModel collectionBindingModel, HttpSession session) throws IOException;

    BaseProductServiceModel getChangedProduct(AuctionServiceModel auctionToEdit, AuctionEditBindingModel model,
                                              CoinBindingModel coin, BanknoteBindingModel banknote,
                                              File mainImage, File[] files) throws IOException ;
}
