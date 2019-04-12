package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.User;
import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.auctionRelated.Bidding;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.entities.enums.AuctionType;
import bauction.domain.entities.productRelated.Town;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.domain.models.serviceModels.TownServiceModel;
import bauction.domain.models.serviceModels.participations.BiddingServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.repositories.*;
import bauction.services.BiddingServiceImpl;
import bauction.services.contracts.BiddingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BiddingServiceTests {

    @Autowired
    private BiddingRepository biddingRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private BiddingService biddingService;
    private ModelMapper modelMapper;
    private AuctionServiceModel auctionServiceModel;


    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.biddingService=new BiddingServiceImpl(this.biddingRepository, this.modelMapper);
        this.auctionServiceModel=this.createTestAuction();
    }

    @Test
    public void deleteBiddingsOfAuctionById_withInCorrectId_works(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        this.biddingService.registerBidding(biddingServiceModel);

        this.biddingService.deleteBiddingsOfAuctionById(TestConstants.INVALID_ID);
        Long count = this.biddingRepository.count();
        Assert.assertEquals(1, (long) count);
    }

    @Test
    public void registerBidding_withCorrectData_returnsCorrect(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        this.biddingService.registerBidding(biddingServiceModel);

        List<Bidding> all = this.biddingRepository.findAll();
        Assert.assertEquals(all.size(),1);
        Assert.assertEquals(all.get(0).getBiddingStep(), BigDecimal.ONE);
        Assert.assertEquals(all.get(0).getReachedPrice(), BigDecimal.TEN);
    }

    @Test(expected = Exception.class)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void registerBidding_withNullData_throws(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        null, null);
        this.biddingService.registerBidding(biddingServiceModel);
    }

    @Test
    public void findAllBiddingsOfAuction_withCorrectId_returnsCorrect(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        this.biddingService.registerBidding(biddingServiceModel);
        List<BiddingServiceModel> all = this.biddingService.findAllBiddingsOfAuction(this.auctionServiceModel.getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void findAllBiddingsOfAuction_withInCorrectId_returnsZero(){
        List<BiddingServiceModel> all = this.biddingService.findAllBiddingsOfAuction(TestConstants.INVALID_ID);
        Assert.assertEquals(all.size(),0);
    }

    @Test
    public void getAuctionBiddingCount_withCorrectId_returnsCorrect(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        BiddingServiceModel biddingServiceModel2 =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        this.biddingService.registerBidding(biddingServiceModel);
        this.biddingService.registerBidding(biddingServiceModel2);

        Long count = this.biddingService.getAuctionBiddingCount(this.auctionServiceModel.getId());
        Assert.assertEquals(2, (long) count);
    }

    @Test
    public void getAuctionBiddingCount_withInCorrectId_returnsZero(){
        Long count = this.biddingService.getAuctionBiddingCount(TestConstants.INVALID_ID);
        Assert.assertEquals(0, (long) count);
    }

    @Test
    public void findHighestBiddingOfAuction_withInCorrectId_returnsCorrect(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        BiddingServiceModel biddingServiceModel2 =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.valueOf(50));

        this.biddingService.registerBidding(biddingServiceModel);
        this.biddingService.registerBidding(biddingServiceModel2);

        BiddingServiceModel highestBiddingOfAuction = this.biddingService.findHighestBiddingOfAuction(this.auctionServiceModel.getId());
        Assert.assertEquals(highestBiddingOfAuction.getReachedPrice(),BigDecimal.valueOf(50));
    }

    @Test
    public void findHighestBiddingOfAuction_withInCorrectId_returnsNull(){
        BiddingServiceModel highestBiddingOfAuction = this.biddingService.findHighestBiddingOfAuction(TestConstants.INVALID_ID);
        Assert.assertNull(highestBiddingOfAuction);
    }

    @Test
    @DirtiesContext
    public void deleteBiddingsOfAuctionById_withCorrectId_works(){
        BiddingServiceModel biddingServiceModel =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.TEN);

        BiddingServiceModel biddingServiceModel2 =
                new BiddingServiceModel(new Date(),this.auctionServiceModel.getSeller(),this.auctionServiceModel,
                        BigDecimal.ONE, BigDecimal.valueOf(50));

        this.biddingService.registerBidding(biddingServiceModel);
        this.biddingService.registerBidding(biddingServiceModel2);

        this.biddingService.deleteBiddingsOfAuctionById(this.auctionServiceModel.getId());
        long count = this.biddingRepository.count();
        Assert.assertEquals(count,0);
        System.out.println();
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
        auction.setStatus(AuctionStatus.Waiting);
        auction.setViews(0L);

        User user=new User();
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setFullName(TestConstants.TEST_USER_FULL_NAME);
        user.setRegistrationDate(new Date());
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        auction.setSeller(this.modelMapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class));

        Category coins = this.categoryRepository.saveAndFlush(new Category(TestConstants.CATEGORY));
        auction.setCategory(this.modelMapper.map(coins,CategoryServiceModel.class));
        Auction auctionToSave = this.modelMapper.map(auction, Auction.class);
        auctionToSave=this.auctionRepository.saveAndFlush(auctionToSave);
        return this.modelMapper.map(auctionToSave,AuctionServiceModel.class);
    }
}
