package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean registerUser(UserServiceModel userServiceModel);

    UserServiceModel loginUser(UserServiceModel userServiceModel);

    UserServiceModel findUserById(Integer id);
}
