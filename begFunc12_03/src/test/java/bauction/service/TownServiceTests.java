package bauction.service;

import bauction.TestConstants;
import bauction.domain.models.serviceModels.TownServiceModel;
import bauction.repositories.TownRepository;
import bauction.services.TownServiceImpl;
import bauction.services.contracts.TownService;
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

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TownServiceTests {
    @Autowired
    private TownRepository townRepository;
    private TownService townService;

    @Before
    public void init(){
        this.townService=new TownServiceImpl(this.townRepository,new ModelMapper());
    }

    @Test
    public void findByNameOrElseCreateByName_returnsCorrect(){
        TownServiceModel actual = this.townService.findByNameOrElseCreateByName(TestConstants.TOWN);
        Assert.assertEquals(actual.getName(), TestConstants.TOWN);
    }

    @Test
    public void findByNameOrElseCreateByName_withDuplicatedName_returnsCorrect(){
        TownServiceModel firstSaved = this.townService.findByNameOrElseCreateByName(TestConstants.TOWN);
        TownServiceModel actual = this.townService.findByNameOrElseCreateByName(TestConstants.TOWN);
        Assert.assertEquals(actual.getName(), TestConstants.TOWN);
    }
}
