package bauction.services.contracts;

import bauction.domain.models.serviceModels.users.RoleServiceModel;

public interface RoleService {
    RoleServiceModel findByAuthority(String authority);
}
