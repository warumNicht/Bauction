package beginfunc.services;

import beginfunc.domain.entities.auctionRelated.Auction;
import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.repositories.AuctionRepository;
import beginfunc.services.contracts.AuctionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, ModelMapper modelMapper) {
        this.auctionRepository = auctionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuctionServiceModel createAuction(AuctionServiceModel model) {
        Auction auctionToAdd = this.modelMapper.map(model, Auction.class);
        auctionToAdd.setStartDate(new Date());
        auctionToAdd.setEndDate(this.getDateAfter7Days());
        this.correctModelMappersBug(auctionToAdd);

        Auction createdAuction = this.auctionRepository.saveAndFlush(auctionToAdd);
        return this.modelMapper.map(createdAuction, AuctionServiceModel.class);
    }

    @Override
    public List<AuctionServiceModel> findAllActivesAuctions() {
        List<AuctionServiceModel> allActives = this.auctionRepository
                .findAllByStatusIsLikeOrStatusIsLike(AuctionStatus.Active,AuctionStatus.Finished)
                .stream()
                .map(a -> this.modelMapper.map(a, AuctionServiceModel.class))
                .collect(Collectors.toList());
        return allActives;
    }

    @Override
    public AuctionServiceModel findById(Integer id) {
        Auction found = this.auctionRepository.findById(id).orElse(null);
        if(found!=null){
            AuctionServiceModel auctionServiceModel = this.modelMapper.map(found, AuctionServiceModel.class);
            return auctionServiceModel;
        }
        return null;
    }

    @Override
    public boolean increaseAuctionViews(Integer id) {
        try {
            this.auctionRepository.increaseViews(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void increaseCurrentPrice(Integer id, BigDecimal biddingStep) {
        this.auctionRepository.increaseCurrentPrice(id,biddingStep);
    }

    @Override
    public void updateAuction(AuctionServiceModel model) {
        Auction auction = this.modelMapper.map(model, Auction.class);
        this.auctionRepository.save(auction);
    }

    @Override
    public void updateAuctionStatus(Integer id, AuctionStatus status) {
        this.auctionRepository.updateStatus(id,status);
    }

    private void correctModelMappersBug(Auction auctionToAdd) {
        BaseProduct product = auctionToAdd.getProduct();
        if (product.getMainPicture()!=null){
            product.getMainPicture().setProduct(product);
        }
        if(product.getPictures()!=null){
            product.getPictures().stream()
                    .forEach(p->p.setProduct(product));
        }
    }

    private Date getDateAfter7Days() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 7);
        return cal.getTime();
    }
}
