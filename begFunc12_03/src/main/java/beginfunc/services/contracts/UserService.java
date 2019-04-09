package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel loginUser(UserServiceModel userServiceModel);

    UserServiceModel findUserById(String id);

    List<UserServiceModel> findAllUsersWithoutTheLoggedIn(String loggedInId);

    UserServiceModel updateUser(UserServiceModel toEdit);
}
