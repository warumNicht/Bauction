package beginfunc.config;

import beginfunc.web.interceptors.EditDeleteAuctionInterceptor;
import beginfunc.web.interceptors.PreventSellerParticipateInHisAuctionsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final PreventSellerParticipateInHisAuctionsInterceptor preventSellerParticipateInHisAuctionsInterceptor;
    private final EditDeleteAuctionInterceptor editDeleteAuctionInterceptor;

    public InterceptorConfiguration(PreventSellerParticipateInHisAuctionsInterceptor preventSellerParticipateInHisAuctionsInterceptor, EditDeleteAuctionInterceptor editDeleteAuctionInterceptor) {
        this.preventSellerParticipateInHisAuctionsInterceptor = preventSellerParticipateInHisAuctionsInterceptor;
        this.editDeleteAuctionInterceptor = editDeleteAuctionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.preventSellerParticipateInHisAuctionsInterceptor)
                .addPathPatterns("/auctions/bidding/*","/auctions/offers/*");

        registry.addInterceptor(this.editDeleteAuctionInterceptor)
                .addPathPatterns("/auctions/edit/*","/auctions/delete/*");
    }
}
