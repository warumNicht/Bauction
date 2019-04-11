package bauction.services.contracts;

import bauction.domain.models.bindingModels.CommentBindingModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;

import java.util.List;

public interface DealService {

    DealServiceModel findDealById(String id);

    DealServiceModel registerDeal(DealServiceModel dealServiceModel);

    List<DealServiceModel> allRecentDealsOfUser(String userId);

    List<DealServiceModel> allDealCommentsOfUser(String userId);

    void registerComment(CommentBindingModel model, UserServiceModel loggedInUser);
}
