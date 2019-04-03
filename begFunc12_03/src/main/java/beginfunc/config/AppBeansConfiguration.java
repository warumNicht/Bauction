package beginfunc.config;

import beginfunc.util.EmailUtil;
import beginfunc.util.EmailUtilImpl;
import beginfunc.util.ImageStorageUtil;
import beginfunc.util.ImageStorageUtilImpl;
import beginfunc.web.filters.LoggedInUserFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppBeansConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EmailUtil emailUtil(){
        return new EmailUtilImpl();
    }

    @Bean
    @Autowired
    public ImageStorageUtil imageStorageUtil(ModelMapper modelMapper){
        return new ImageStorageUtilImpl(modelMapper);
    }


    @Bean
    public FilterRegistrationBean<LoggedInUserFilter> loggingFilter(){
        FilterRegistrationBean<LoggedInUserFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LoggedInUserFilter());
        registrationBean.addUrlPatterns("/login");
        registrationBean.addUrlPatterns("/");
        registrationBean.addUrlPatterns("/register");

        return registrationBean;
    }

}
