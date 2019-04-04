package beginfunc.services.contracts;

import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface ProductService {

    BaseProductServiceModel createProduct(AuctionCreateBindingModel model, BaseCollectionBindingModel collectionBindingModel, HttpSession session) throws IOException;
}
