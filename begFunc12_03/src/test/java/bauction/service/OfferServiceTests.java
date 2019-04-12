package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.User;
import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.entities.auctionRelated.Offer;
import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.entities.enums.AuctionType;
import bauction.domain.entities.productRelated.Town;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.domain.models.serviceModels.TownServiceModel;
import bauction.domain.models.serviceModels.participations.OfferServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.NoPositiveOfferException;
import bauction.error.OfferNotFoundException;
import bauction.repositories.*;
import bauction.services.OfferServiceImpl;
import bauction.services.contracts.BiddingService;
import bauction.services.contracts.OfferService;
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
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OfferServiceTests {
    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;
    private AuctionServiceModel auctionServiceModel;
    private OfferService offerService;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.offerService=new OfferServiceImpl(this.offerRepository, this.modelMapper);
        this.auctionServiceModel=this.createTestAuction();
    }

    @Test
    public void registerOffer_withCorrectData_works(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);

        List<Offer> all = this.offerRepository.findAll();
        Assert.assertEquals(all.size(),1);
        Assert.assertEquals(all.get(0).getOfferedPrice(),TestConstants.OFFER_PRICE_VALID);
    }

    @Test(expected = NoPositiveOfferException.class)
    public void registerOffer_withNegativePrice_throws(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_INVALID);

        this.offerService.registerOffer(offerServiceModel);
    }

    @Test(expected = Exception.class)
    public void registerOffer_withNNullData_throws(){
        this.offerService.registerOffer(new OfferServiceModel());
    }

    @Test
    public void findAllOffersOfAuction_withCorrectId_works(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        List<OfferServiceModel> all = this.offerService.findAllOffersOfAuction(this.auctionServiceModel.getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void findAllOffersOfAuction_withInCorrectId_returnsEmptyList(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        List<OfferServiceModel> all = this.offerService.findAllOffersOfAuction(TestConstants.INVALID_ID);
        Assert.assertEquals(all.size(),0);
    }

    @Test
    public void findAllActiveOffersToUser_withCorrectId_works(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);

        List<OfferServiceModel> all = this.offerService.findAllActiveOffersToUser(this.auctionServiceModel.getSeller().getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void findAllActiveOffersToUser_withInCorrectId_returnsEmptyList(){
        List<OfferServiceModel> all = this.offerService.findAllActiveOffersToUser(TestConstants.INVALID_ID);
        Assert.assertEquals(all.size(),0);
    }

    @Test
    public void getAuctionOffersCount_withCorrectId_returnsCorrect(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        Long count = this.offerService.getAuctionOffersCount(this.auctionServiceModel.getId());
        Assert.assertEquals((long)count,1);
    }

    @Test
    public void getAuctionOffersCount_withInCorrectId_returnsZero(){
        Long count = this.offerService.getAuctionOffersCount(TestConstants.INVALID_ID);
        Assert.assertEquals((long)count,0);
    }

    @Test
    public void acceptOffer_withCorrectId_returnsCorrect(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        String savedOfferId = this.offerRepository.findAll().get(0).getId();
        OfferServiceModel actual = this.offerService.acceptOffer(savedOfferId);
        Assert.assertTrue(actual.isAccepted());
    }

    @Test(expected = OfferNotFoundException.class)
    public void acceptOffer_withInCorrectId_throws(){
        this.offerService.acceptOffer(TestConstants.INVALID_ID);
    }

    @Test
    public void deleteOffersOfAuctionById_withCorrectId_works(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        this.offerService.deleteOffersOfAuctionById(this.auctionServiceModel.getId());
        List<Offer> all = this.offerRepository.findAll();

        Assert.assertTrue(all.isEmpty());
    }

    @Test
    public void deleteOffersOfAuctionById_withInCorrectId_works(){
        OfferServiceModel offerServiceModel = new OfferServiceModel(new Date(), this.auctionServiceModel.getSeller(), this.auctionServiceModel,
                TestConstants.OFFER_PRICE_VALID);

        this.offerService.registerOffer(offerServiceModel);
        this.offerService.deleteOffersOfAuctionById(TestConstants.INVALID_ID);
        List<Offer> all = this.offerRepository.findAll();

        Assert.assertTrue(all.size()==1);
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
