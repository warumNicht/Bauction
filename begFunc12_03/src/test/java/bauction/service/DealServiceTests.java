package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.Deal;
import bauction.domain.entities.User;
import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.entities.enums.AuctionType;
import bauction.domain.entities.productRelated.Town;
import bauction.domain.models.bindingModels.CommentBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.domain.models.serviceModels.TownServiceModel;
import bauction.domain.models.serviceModels.deals.DealServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.DealNotFoundException;
import bauction.repositories.*;
import bauction.services.DealServiceImpl;
import bauction.services.contracts.DealService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class DealServiceTests {
    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;
    private AuctionServiceModel auctionServiceModel;

    private DealService dealService;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.dealService=new DealServiceImpl(this.dealRepository, this.modelMapper);
        this.auctionServiceModel=this.createTestAuction();
    }


    @Test
    public void findDealById_withCorrectId_returnsCorrect(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel savedDeal = this.dealService.registerDeal(deal);
        DealServiceModel actual = this.dealService.findDealById(savedDeal.getId());

        Assert.assertEquals(actual.getDealPrice(),deal.getDealPrice());
        Assert.assertEquals(actual.getId(),savedDeal.getId());
        Assert.assertEquals(actual.getDateTime(),deal.getDateTime());
    }

    @Test(expected = DealNotFoundException.class)
    public void findDealById_withInCorrectId_throws(){
        this.dealService.findDealById(TestConstants.INVALID_ID);
    }


    @Test
    public void registerDeal_withCorrectData_returnsCorrect(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel actual = this.dealService.registerDeal(deal);

        List<Deal> all = this.dealRepository.findAll();
        Assert.assertEquals(all.size(),1);

        Deal expected = all.get(0);
        Assert.assertEquals(expected.getDealPrice(), actual.getDealPrice());
    }

    @Test(expected = Exception.class)
    public void registerDeal_withNullData_throws(){
        this.dealService.registerDeal(new DealServiceModel());
    }

    @Test
    public void allRecentDealsOfUser_withCorrectId_returnsCorrect(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel actual = this.dealService.registerDeal(deal);
        List<DealServiceModel> dealServiceModels = this.dealService.allRecentDealsOfUser(this.auctionServiceModel.getSeller().getId());

        Assert.assertEquals(dealServiceModels.size(),1);
    }

    @Test
    public void allRecentDealsOfUser_withInCorrectId_returnsZero(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel actual = this.dealService.registerDeal(deal);
        List<DealServiceModel> dealServiceModels = this.dealService.allRecentDealsOfUser(TestConstants.INVALID_ID);

        Assert.assertEquals(dealServiceModels.size(),0);
    }

    @Test
    public void registerComment_withCorrectData_works(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel savedDeal = this.dealService.registerDeal(deal);

        CommentBindingModel model = new CommentBindingModel(savedDeal.getId(),
                "date", "Seller", "Buyer");
        model.setContent(TestConstants.COMMENT_CONTENT);
        model.setEstimation("Positive");

        this.dealService.registerComment(model,this.auctionServiceModel.getSeller());

        Deal actual = this.dealRepository.findAll().get(0);
        Assert.assertTrue(actual.getSellerComment()!=null);
    }

    @Test(expected = Exception.class)
    public void registerComment_withNullData_throws(){
        this.dealService.registerComment(null,null);
    }

    @Test
    public void allDealCommentsOfUser_withCorrectId_works(){
        DealServiceModel deal = new DealServiceModel(new Date(), TestConstants.AUCTION_WANTED_PRICE, this.auctionServiceModel.getSeller(),
                this.auctionServiceModel.getSeller(), this.auctionServiceModel);

        DealServiceModel savedDeal = this.dealService.registerDeal(deal);

        CommentBindingModel model = new CommentBindingModel(savedDeal.getId(),
                "date", "Seller", "Buyer");
        model.setContent(TestConstants.COMMENT_CONTENT);
        model.setEstimation("Positive");

        this.dealService.registerComment(model,this.auctionServiceModel.getSeller());

        List<DealServiceModel> all = this.dealService.allRecentDealsOfUser(this.auctionServiceModel.getSeller().getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void allDealCommentsOfUser_withInCorrectId_returnsZero(){
        List<DealServiceModel> all = this.dealService.allRecentDealsOfUser(TestConstants.INVALID_ID);
        Assert.assertEquals(all.size(),0);
    }

    private AuctionServiceModel createTestAuction() {
        BaseProductServiceModel product = new BaseProductServiceModel();
        product.setName(TestConstants.PRODUCT_NAME);
        product.setDescription(TestConstants.PRODUCT_DESCRIPTION);
        TownServiceModel sofia = this.modelMapper.
                map(this.townRepository.saveAndFlush(new Town(TestConstants.TOWN)), TownServiceModel.class);
        product.setTown(sofia);

        AuctionServiceModel auction = new AuctionServiceModel();
        auction.setProduct(product);
        auction.setWantedPrice(TestConstants.AUCTION_WANTED_PRICE);
        auction.setReachedPrice(TestConstants.AUCTION_REACHED_PRICE);
        auction.setType(AuctionType.Standard);
        auction.setStatus(AuctionStatus.Active);

        User user=new User();
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setFullName(TestConstants.TEST_USER_FULL_NAME);
        user.setRegistrationDate(new Date());
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        auction.setSeller(this.modelMapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class));

        Category category = this.categoryRepository.saveAndFlush(new Category(TestConstants.CATEGORY));
        auction.setCategory(this.modelMapper.map(category,CategoryServiceModel.class));
        Auction auctionToSave = this.modelMapper.map(auction, Auction.class);
        auctionToSave=this.auctionRepository.saveAndFlush(auctionToSave);
        return this.modelMapper.map(auctionToSave,AuctionServiceModel.class);
    }

}
