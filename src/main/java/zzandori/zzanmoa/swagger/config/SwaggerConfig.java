package zzandori.zzanmoa.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket Market() {
        return new Docket(DocumentationType.OAS_30)
            .groupName("Market-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.market.controller"))
            .paths(PathSelectors.ant("/market/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket MarketPlace() {
        return new Docket(DocumentationType.OAS_30)
            .groupName("MarketPlace-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.marketplace.controller"))
            .paths(PathSelectors.ant("/marketplace/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket Item() {
        return new Docket(DocumentationType.OAS_30)
            .groupName("Item-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.item.controller"))
            .paths(PathSelectors.ant("/item/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket ThriftStore() {
        return new Docket(DocumentationType.OAS_30)
            .groupName("ThriftStore-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.thriftstore.controller"))
            .paths(PathSelectors.ant("/thriftstore/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket SavingPlace(){
        return new Docket(DocumentationType.OAS_30)
            .groupName("SavingPlace-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.savingplace.controller"))
            .paths(PathSelectors.ant("/savingplace/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket Bargain(){
        return new Docket(DocumentationType.OAS_30)
            .groupName("Bargain-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.bargain.controller"))
            .paths(PathSelectors.ant("/bargain/**"))
            .build()
            .apiInfo(apiInfo());
    }

    @Bean
    public Docket BargainBoard(){
        return new Docket(DocumentationType.OAS_30)
            .groupName("BargainBoard-api")
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("zzandori.zzanmoa.bargainboard.controller"))
            .paths(PathSelectors.ant("/bargain-board/**"))
            .build()
            .apiInfo(apiInfo());
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Your API Title")
            .description("Your API Description")
            .version("1.0.0")
            .build();
    }
}
