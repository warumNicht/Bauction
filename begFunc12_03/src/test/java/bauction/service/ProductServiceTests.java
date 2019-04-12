package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.User;
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
import bauction.repositories.*;
import bauction.services.ProductServiceImpl;
import bauction.services.contracts.PictureService;
import bauction.services.contracts.ProductService;
import bauction.services.contracts.TownService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTests {
    @Autowired
    private BaseProductRepository baseProductRepository;

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private  TownService townService;
    @MockBean
    private  PictureService pictureService;

    private  ModelMapper modelMapper;
    private ProductService productService;
    private AuctionServiceModel auctionServiceModel;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.productService=new ProductServiceImpl(this.baseProductRepository, this.townService,
                this.pictureService, null,this.modelMapper);

        this.auctionServiceModel=this.createTestAuction();
    }

    @Test
    public void createProduct_withCorrectData_returnsCorrect() throws IOException {
        AuctionCreateBindingModel model = new AuctionCreateBindingModel();
        model.setCategory(TestConstants.CATEGORY);
        model.setTown(TestConstants.TOWN);
        model.setDescription(TestConstants.PRODUCT_DESCRIPTION);
        model.setType("Fixed_Price");
        model.setName(TestConstants.PRODUCT_NAME);
        model.setWantedPrice(TestConstants.AUCTION_WANTED_PRICE);

        Mockito.when(this.townService.findByNameOrElseCreateByName(model.getTown()))
                .thenReturn(this.auctionServiceModel.getProduct().getTown());

        BaseProductServiceModel actual = this.productService.createProduct(model, null);

        Assert.assertEquals(actual.getDescription(), model.getDescription());
        Assert.assertEquals(actual.getName(), model.getName());
        Assert.assertEquals(actual.getTown().getName(), model.getTown());
    }

    @Test(expected = Exception.class)
    public void createProduct_withNullData_throws() throws IOException {
        this.productService.createProduct(null, null);
    }


    @Test
    public void getChangedProduct_withCorrectData_returnsCorrect() throws IOException {

        AuctionEditBindingModel editModel = new AuctionEditBindingModel();
        editModel.setCategory(TestConstants.CATEGORY);
        editModel.setTown(TestConstants.TOWN);
        editModel.setDescription(TestConstants.PRODUCT_DESCRIPTION_OTHER);
        editModel.setType("Standard");
        editModel.setName(TestConstants.PRODUCT_NAME_OTHER);
        editModel.setWantedPrice(BigDecimal.valueOf(50));

        Mockito.when(this.townService.findByNameOrElseCreateByName(any()))
                .thenReturn(this.auctionServiceModel.getProduct().getTown());

        Mockito.when(this.pictureService.findAllByProductId(this.auctionServiceModel.getProduct().getId()))
                .thenReturn(null);

        BaseProductServiceModel actual = this.productService.getChangedProduct(this.auctionServiceModel, editModel, null, null);
        Assert.assertEquals(actual.getName(),TestConstants.PRODUCT_NAME_OTHER);
        Assert.assertEquals(actual.getDescription(),TestConstants.PRODUCT_DESCRIPTION_OTHER);
    }

    @Test(expected = Exception.class)
    public void getChangedProduct_withNullData_throws() throws IOException {
        this.productService.getChangedProduct(this.auctionServiceModel, null, null, null);
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
        auction.setType(AuctionType.Fixed_Price);
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
        return auction;
    }
}
