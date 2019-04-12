package bauction.service;

import bauction.domain.entities.auctionRelated.Category;
import bauction.domain.models.serviceModels.CategoryServiceModel;
import bauction.error.CategoryNotFoundException;
import bauction.error.DuplicatedCategoryException;
import bauction.repositories.CategoryRepository;
import bauction.services.CategoryServiceImpl;
import bauction.services.contracts.CategoryService;
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
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTests {
    private final String CATEGORY_TEST_NAME_COINS="Coins";
    private final String CATEGORY_TEST_NAME_BANKNOTES="Banknotes";
    private final String CATEGORY_TEST_TO_LONG_NAME="1234567890123456789012345678901";

    @Autowired
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    private CategoryService categoryService;
    private CategoryServiceModel testCategory;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.categoryService=new CategoryServiceImpl(this.categoryRepository, this.modelMapper);
        this.testCategory=this.initTestCategory();
    }

    private CategoryServiceModel initTestCategory() {
        CategoryServiceModel category = new CategoryServiceModel();
        category.setName(CATEGORY_TEST_NAME_COINS);
        return category;
    }

    @Test
    public void saveCategoryWithCorrectData(){
        CategoryServiceModel actual = this.categoryService.addCategory(this.testCategory);
        CategoryServiceModel expected = this.modelMapper
                .map(this.categoryRepository.findAll().get(0), CategoryServiceModel.class);

        Assert.assertEquals(actual.getName(),expected.getName());
    }

    @Test(expected = Exception.class)
    public void saveCategoryWithNullData(){
        this.categoryService.addCategory(new CategoryServiceModel());
    }

    @Test(expected = Exception.class)
    public void saveCategoryWithToLongData(){
        CategoryServiceModel longNameCategory = new CategoryServiceModel();
        longNameCategory.setName(CATEGORY_TEST_TO_LONG_NAME);
        this.categoryService.addCategory(new CategoryServiceModel());
    }

    @Test(expected = Exception.class)
    public void saveDuplicatedCategory(){
        this.categoryService.addCategory(this.testCategory);
        this.categoryService.addCategory(this.testCategory);
    }

    @Test
    public void findAllCategories(){
        this.categoryService.addCategory(this.testCategory);
        CategoryServiceModel banknotes = new CategoryServiceModel();
        banknotes.setName(CATEGORY_TEST_NAME_BANKNOTES);
        this.categoryService.addCategory(banknotes);

        List<Category> all = this.categoryRepository.findAll();
        Assert.assertEquals(all.size(),2);
    }

    @Test(expected = Exception.class)
    public void findByName_withInexistentData_throwsException(){
        CategoryServiceModel actual = this.categoryService.findByName(CATEGORY_TEST_NAME_COINS);
    }


}
