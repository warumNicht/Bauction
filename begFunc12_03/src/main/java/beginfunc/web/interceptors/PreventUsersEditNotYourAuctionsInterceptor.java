package beginfunc.web.interceptors;

import beginfunc.domain.entities.User;
import beginfunc.services.contracts.AuctionService;
import beginfunc.web.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PreventUsersEditNotYourAuctionsInterceptor extends BaseController implements HandlerInterceptor {
    private final AuctionService auctionService;

    @Autowired
    public PreventUsersEditNotYourAuctionsInterceptor(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User loggedInUser = super.getLoggedInUser();
        String loggedInUserId = loggedInUser.getId();
        List<String> loggedInUserAuthorities = loggedInUser
                .getAuthorities()
                .stream().map(a -> a.getAuthority())
                .collect(Collectors.toList());

        LinkedHashMap requestAttributes = (LinkedHashMap) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String auctionIdPathVar = (String)requestAttributes.get("id");
        String sellerId=this.auctionService.getAuctionSellerId(auctionIdPathVar);

        if(!loggedInUserId.equals(sellerId) && !loggedInUserAuthorities.contains("ROLE_MODERATOR")){
            response.sendRedirect("/unauthorized");
            return false;
        }
        return true;
    }
}
