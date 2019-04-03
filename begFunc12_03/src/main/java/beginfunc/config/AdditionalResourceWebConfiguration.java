package beginfunc.config;

import beginfunc.constants.AppConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + AppConstants.UPLOAD_FOLDER_PATH + "/");
    }
}
