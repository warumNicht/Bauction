package beginfunc.services;

import beginfunc.domain.entities.Comment;
import beginfunc.domain.entities.Deal;
import beginfunc.domain.models.serviceModels.deals.CommentServiceModel;
import beginfunc.domain.models.serviceModels.deals.DealServiceModel;
import beginfunc.repositories.DealRepository;
import beginfunc.services.contracts.DealService;
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
    public DealServiceModel findDealById(Integer id) {
        Deal deal = this.dealRepository.findById(id).orElse(null);
        if(deal!=null){
            return this.modelMapper.map(deal,DealServiceModel.class);
        }
        return null;
    }

    @Override
    public DealServiceModel registerDeal(DealServiceModel dealServiceModel) {
        Deal deal = this.modelMapper.map(dealServiceModel, Deal.class);
        return this.modelMapper.map(this.dealRepository.saveAndFlush(deal),DealServiceModel.class);
    }

    @Override
    public List<DealServiceModel> allRecentDealsOfUser(Integer userId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -5);
        Date deadline = cal.getTime();
        List<Deal> deals = this.dealRepository.allRecentDealsOfUser(userId, deadline);

        return deals.stream().map(d-> this.modelMapper.map(d,DealServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public DealServiceModel updateDeal(DealServiceModel deal) {
        Deal dealMapped = this.modelMapper.map(deal, Deal.class);
        Deal saved = this.dealRepository.save(dealMapped);
        return this.modelMapper.map(saved,DealServiceModel.class);
    }

    @Override
    public List<DealServiceModel> allDealCommentsOfUser(Integer userId) {
        List<Deal> deals = this.dealRepository.allDealsWithCommentOfUser(userId);
        return deals.stream()
                .map(d->this.modelMapper.map(d,DealServiceModel.class))
                .collect(Collectors.toList());
    }


}
