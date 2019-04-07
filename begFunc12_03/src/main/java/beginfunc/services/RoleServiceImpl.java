package beginfunc.services;

import beginfunc.domain.entities.Role;
import beginfunc.domain.models.serviceModels.users.RoleServiceModel;
import beginfunc.repositories.RoleRepository;
import beginfunc.services.contracts.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        Role role = this.roleRepository.findByAuthority(authority);
        return this.modelMapper.map(role,RoleServiceModel.class);
    }
}
