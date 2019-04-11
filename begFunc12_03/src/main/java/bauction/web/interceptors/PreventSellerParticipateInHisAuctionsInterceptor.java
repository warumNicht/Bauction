package bauction.web.interceptors;

import bauction.services.contracts.AuctionService;
import bauction.web.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Component
public class PreventSellerParticipateInHisAuctionsInterceptor extends BaseController implements HandlerInterceptor {

    private final AuctionService auctionService;

    @Autowired
    public PreventSellerParticipateInHisAuctionsInterceptor(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String loggedInUserId = super.getLoggedInUserId();
        LinkedHashMap requestAttributes = (LinkedHashMap) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String auctionId = (String)requestAttributes.get("id");
        String sellerId=this.auctionService.getAuctionSellerId(auctionId);

        if(loggedInUserId.equals(sellerId)){
            response.sendRedirect("/unauthorized");
            return false;
        }
        return true;
    }



}
