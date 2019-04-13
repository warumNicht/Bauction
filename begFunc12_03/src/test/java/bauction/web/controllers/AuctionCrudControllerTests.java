package bauction.web.controllers;

import bauction.TestConstants;
import bauction.domain.entities.User;
import bauction.domain.entities.auctionRelated.Auction;
import bauction.repositories.AuctionRepository;
import bauction.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuctionCrudControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void emptyDb() {
        this.userRepository.deleteAll();
        this.auctionRepository.deleteAll();
        this.createTestUser();
    }

    @Test
    @WithMockUser("Ivan")
    public void createAuction_savesEntityProperly() throws Exception {

        this.mvc
                .perform(post("/auctions/create")
                        .with(csrf())
                        .param("name", TestConstants.PRODUCT_NAME)
                        .param("type", TestConstants.AUCTION_TYPE)
                        .param("startLater", "false")
                        .param("category", TestConstants.CATEGORY)
                        .param("description", TestConstants.PRODUCT_DESCRIPTION)
                        .param("town", TestConstants.TOWN)
                        .param("wantedPrice", "10"));

        long count = this.auctionRepository.count();
        Auction auction = this.auctionRepository.findAll().get(0);
        Assert.assertEquals(count,1);
        Assert.assertEquals(auction.getType().name(),TestConstants.AUCTION_TYPE);
        Assert.assertEquals(auction.getProduct().getName(),TestConstants.PRODUCT_NAME);
        Assert.assertEquals(auction.getProduct().getTown().getName(),TestConstants.TOWN);
    }

    @Test
    @WithMockUser(value = "Ivan")
    public void editAuction_savesEntityProperly() throws Exception {
        this.mvc
                .perform(post("/auctions/create")
                        .with(csrf())
                        .param("name", TestConstants.PRODUCT_NAME)
                        .param("type", TestConstants.AUCTION_TYPE)
                        .param("startLater", "false")
                        .param("category", TestConstants.CATEGORY)
                        .param("description", TestConstants.PRODUCT_DESCRIPTION)
                        .param("town", TestConstants.TOWN)
                        .param("wantedPrice", "10"));

        Auction auctionToEdit = this.auctionRepository.findAll().get(0);

        this.mvc
                .perform(post("/auctions/edit/" +auctionToEdit.getId())
                        .with(csrf())
                        .with(user("Ivan"))
                        .param("name", TestConstants.PRODUCT_NAME_OTHER)
                        .param("type", TestConstants.AUCTION_TYPE)
                        .param("startLater", "false")
                        .param("category", TestConstants.CATEGORY)
                        .param("description", TestConstants.PRODUCT_DESCRIPTION_OTHER)
                        .param("town", TestConstants.TOWN)
                        .param("wantedPrice", "10"));

        Auction actual = this.auctionRepository.findAll().get(0);
        Assert.assertEquals(actual.getProduct().getName(), TestConstants.PRODUCT_NAME_OTHER);
        Assert.assertEquals(actual.getProduct().getDescription(), TestConstants.PRODUCT_DESCRIPTION_OTHER);
    }

    @Test
    @WithMockUser("Ivan")
    public void deleteAuction_works_withSellerUser() throws Exception {
        this.mvc
                .perform(post("/auctions/create")
                        .with(csrf())
                        .param("name", TestConstants.PRODUCT_NAME)
                        .param("type", TestConstants.AUCTION_TYPE)
                        .param("startLater", "false")
                        .param("category", TestConstants.CATEGORY)
                        .param("description", TestConstants.PRODUCT_DESCRIPTION)
                        .param("town", TestConstants.TOWN)
                        .param("wantedPrice", "10"));

        Auction auctionToDel = this.auctionRepository.findAll().get(0);

        this.mvc
                .perform(post("/auctions/delete/" +auctionToDel.getId())
                        .with(csrf()));
        long count = this.auctionRepository.count();
        Assert.assertEquals(count,0);
    }

    @Test
    @WithMockUser("Ivan")
    public void deleteAuction_NotWorks_withOtherUser() throws Exception {
        this.createTestUser2();
        this.mvc
                .perform(post("/auctions/create")
                        .with(csrf())
                        .param("name", TestConstants.PRODUCT_NAME)
                        .param("type", TestConstants.AUCTION_TYPE)
                        .param("startLater", "false")
                        .param("category", TestConstants.CATEGORY)
                        .param("description", TestConstants.PRODUCT_DESCRIPTION)
                        .param("town", TestConstants.TOWN)
                        .param("wantedPrice", "10"));

        Auction auctionToDel = this.auctionRepository.findAll().get(0);

        this.mvc
                .perform(post("/auctions/delete/" +auctionToDel.getId())
                        .with(csrf())
                        .with(user("Stoqn")));
        long count = this.auctionRepository.count();
        Assert.assertEquals(count,1);
    }

    private void createTestUser() {
        User user=new User();
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setFullName(TestConstants.TEST_USER_FULL_NAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRegistrationDate(new Date());
        this.userRepository.saveAndFlush(user);
    }

    private void createTestUser2() {
        User user=new User();
        user.setUsername("Stoqn");
        user.setFullName(TestConstants.TEST_USER_FULL_NAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRegistrationDate(new Date());
        this.userRepository.saveAndFlush(user);
    }
}
