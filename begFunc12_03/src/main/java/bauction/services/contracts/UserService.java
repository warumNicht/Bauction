package bauction.services.contracts;

import bauction.domain.models.serviceModels.users.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserById(String id);

    List<UserServiceModel> findAllUsersWithoutTheLoggedIn(String loggedInId);

    UserServiceModel updateUser(UserServiceModel toEdit);

    boolean existsUserByUsername(String username);

    UserServiceModel findUserByUsername(String username);
}
