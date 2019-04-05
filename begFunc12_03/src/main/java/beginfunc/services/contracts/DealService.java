package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.deals.DealServiceModel;

import java.util.List;

public interface DealService {

    DealServiceModel findDealById(String id);

    DealServiceModel registerDeal(DealServiceModel dealServiceModel);

    List<DealServiceModel> allRecentDealsOfUser(String userId);

    DealServiceModel updateDeal(DealServiceModel deal);

    List<DealServiceModel> allDealCommentsOfUser(String userId);
}
