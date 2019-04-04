package beginfunc.services;

import beginfunc.domain.entities.auctionRelated.Auction;
import beginfunc.domain.entities.enums.AuctionStatus;
import beginfunc.domain.entities.enums.AuctionType;
import beginfunc.domain.entities.productRelated.products.BankNote;
import beginfunc.domain.entities.productRelated.products.BaseProduct;
import beginfunc.domain.entities.productRelated.products.Coin;
import beginfunc.domain.models.bindingModels.AuctionCreateBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BanknoteBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.BaseCollectionBindingModel;
import beginfunc.domain.models.bindingModels.collectionProducts.CoinBindingModel;
import beginfunc.domain.models.serviceModels.AuctionServiceModel;
import beginfunc.domain.models.serviceModels.products.BanknoteServiceModel;
import beginfunc.domain.models.serviceModels.products.BaseProductServiceModel;
import beginfunc.domain.models.serviceModels.PictureServiceModel;
import beginfunc.domain.models.serviceModels.products.CoinServiceModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.repositories.AuctionRepository;
import beginfunc.services.contracts.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, ProductService productService, CategoryService categoryService, ModelMapper modelMapper) {
        this.auctionRepository = auctionRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuctionServiceModel createAuction(AuctionCreateBindingModel model,
                                             BaseCollectionBindingModel collectionBindingModel, HttpSession session, UserServiceModel user) throws IOException {

        AuctionServiceModel auctionToSave = this.modelMapper.map(model, AuctionServiceModel.class);
        BaseProductServiceModel product=this.productService.createProduct(model, collectionBindingModel, session);

        auctionToSave.setSeller(user);
        auctionToSave.setCategory(this.categoryService.findByName(model.getCategory()));
        this.setAuctionPrices(auctionToSave, model);
        this.setAuctionStatus(auctionToSave, model);

        Auction auction = this.modelMapper.map(auctionToSave, Auction.class);
        BaseProduct productBase=this.createBaseProductEntity(product);

        auction.setProduct(productBase);
        this.correctModelMappersBug(auction);
        Auction savedAuction = this.auctionRepository.saveAndFlush(auction);
        return this.modelMapper.map(savedAuction, AuctionServiceModel.class);
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
    public List<AuctionServiceModel> getWaitingAuctionsOfUser(Integer userId) {
        List<Auction> waitingAuctionsOfUser = this.auctionRepository.getWaitingAuctionsOfUser(userId);
        return waitingAuctionsOfUser.stream()
                .map(a->this.modelMapper.map(a,AuctionServiceModel.class))
                .collect(Collectors.toList());
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

    private BaseProduct createBaseProductEntity(BaseProductServiceModel product) {
        if(product instanceof CoinServiceModel){
            return this.modelMapper.map(product,Coin.class);
        }else if(product instanceof BanknoteServiceModel){
            return this.modelMapper.map(product,BankNote.class);
        }else {
            return this.modelMapper.map(product,BaseProduct.class);
        }
    }

    private void setAuctionStatus(AuctionServiceModel auctionToSave, AuctionCreateBindingModel model) {
        if(model.isStartLater()){
            auctionToSave.setStatus(AuctionStatus.Waiting);
        }else {
            auctionToSave.setStatus(AuctionStatus.Active);
            auctionToSave.setStartDate(new Date());
            auctionToSave.setEndDate(getDateAfter7Days());
        }
    }

    private void setAuctionPrices(AuctionServiceModel auctionToSave, AuctionCreateBindingModel model) {
        BigDecimal wantedPrice = model.getWantedPrice();
        if(wantedPrice!=null){
            auctionToSave.setWantedPrice(wantedPrice);
            if(!auctionToSave.getType().equals(AuctionType.Preserved_Price)){
                auctionToSave.setReachedPrice(wantedPrice);
            }
        }
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
