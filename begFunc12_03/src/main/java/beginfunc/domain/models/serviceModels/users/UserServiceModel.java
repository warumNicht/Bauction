package beginfunc.domain.models.serviceModels.users;

import beginfunc.domain.models.serviceModels.BaseServiceModel;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {
    private String username;
    private String fullName;
    private String password;
    private String email;
    private Date registrationDate;
    private Set<RoleServiceModel> authorities;

    public UserServiceModel() {
        this.authorities=new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<RoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleServiceModel> authorities) {
        this.authorities = authorities;
    }
}
