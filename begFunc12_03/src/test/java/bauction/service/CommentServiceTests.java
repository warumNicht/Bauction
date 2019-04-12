package bauction.service;

import bauction.TestConstants;
import bauction.domain.entities.Comment;
import bauction.domain.entities.User;
import bauction.domain.entities.enums.Estimation;
import bauction.domain.models.serviceModels.deals.CommentServiceModel;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.repositories.CommentRepository;
import bauction.repositories.UserRepository;
import bauction.services.CommentServiceImpl;
import bauction.services.contracts.CommentService;
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
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentServiceTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private CommentService commentService;
    private UserServiceModel testUser;

    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.commentService=new CommentServiceImpl(this.commentRepository, this.modelMapper);
        this.testUser=this.createTestUser();
    }

    @Test
    public void registerComment_withCorrectData_works(){
        CommentServiceModel commentServiceModel =
                new CommentServiceModel(new Date(), TestConstants.COMMENT_CONTENT, Estimation.Positive, this.testUser);

        this.commentService.registerComment(commentServiceModel);
        List<Comment> all = this.commentRepository.findAll();

        Assert.assertEquals(all.size(),1);
        Assert.assertEquals(all.get(0).getContent(), TestConstants.COMMENT_CONTENT);
    }

    @Test(expected = Exception.class)
    public void registerComment_withNullData_throws(){
        this.commentService.registerComment(new CommentServiceModel());
        System.out.println();
    }



    private UserServiceModel createTestUser(){
        User user=new User();
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setFullName(TestConstants.TEST_USER_FULL_NAME);
        user.setRegistrationDate(new Date());
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user  = this.userRepository.saveAndFlush(user);
        return this.modelMapper.map(user, UserServiceModel.class);
    }
}
