package bauction.web.controllers;

import bauction.constants.ErrorMessagesConstants;
import bauction.domain.entities.User;
import bauction.domain.models.serviceModels.users.UserServiceModel;
import bauction.error.AuctionNotFoundException;
import bauction.error.NoLoggedUserException;
import bauction.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {
    @Autowired
    private UserService userService;



    @ExceptionHandler({AuctionNotFoundException.class})
    public ModelAndView handleProductNotFound(AuctionNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }

    @ExceptionHandler({NoLoggedUserException.class})
    public ModelAndView handleNoLoggedUser(NoLoggedUserException e) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }

    protected String getLoggedInUserId() {
        Authentication authentication = this.getAuthentication();
        if(authentication==null){
            return null;
        }
        User authenticated = (User) authentication.getPrincipal();
        return authenticated.getId();
    }

    protected String getLoggedInUsername() {
        Authentication authentication = this.getAuthentication();
        User authenticated = (User) authentication.getPrincipal();
        return authenticated.getUsername();
    }

    protected User getLoggedInUser(){
        Authentication authentication = this.getAuthentication();
        Object principal = authentication.getPrincipal();
        User authenticated = (User) authentication.getPrincipal();
        return authenticated;
    }

    protected UserServiceModel getLoggedInUserService(){
        Authentication authentication = this.getAuthentication();
        String name = authentication.getName();
        return this.userService.findUserByUsername(name);

    }

    protected User getLoggedInUserByUsername(){
        Authentication authentication = this.getAuthentication();
        Object principal = authentication.getPrincipal();
        User authenticated = (User) authentication.getPrincipal();
        return authenticated;
    }

    private Authentication getAuthentication() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||authentication instanceof AnonymousAuthenticationToken) {
            throw new NoLoggedUserException(ErrorMessagesConstants.NO_LOGGED_IN_USER_MESSAGE);
        }
        return authentication;
    }


    protected String getLoggedInByUsername() {
        Authentication authentication = this.getAuthentication();
        String name = authentication.getName();
        return name;
    }
}
