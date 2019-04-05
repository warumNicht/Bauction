package beginfunc.web.controllers;

import beginfunc.domain.entities.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

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
        if(authentication==null){
            return null;
        }
        User authenticated = (User) authentication.getPrincipal();
        return authenticated.getUsername();
    }

    protected User getLoggedInUser(){
        Authentication authentication = this.getAuthentication();
        if(authentication==null){
            return null;
        }
        User authenticated = (User) authentication.getPrincipal();
        return authenticated;
    }

    private Authentication getAuthentication() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication;
    }


}
