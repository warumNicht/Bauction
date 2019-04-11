package bauction.service;

import bauction.domain.entities.User;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.DuplicatedUserException;
import bauction.error.UserNotFoundException;
import bauction.repositories.RoleRepository;
import bauction.repositories.UserRepository;
import bauction.services.UserServiceImpl;
import bauction.services.contracts.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTests {
    private final String TEST_USER_USERNAME="Pesho";
    private final String TEST_USER_FULL_NAME="Pesho Peshev";
    private final String TEST_USER_PASSWORD="1aW2";
    private final String TEST_USER_EMAIL="pesho@abv.bg";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder encoder;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserServiceModel testUser;
    private User testUserEntity;


    @Before
    public void init(){
        this.modelMapper=new ModelMapper();
        this.encoder=new BCryptPasswordEncoder();
        this.userService=new UserServiceImpl(this.userRepository,this.roleRepository,
                this.modelMapper , this.encoder);
        this.testUser=this.initTestUser();
        this.testUserEntity=this.modelMapper.map(this.testUser,User.class);
    }

    private UserServiceModel initTestUser() {
        UserServiceModel user = new UserServiceModel();
        user.setFullName(TEST_USER_FULL_NAME);
        user.setUsername(TEST_USER_USERNAME);
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(TEST_USER_PASSWORD);
        user.setRegistrationDate(new Date());
        return user;
    }

    @Test
    public void registerUser_WithCorrectData(){
        this.userService.registerUser(this.testUser);

        UserServiceModel actual = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(actual.getUsername(),TEST_USER_USERNAME);
        Assert.assertEquals(actual.getFullName(),TEST_USER_FULL_NAME);
        Assert.assertEquals(actual.getEmail(),TEST_USER_EMAIL);
    }

    @Test(expected = Exception.class)
    public void registerUser_WithNullData(){
        this.userService.registerUser(new UserServiceModel());
    }

    @Test(expected = Exception.class)
    public void registerUser_WithDuplicatedUsername(){
        this.userService.registerUser(this.testUser);
        this.userService.registerUser(this.testUser);
    }

    @Test
    public void findUserById_WithCorrectData_returnsCorrect(){
        this.userService.registerUser(this.testUser);
        User actual = this.userRepository.findAll().get(0);
        UserServiceModel expected = this.userService.findUserById(actual.getId());
        Assert.assertEquals(actual.getId(),expected.getId());
        Assert.assertEquals(actual.getUsername(),expected.getUsername());
        Assert.assertEquals(actual.getFullName(),expected.getFullName());
        Assert.assertEquals(actual.getRegistrationDate(),expected.getRegistrationDate());
    }

    @Test(expected = UserNotFoundException.class)
    public void findUserById_WithInCorrectData_throwException(){
        this.userService.findUserById("123");
    }

    @Test
    public void findAllUsersWithoutTheLoggedIn_returnsCorrect(){
        this.userService.registerUser(this.testUser);
        User testUser = this.userRepository.findAll().get(0);
        List<UserServiceModel> actual = this.userService.findAllUsersWithoutTheLoggedIn(testUser.getId());

        Assert.assertEquals(actual.size(),0);
    }

    @Test
    public void updateUser_withCorrectData_returnsCorrect(){
        User user = this.userRepository.saveAndFlush(this.testUserEntity);

        UserServiceModel userToBeEdited = this.modelMapper.map(user, UserServiceModel.class);
        userToBeEdited.setUsername("Wilhelm Busch");
        userToBeEdited.setEmail("wili@max.de");

        UserServiceModel actual = this.userService.updateUser(userToBeEdited);
        UserServiceModel expected = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(actual.getEmail(), expected.getEmail());
        Assert.assertEquals(actual.getUsername(), expected.getUsername());
    }

    @Test(expected = Exception.class)
    public void updateUser_withNullData_throwException(){
        User user = this.userRepository.saveAndFlush(this.testUserEntity);

        UserServiceModel userToBeEdited = this.modelMapper.map(user, UserServiceModel.class);
        userToBeEdited.setUsername(null);
        userToBeEdited.setPassword(null);
        userToBeEdited.setAuthorities(null);
        user.setUsername(null);
        this.userService.updateUser(userToBeEdited);
    }
    @Test
    public void existsUserByUsername_returnsCorrect(){
        User user = this.userRepository.saveAndFlush(this.testUserEntity);
        boolean expectedTrue = this.userService.existsUserByUsername(user.getUsername());
        boolean expectedFalse = this.userService.existsUserByUsername("notExistent");

        Assert.assertTrue(expectedTrue);
        Assert.assertFalse(expectedFalse);
    }

}
