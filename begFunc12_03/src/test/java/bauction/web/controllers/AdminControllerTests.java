package bauction.web.controllers;

import bauction.TestConstants;
import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.models.bindingModels.CategoryBindingModel;
import bauction.repositories.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;


    @Before
    public void emptyDb() {
       this.categoryRepository.deleteAll();
    }

    @Test
    public void index_ReturnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
   @WithMockUser(roles={"ADMIN"})
    public void add_ReturnsCorrectView() throws Exception {
        this.mvc
                .perform(get("/admin/create/category"))
                .andExpect(view().name("admin/add-category"));

   }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void addCategoryPost_savesEntityProperly() throws Exception {

        this.mvc.perform(post("/admin/create/category")
                .with(csrf())
                .param("name", TestConstants.CATEGORY));

        List<Category> all = this.categoryRepository.findAll();
        Assert.assertEquals(this.categoryRepository.count(),1);
        Assert.assertEquals(all.get(0).getName(),TestConstants.CATEGORY);
    }

    @Test
    public void allUsers_withGuestRedirectToLogin() throws Exception {
        this.mvc
                .perform(get("/admin/users/all"))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    public void editUser_worksCorrectly() throws Exception {

    }


}
