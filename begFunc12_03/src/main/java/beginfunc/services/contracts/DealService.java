package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.deals.DealServiceModel;

import java.util.List;

public interface DealService {

    DealServiceModel findDealById(Integer id);

    DealServiceModel registerDeal(DealServiceModel dealServiceModel);

    List<DealServiceModel> allRecentDealsOfUser(Integer userId);

    DealServiceModel updateDeal(DealServiceModel deal);

    List<DealServiceModel> allDealCommentsOfUser(Integer userId);
}
