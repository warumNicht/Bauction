package bauction.services;

import bauction.constants.ErrorMessagesConstants;
import bauction.domain.entities.Deal;
import bauction.domain.models.bindingModels.CommentBindingModel;
import bauction.domain.models.serviceModels.deals.CommentServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.DealNotFoundException;
import bauction.repositories.DealRepository;
import bauction.services.contracts.DealService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DealServiceImpl(DealRepository dealRepository, ModelMapper modelMapper) {
        this.dealRepository = dealRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DealServiceModel findDealById(String id) {
        Deal deal = this.dealRepository.findById(id)
                .orElseThrow(() -> new DealNotFoundException(ErrorMessagesConstants.NOT_EXISTENT_DEAL_MESSAGE));
        return this.modelMapper.map(deal, DealServiceModel.class);
    }

    @Override
    public DealServiceModel registerDeal(DealServiceModel dealServiceModel) {
        Deal deal = this.modelMapper.map(dealServiceModel, Deal.class);
        return this.modelMapper.map(this.dealRepository.saveAndFlush(deal), DealServiceModel.class);
    }

    @Override
    public List<DealServiceModel> allRecentDealsOfUser(String userId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -5);
        Date deadline = cal.getTime();
        List<Deal> deals = this.dealRepository.allRecentDealsOfUser(userId, deadline);

        return deals.stream().map(d -> this.modelMapper.map(d, DealServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DealServiceModel> allDealCommentsOfUser(String userId) {
        List<Deal> deals = this.dealRepository.allDealsWithCommentOfUser(userId);
        return deals.stream()
                .map(d -> this.modelMapper.map(d, DealServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void registerComment(CommentBindingModel model, UserServiceModel loggedInUser) {
        DealServiceModel deal = this.findDealById(model.getDealId());
        CommentServiceModel comment = this.modelMapper.map(model, CommentServiceModel.class);
        comment.setAuthor(loggedInUser);
        comment.setDate(new Date());

        if (model.getPartnerRole().equals("Seller")) {
            deal.setBuyerComment(comment);
        } else {
            deal.setSellerComment(comment);
        }
        this.updateDeal(deal);
    }

    private DealServiceModel updateDeal(DealServiceModel deal) {
        Deal dealMapped = this.modelMapper.map(deal, Deal.class);
        Deal saved = this.dealRepository.save(dealMapped);
        return this.modelMapper.map(saved, DealServiceModel.class);
    }


}
