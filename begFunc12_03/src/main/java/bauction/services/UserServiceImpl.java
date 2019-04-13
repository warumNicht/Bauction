package bauction.services;

import bauction.constants.AppConstants;
import bauction.constants.ErrorMessagesConstants;
import bauction.domain.entities.User;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.UserNotFoundException;
import bauction.repositories.RoleRepository;
import bauction.repositories.UserRepository;
import bauction.services.contracts.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.encoder.encode(user.getPassword()));

        if (this.userRepository.count() == 0) {
            this.userRepository.insertRootUser(AppConstants.ROOT_USER_ID, user.getUsername(),
                    user.getFullName(), user.getPassword(), user.getEmail(), new Date());

            User rootUser = this.userRepository.findById(AppConstants.ROOT_USER_ID).orElse(null);
            this.giveRolesToRoot(rootUser);
            this.userRepository.save(rootUser);
        }else {
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public UserServiceModel loginUser(UserServiceModel userServiceModel) {
        return this.userRepository.findByUsername(userServiceModel.getUsername())
                .filter(u -> u.getPassword().equals(DigestUtils.sha256Hex(userServiceModel.getPassword())))
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public UserServiceModel findUserById(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessagesConstants.NOT_EXISTENT_USER_ID_MESSAGE));
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsersWithoutTheLoggedIn(String loggedInId) {
        return this.userRepository.findAllUsersExceptLoggedIn(loggedInId).stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessagesConstants.NOT_EXISTENT_USERNAME_MESSAGE, username)));
    }

    @Override
    public UserServiceModel updateUser(UserServiceModel toEdit) {
        User user = this.modelMapper.map(toEdit, User.class);
        User updated = this.userRepository.saveAndFlush(user);
        return this.modelMapper.map(updated, UserServiceModel.class);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);
        return user != null;
    }



    @Override
    public UserServiceModel findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessagesConstants.NOT_EXISTENT_USERNAME_MESSAGE, username)));
        return this.modelMapper.map(user,UserServiceModel.class);
    }

    private void giveRolesToRoot(User user) {
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ROOT"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_MODERATOR"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
    }


}
