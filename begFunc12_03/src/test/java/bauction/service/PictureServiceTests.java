package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.productRelated.Picture;
import bauction.domain.entities.productRelated.Town;
import bauction.domain.entities.productRelated.products.BaseProduct;
import bauction.domain.models.serviceModels.PictureServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.repositories.BaseProductRepository;
import bauction.repositories.PictureRepository;
import bauction.repositories.TownRepository;
import bauction.services.PictureServiceImpl;
import bauction.services.contracts.PictureService;
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
public class PictureServiceTests {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private TownRepository townRepository;
    @Autowired
    private BaseProductRepository baseProductRepository;

    private PictureService pictureService;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.pictureService=new PictureServiceImpl(this.pictureRepository,new ModelMapper());
    }

    @Test
    public void savePicture_withCorrectData_returnsCorrect(){
        PictureServiceModel pictureToSave = new PictureServiceModel(TestConstants.PICTURE_PATH);
        pictureToSave.setProduct(this.createTestProduct());
        this.pictureService.savePicture(pictureToSave);
        List<Picture> all = this.pictureRepository.findAll();
        Assert.assertEquals(all.size(),1);
        Assert.assertEquals(all.get(0).getPath(),TestConstants.PICTURE_PATH);
    }

    @Test(expected = Exception.class)
    public void savePicture_withNullData_throws(){
        this.pictureService.savePicture(new PictureServiceModel());
    }

    @Test
    public void deleteImage_withCorrectId_works(){
        PictureServiceModel pictureToSave = new PictureServiceModel(TestConstants.PICTURE_PATH);
        pictureToSave.setProduct(this.createTestProduct());
        this.pictureService.savePicture(pictureToSave);
        String savedPicId = this.pictureRepository.findAll().get(0).getId();

        this.pictureService.deleteImage(savedPicId);
        long count = this.pictureRepository.count();
        Assert.assertEquals(count,0);
    }

    @Test
    public void deleteImage_withInCorrectId_works(){
        PictureServiceModel pictureToSave = new PictureServiceModel(TestConstants.PICTURE_PATH);
        pictureToSave.setProduct(this.createTestProduct());
        this.pictureService.savePicture(pictureToSave);

        this.pictureService.deleteImage(TestConstants.INVALID_ID);
        long count = this.pictureRepository.count();
        Assert.assertEquals(count,1);
    }

    private BaseProductServiceModel createTestProduct() {
        BaseProduct product = new BaseProduct();
        product.setName(TestConstants.PRODUCT_NAME);
        product.setDescription(TestConstants.PRODUCT_DESCRIPTION);
        Town sofia = this.townRepository.saveAndFlush(new Town(TestConstants.TOWN));
        product.setTown(sofia);

        return this.modelMapper.map(this.baseProductRepository.saveAndFlush(product),BaseProductServiceModel.class);
    }
}
