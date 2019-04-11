package bauction.config;

import bauction.domain.entities.auctionRelated.Auction;
import bauction.domain.entities.productRelated.products.BankNote;
import bauction.domain.entities.productRelated.products.BaseProduct;
import bauction.domain.entities.productRelated.products.Coin;
import bauction.domain.models.serviceModels.AuctionServiceModel;
import bauction.domain.models.serviceModels.products.BanknoteServiceModel;
import bauction.domain.models.serviceModels.products.BaseProductServiceModel;
import bauction.domain.models.serviceModels.products.CoinServiceModel;
import bauction.services.contracts.EmailService;
import bauction.services.EmailServiceImpl;
import bauction.web.filters.LoggedInUserFilter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppBeansConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        this.configureMapper(modelMapper);
        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EmailService emailUtil(){
        return new EmailServiceImpl();
    }

    @Bean
    public FilterRegistrationBean<LoggedInUserFilter> loggingFilter(){
        FilterRegistrationBean<LoggedInUserFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LoggedInUserFilter());
        registrationBean.addUrlPatterns("/users/login");
        registrationBean.addUrlPatterns("/");
        registrationBean.addUrlPatterns("/users/register");
        return registrationBean;
    }


    private void configureMapper(ModelMapper modelMapper) {
        this.addMappingFromEntityProductToServiceProduct(modelMapper);
        this.addMappingFromServiceProductToEntityProduct(modelMapper);
    }

    private void addMappingFromServiceProductToEntityProduct(ModelMapper modelMapper) {
        Converter<BaseProductServiceModel, BaseProduct> converter = new AbstractConverter<BaseProductServiceModel, BaseProduct>() {
            private  ModelMapper modelMapper=new ModelMapper();

            @Override
            protected BaseProduct convert(BaseProductServiceModel baseProduct) {
                if(baseProduct instanceof CoinServiceModel){
                    return this.modelMapper.map(baseProduct, Coin.class);
                }else if(baseProduct instanceof BanknoteServiceModel){
                    return this.modelMapper.map(baseProduct,BankNote.class);
                }
                return this.modelMapper.map(baseProduct,BaseProduct.class);
            }
        };
        modelMapper.addConverter(converter);
        PropertyMap<AuctionServiceModel, Auction> map = new PropertyMap<AuctionServiceModel, Auction>() {
            @Override
            protected void configure() {
                using(converter).map(source.getProduct()).setProduct(null);
            }
        };
        modelMapper.addMappings(map);
    }

    private void addMappingFromEntityProductToServiceProduct(ModelMapper modelMapper) {
        Converter<BaseProduct, BaseProductServiceModel> converter = new AbstractConverter<BaseProduct, BaseProductServiceModel>() {
            private  ModelMapper modelMapper=new ModelMapper();

            @Override
            protected BaseProductServiceModel convert(BaseProduct baseProduct) {
                if(baseProduct instanceof Coin){
                    return this.modelMapper.map(baseProduct, CoinServiceModel.class);
                }else if(baseProduct instanceof BankNote){
                    return this.modelMapper.map(baseProduct,BanknoteServiceModel.class);
                }
                return this.modelMapper.map(baseProduct,BaseProductServiceModel.class);
            }
        };
        modelMapper.addConverter(converter);
        PropertyMap<Auction, AuctionServiceModel> map = new PropertyMap<Auction, AuctionServiceModel>() {
            @Override
            protected void configure() {
                using(converter).map(source.getProduct()).setProduct(null);
            }
        };
        modelMapper.addMappings(map);
    }

}
