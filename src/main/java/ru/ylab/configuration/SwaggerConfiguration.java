package ru.ylab.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Swagger генерация
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(Collections.singleton("application/json"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.ylab.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wallet Service")
                .description("Player can Get or Put money from self wallet.")
                .version("1.0.4")
                .contact(new Contact(
                        "Bannov Daniil",
                        "https://github.com/bannovdaniil/",
                        "baddan@mail.ru"))
                .build();
    }
}