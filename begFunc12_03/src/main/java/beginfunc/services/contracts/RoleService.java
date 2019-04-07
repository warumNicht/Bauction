package beginfunc.services.contracts;

import beginfunc.domain.models.serviceModels.users.RoleServiceModel;

public interface RoleService {
    RoleServiceModel findByAuthority(String authority);
}
