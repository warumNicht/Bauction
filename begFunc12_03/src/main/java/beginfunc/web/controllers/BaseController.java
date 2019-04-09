package beginfunc.web.controllers;

import beginfunc.constants.ErrorMessagesConstants;
import beginfunc.domain.entities.User;
import beginfunc.error.AuctionNotFoundException;
import beginfunc.error.NoLoggedUserException;
import beginfunc.error.UserNotFoundException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

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


}
