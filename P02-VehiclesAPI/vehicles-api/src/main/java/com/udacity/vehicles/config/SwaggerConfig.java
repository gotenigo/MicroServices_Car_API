package com.udacity.vehicles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


/*
*Swagger also provides some default values in its response that you can customize, such as “Api Documentation”,
 “Created by Contact Email”, “Apache 2.0”. To change these values, you can use the apiInfo(ApiInfo apiInfo) method.
*/
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        // .useDefaultResponseMessages(false);
    }


    //Swagger UI via http://localhost:8080/swagger-ui.html
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Location API",
                "This API returns a list of car + price.",
                "1.0",
                "http://www.LeGG.com/ggHome",
                new Contact("Gothard GOTENI", "www.LeGG.com", "myemail@gg.com"),
                "License of API", "http://www.LeGG.com/license", Collections.emptyList());
    }




}
