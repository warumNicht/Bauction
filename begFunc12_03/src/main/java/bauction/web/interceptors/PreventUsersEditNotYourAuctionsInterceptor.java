package bauction.web.interceptors;

import bauction.domain.entities.User;
import bauction.services.contracts.AuctionService;
import bauction.web.controllers.BaseController;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public PreventUsersEditNotYourAuctionsInterceptor(AuctionService auctionService, ModelMapper modelMapper) {
        this.auctionService = auctionService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User loggedInUser = this.modelMapper.map(super.getLoggedInUserService(),User.class);
        String loggedInUserId = loggedInUser.getId();
        List<String> loggedInUserAuthorities = loggedInUser
                .getAuthorities()
                .stream().map(a -> a.getAuthority())
                .collect(Collectors.toList());

        LinkedHashMap requestAttributes = (LinkedHashMap) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String auctionIdPathVar = (String)requestAttributes.get("id");
        String sellerId=this.auctionService.getAuctionSellerId(auctionIdPathVar);

        if(!loggedInUserId.equals(sellerId) && !loggedInUserAuthorities.contains("ROLE_MODERATOR")
        &&!loggedInUserAuthorities.contains("ROLE_ADMIN")&&!loggedInUserAuthorities.contains("ROLE_ROOT")){
            response.sendRedirect("/unauthorized");
            return false;
        }
        return true;
    }
}
