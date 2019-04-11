package bauction.config;

import bauction.web.interceptors.PreventUsersEditNotYourAuctionsInterceptor;
import bauction.web.interceptors.PreventSellerParticipateInHisAuctionsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final PreventSellerParticipateInHisAuctionsInterceptor preventSellerParticipateInHisAuctionsInterceptor;
    private final PreventUsersEditNotYourAuctionsInterceptor preventUsersEditNotYourAuctionsInterceptor;

    public InterceptorConfiguration(PreventSellerParticipateInHisAuctionsInterceptor preventSellerParticipateInHisAuctionsInterceptor, PreventUsersEditNotYourAuctionsInterceptor preventUsersEditNotYourAuctionsInterceptor) {
        this.preventSellerParticipateInHisAuctionsInterceptor = preventSellerParticipateInHisAuctionsInterceptor;
        this.preventUsersEditNotYourAuctionsInterceptor = preventUsersEditNotYourAuctionsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.preventSellerParticipateInHisAuctionsInterceptor)
                .addPathPatterns("/auctions/bidding/*","/auctions/offers/*");

        registry.addInterceptor(this.preventUsersEditNotYourAuctionsInterceptor)
                .addPathPatterns("/auctions/edit/*","/auctions/delete/*");
    }
}
