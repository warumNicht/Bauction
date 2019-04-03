package beginfunc.domain.models.serviceModels.users;

import beginfunc.domain.models.serviceModels.BaseServiceModel;

public class RoleServiceModel extends BaseServiceModel {

    private String authority;


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
