package beginfunc.web.controllers;

import beginfunc.domain.models.bindingModels.UserRegisterBindingModel;
import beginfunc.domain.models.serviceModels.users.UserServiceModel;
import beginfunc.domain.models.viewModels.users.UserProfileViewModel;
import beginfunc.domain.models.viewModels.users.UsersWaitingAuctionViewModel;
import beginfunc.services.contracts.AuctionService;
import beginfunc.services.contracts.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuctionService auctionService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, AuctionService auctionService, ModelMapper modelMapper) {
        this.userService = userService;
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerUser(ModelAndView modelAndView,
                                     @ModelAttribute(name = "userRegisterModel") UserRegisterBindingModel model){
        modelAndView.addObject("userRegisterModel",model);
        modelAndView.setViewName("user/register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUserPost(@Valid @ModelAttribute(name = "userRegisterModel") UserRegisterBindingModel model,
                                         BindingResult bindingResult, ModelAndView modelAndView){
        if(bindingResult.hasErrors()){
            modelAndView.addObject("userRegisterModel",model);
            modelAndView.setViewName("user/register");
            return modelAndView;
        }

        if(!model.getPassword().equals(model.getConfirmPassword())){
            throw new IllegalArgumentException("Not matching passwords!");
        }
        UserServiceModel userToRegister = this.modelMapper.map(model, UserServiceModel.class);
        userToRegister.setRegistrationDate(new Date());

        if(!this.userService.registerUser(userToRegister)){
            throw new IllegalArgumentException("User registration failed!");
        }
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginUser(ModelAndView modelAndView){
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable(name = "id") Integer  id,
                                @ModelAttribute("message") String emailMessage, ModelAndView modelAndView){
        UserServiceModel userById = this.userService.findUserById(id);
        UserProfileViewModel profileViewModel = this.modelMapper.map(userById, UserProfileViewModel.class);

        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        profileViewModel.setRegistrationDate(format.format(userById.getRegistrationDate()));
        modelAndView.addObject("userProfile",profileViewModel);
        modelAndView.addObject("message", emailMessage);
        modelAndView.setViewName("user/user-profile");
        return modelAndView;
    }

    @GetMapping("/{id}/auctions/waiting")
    public ModelAndView viewWaitingAuctions(@PathVariable(name = "id") Integer  id,ModelAndView modelAndView){

        List<UsersWaitingAuctionViewModel> waitingAuctionViewModels = this.auctionService.getWaitingAuctionsOfUser(id).stream()
                .map(a -> {
                    UsersWaitingAuctionViewModel model = this.modelMapper.map(a, UsersWaitingAuctionViewModel.class);
                    model.setName(a.getProduct().getName());
                    return model;
                })
                .collect(Collectors.toList());
        modelAndView.addObject("waitingAuctions", waitingAuctionViewModels);
        modelAndView.setViewName("user/waiting-auctions");
        return modelAndView;
    }
}
