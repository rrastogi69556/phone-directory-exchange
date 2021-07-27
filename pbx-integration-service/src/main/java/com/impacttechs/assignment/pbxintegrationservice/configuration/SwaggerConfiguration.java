package com.impacttechs.assignment.pbxintegrationservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final String PACKAGE_NAME = "com.impacttechs.assignment.pbx-integration-service";

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(PACKAGE_NAME))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "CDR API",
                "This application contains endpoints for creating/updating/fetching call data records operations from PBX exchange",
                "1.0.0",
                "TERMS OF SERVICE URL",
                new Contact("roopanshu.rastogi", "", "rastogi.roopanshu@gmail.com"),
                "Sample License",
                "LICENSE URL"
        );
    }
}

