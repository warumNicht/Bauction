package beginfunc.services;

import beginfunc.domain.entities.User;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.repositories.RoleRepository;
import beginfunc.repositories.UserRepository;
import beginfunc.services.contracts.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public boolean registerUser(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(this.encoder.encode(user.getPassword()));

        if(this.userRepository.count()==0){
            this.giveRolesToRoot(user);
        }else {
            user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
        }

        try {
            this.userRepository.saveAndFlush(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
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
    public UserServiceModel findUserById(Integer id) {
        User user = this.userRepository.findById(id).orElse(null);
        if(user==null){
            return null;
        }
        return this.modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    private void giveRolesToRoot(User user) {
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ROOT"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_ADMIN"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_MODERATOR"));
        user.getAuthorities().add(this.roleRepository.findByAuthority("ROLE_USER"));
    }


}
