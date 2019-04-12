package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.User;
import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.entities.enums.AuctionStatus;
import bauction.domain.entities.enums.AuctionType;
import bauction.domain.entities.productRelated.Town;
import bauction.domain.models.bindingModels.AuctionCreateBindingModel;
import bauction.domain.models.bindingModels.AuctionEditBindingModel;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.domain.models.serviceModels.TownServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.AuctionNotFoundException;
import bauction.repositories.AuctionRepository;
import bauction.repositories.CategoryRepository;
import bauction.repositories.TownRepository;
import bauction.repositories.UserRepository;
import bauction.services.AuctionServiceImpl;
import bauction.services.contracts.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuctionServiceTests {
    private final String SELLER_USERNAME="Ivan";
    private final String SELLER_FULL_NAME="Ivan Ivanov";
    private final String SELLER_EMAIL="van40@vsi4ko.bg";
    private final String SELLER_PASSWORD="1234";

    private final String CATEGORY="Coins";
    private final String TOWN="Sofia";

    private final String PRODUCT_NAME="Windsutzscheibe";
    private final String PRODUCT_DESCRIPTION="Sehr gut gehaltene";
    private final BigDecimal AUCTION_WANTED_PRICE=BigDecimal.TEN;
    private final BigDecimal AUCTION_REACHED_PRICE=BigDecimal.ONE;

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductService productService;
    @MockBean
    private OfferService offerService;
    @MockBean
    private BiddingService biddingService;
    @MockBean
    private CategoryService categoryService;

    private ModelMapper modelMapper;
    private AuctionServiceModel testAuctionServiceModel;
    private AuctionService auctionService;
    private UserServiceModel user;
    private CategoryServiceModel category;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.auctionService=new AuctionServiceImpl(this.auctionRepository, this.productService,
                this.offerService,this.biddingService,this.categoryService,this.modelMapper );

        this.testAuctionServiceModel=this.createTestAuction();
        this.user =this.testAuctionServiceModel.getSeller();
        this.category=testAuctionServiceModel.getCategory();
    }

    private AuctionServiceModel createTestAuction() {
        BaseProductServiceModel product = new BaseProductServiceModel();
        product.setName(PRODUCT_NAME);
        product.setDescription(PRODUCT_DESCRIPTION);
        TownServiceModel sofia = this.modelMapper.
                map(this.townRepository.saveAndFlush(new Town(TOWN)), TownServiceModel.class);
        product.setTown(sofia);

        AuctionServiceModel auction = new AuctionServiceModel();
        auction.setProduct(product);
        auction.setWantedPrice(AUCTION_WANTED_PRICE);
        auction.setReachedPrice(AUCTION_REACHED_PRICE);
        auction.setType(AuctionType.Standard);
        auction.setStatus(AuctionStatus.Waiting);
        auction.setViews(0L);

        User user=new User();
        user.setUsername(SELLER_USERNAME);
        user.setEmail(SELLER_EMAIL);
        user.setFullName(SELLER_FULL_NAME);
        user.setRegistrationDate(new Date());
        user.setPassword(SELLER_PASSWORD);
        auction.setSeller(this.modelMapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class));

        Category coins = this.categoryRepository.saveAndFlush(new Category(CATEGORY));
        auction.setCategory(this.modelMapper.map(coins,CategoryServiceModel.class));
        return auction;
    }

    @Test
    public void findAllAuctionsByStatus_returnsCorrect(){
        this.testAuctionServiceModel.setStatus(AuctionStatus.Waiting);
        this.auctionRepository.saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel,Auction.class));

        List<AuctionServiceModel> allAuctionsByStatusWaiting = this.auctionService.findAllAuctionsByStatus(AuctionStatus.Waiting);
        List<AuctionServiceModel> allAuctionsByStatusFinished = this.auctionService.findAllAuctionsByStatus(AuctionStatus.Finished);
        List<AuctionServiceModel> allAuctionsByStatusActive = this.auctionService.findAllAuctionsByStatus(AuctionStatus.Active);

        Assert.assertEquals(allAuctionsByStatusWaiting.size(),1);
        Assert.assertEquals(allAuctionsByStatusFinished.size(),0);
        Assert.assertEquals(allAuctionsByStatusActive.size(),0);
    }

    @Test
    public void findById_withCorrectId_returnsCorrect(){
        Auction expected = this.auctionRepository.saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));

        AuctionServiceModel actual = this.auctionService.findById(expected.getId());

        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getProduct().getName(), expected.getProduct().getName());
        Assert.assertEquals(actual.getSeller().getUsername(), expected.getSeller().getUsername());
        Assert.assertEquals(actual.getType(), expected.getType());
    }

    @Test(expected = AuctionNotFoundException.class)
    public void findById_withInCorrectId_throwsException(){
        this.auctionService.findById(TestConstants.INVALID_ID);
    }

    @Test
    public void increaseAuctionViews_withCorrectId_returnsCorrect(){
        Auction expected = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));
        this.auctionRepository.increaseViews(expected.getId());
        expected=this.auctionRepository.findById(expected.getId()).orElse(null);

        this.auctionService.increaseAuctionViews(expected.getId());
        this.auctionService.increaseAuctionViews(expected.getId());
        Auction increased = this.auctionRepository.findById(expected.getId()).orElse(null);
        Assert.assertEquals(increased.getViews(),0);
    }

    @Test
    public void updateAuction_withCorrectData_returnsCorrect(){
        Auction expected = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));

        AuctionServiceModel auctionToUpdate = this.modelMapper.map(expected, AuctionServiceModel.class);

        auctionToUpdate.setStatus(AuctionStatus.Finished);
        auctionToUpdate.setViews(10L);
        auctionToUpdate.setType(AuctionType.Fixed_Price);
        this.auctionService.updateAuction(auctionToUpdate);

        Auction actual = this.auctionRepository.findById(expected.getId()).orElse(null);

        Assert.assertEquals(actual.getType(), AuctionType.Fixed_Price);
        Assert.assertEquals(actual.getViews(), 10);
        Assert.assertEquals(actual.getStatus(), AuctionStatus.Finished);
    }

    @Test(expected = Exception.class)
    public void updateAuction_withNullData_throws(){
        Auction expected = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));
        AuctionServiceModel auctionToUpdate = this.modelMapper.map(expected, AuctionServiceModel.class);
        auctionToUpdate.setStatus(null);
        this.auctionService.updateAuction(auctionToUpdate);
    }

    @Test
    public void getWaitingAuctionsOfUser_withCorrectId_returnsCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);
        List<AuctionServiceModel> waitingAuctionsOfUser = this.auctionService.getWaitingAuctionsOfUser(saved.getSeller().getId());
        Assert.assertEquals(waitingAuctionsOfUser.size(),1);
    }

    @Test
    public void getWaitingAuctionsOfUser_withInCorrectId_returnsCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);
        List<AuctionServiceModel> waitingAuctionsOfUser = this.auctionService.getWaitingAuctionsOfUser(TestConstants.INVALID_ID);
        Assert.assertEquals(waitingAuctionsOfUser.size(),0);
    }

    @Test
    public void createAuction_withCorrectData_works() throws IOException {
        AuctionCreateBindingModel model = new AuctionCreateBindingModel();
        model.setCategory(CATEGORY);
        model.setTown(TOWN);
        model.setDescription(PRODUCT_DESCRIPTION);
        model.setType("Fixed_Price");
        model.setName(PRODUCT_NAME);
        model.setWantedPrice(AUCTION_WANTED_PRICE);

        Mockito.when(this.productService.createProduct(model,null))
                .thenReturn(this.testAuctionServiceModel.getProduct());

        Mockito.when(this.categoryService.findByName(CATEGORY))
                .thenReturn(this.category);

        AuctionServiceModel actual = this.auctionService.createAuction(model, null, this.user);
        long count = this.auctionRepository.count();
        Assert.assertEquals(count,1);
        Assert.assertEquals(actual.getType(), AuctionType.Fixed_Price);
        Assert.assertEquals(actual.getProduct().getName(), model.getName());
        Assert.assertEquals(actual.getProduct().getDescription(), model.getDescription());
        Assert.assertEquals(actual.getWantedPrice(), model.getWantedPrice());
    }

    @Test
    public void editAuction_withCorrectData_works() throws IOException {
        Auction saved = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));

        AuctionServiceModel auctionToEdit = this.modelMapper.map(saved, AuctionServiceModel.class);

        AuctionEditBindingModel editModel = new AuctionEditBindingModel();
        editModel.setCategory(CATEGORY);
        editModel.setTown(TOWN);
        editModel.setDescription(PRODUCT_DESCRIPTION);
        editModel.setType("Standard");
        editModel.setName(PRODUCT_NAME);
        editModel.setWantedPrice(BigDecimal.valueOf(50));

        Mockito.when(this.productService.getChangedProduct(auctionToEdit,editModel,null, null))
                .thenReturn(this.testAuctionServiceModel.getProduct());

        this.auctionService.editAuction(auctionToEdit,editModel,null,null);

        Auction actual = this.auctionRepository.findAll().get(0);

        Assert.assertEquals(actual.getType(), AuctionType.Standard);
        Assert.assertEquals(actual.getWantedPrice(), BigDecimal.valueOf(50));
    }

    @Test(expected = Exception.class)
    public void editAuction_withNullData_throws() throws IOException {
        this.auctionService.editAuction(null,null,null,null);
    }

    @Test
    public void startAuction_works(){
        Auction expected = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));

        AuctionServiceModel auctionToStart = this.modelMapper.map(expected, AuctionServiceModel.class);
        this.auctionService.startAuction(auctionToStart);
        Auction actual = this.auctionRepository.findById(expected.getId()).orElse(null);

        Assert.assertEquals(actual.getStatus(), AuctionStatus.Active);
    }

    @Test(expected = Exception.class)
    public void createAuction_withNullData_throws() throws IOException {
        this.auctionService.createAuction(null, null, this.user);
    }

    @Test
    public void deleteAuction_withCorrectId_works(){
        Auction saved = this.auctionRepository.
                saveAndFlush(this.modelMapper.map(this.testAuctionServiceModel, Auction.class));
        this.auctionService.deleteById(saved.getId());
        long count = this.auctionRepository.count();
        Assert.assertEquals(count,0);
    }

    @Test(expected = AuctionNotFoundException.class)
    public void deleteAuction_withInCorrectId_throws(){
        this.auctionService.deleteById(TestConstants.INVALID_ID);
    }

    @Test
    public void getFinishedAuctionsOfUserWithDeal_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Finished);
        toSave.setBuyer(toSave.getSeller());
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        List<AuctionServiceModel> all = this.auctionService.getFinishedAuctionsOfUserWithDeal(this.user.getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void getFinishedAuctionsOfUserWithoutDeal_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Finished);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        List<AuctionServiceModel> all = this.auctionService.getFinishedAuctionsOfUserWithoutDeal(this.user.getId());
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void getAuctionSellerId_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        String actual = this.auctionService.getAuctionSellerId(saved.getId());
        Assert.assertEquals(actual, toSave.getSeller().getId());
    }

    @Test
    public void findAllActivesAuctionsExceedingEndDate_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Active);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -5);
        toSave.setEndDate(cal.getTime());
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        List<AuctionServiceModel> all = this.auctionService.findAllActivesAuctionsExceedingEndDate();
        Assert.assertEquals(all.size(),1);
    }

    @Test
    public void findAllFinishedAuctionsWithoutDeal_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Finished);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);
        List<AuctionServiceModel> actual = this.auctionService.findAllFinishedAuctionsWithoutDeal();
        Assert.assertEquals(actual.size(),1);
    }

    @Test
    public void getSortedAuctions_directionASC_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Active);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        Auction toSave2 = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave2.setStatus(AuctionStatus.Active);
        toSave2.setReachedPrice(BigDecimal.valueOf(50));
        Auction saved2 = this.auctionRepository.saveAndFlush(toSave2);

        List<AuctionServiceModel> asc = this.auctionService.getSortedAuctions(CATEGORY, "ascending");
        Assert.assertEquals(asc.size(),2);
        Assert.assertEquals(asc.get(0).getReachedPrice(),BigDecimal.ONE);
        Assert.assertEquals(asc.get(1).getReachedPrice(),BigDecimal.valueOf(50));
    }

    @Test
    public void getSortedAuctions_directionDESC_worksCorrect(){
        Auction toSave = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave.setStatus(AuctionStatus.Active);
        Auction saved = this.auctionRepository.saveAndFlush(toSave);

        Auction toSave2 = this.modelMapper.map(this.testAuctionServiceModel, Auction.class);
        toSave2.setStatus(AuctionStatus.Active);
        toSave2.setReachedPrice(BigDecimal.valueOf(50));
        Auction saved2 = this.auctionRepository.saveAndFlush(toSave2);

        List<AuctionServiceModel> asc = this.auctionService.getSortedAuctions(CATEGORY, "descending");
        Assert.assertEquals(asc.size(),2);
        Assert.assertEquals(asc.get(0).getReachedPrice(),BigDecimal.valueOf(50));
        Assert.assertEquals(asc.get(1).getReachedPrice(),BigDecimal.ONE);
    }

}
