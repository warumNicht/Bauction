package beginfunc.domain.models.bindingModels;

import javax.validation.constraints.*;

public class UserRegisterBindingModel {
    @NotNull
    @NotEmpty
    @Size(min = 3,max = 50)
    @Pattern(regexp = "^(?=.*\\S).+$", message = "Username of whitespaces is not valid!")
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 5,max = 80)
    @Pattern(regexp = "^(?=.*\\S).+$", message = "Full name of whitespaces is not valid!")
    private String fullName;

    @NotNull
    @NotEmpty
    @Size(min = 4,message = "Password require at least 4 characters!")
    @Pattern(regexp = "^(?=.*[a-z]).+$", message = "Password require at least 1 lowercase letter!")
    @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "Password require at least 1 uppercase letter!")
    @Pattern(regexp = "^(?=.*\\d).+$", message = "Password require at least 1 digit!")
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 4,message = "Password require at least 4 characters!")
    @Pattern(regexp = "^(?=.*[a-z]).+$", message = "Password require at least 1 lowercase letter!")
    @Pattern(regexp = "^(?=.*[A-Z]).+$", message = "Password require at least 1 uppercase letter!")
    @Pattern(regexp = "^(?=.*\\d).+$", message = "Password require at least 1 digit!")
    private String confirmPassword;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    public UserRegisterBindingModel() {
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
        this.fullName = fullName.replaceAll("\\s+"," ").trim();;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
