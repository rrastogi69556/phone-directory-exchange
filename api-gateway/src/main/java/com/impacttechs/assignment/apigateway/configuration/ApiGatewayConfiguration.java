package com.impacttechs.assignment.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ApiGatewayConfiguration {
    public static final String TENANT_UUID = "tenant-uuid";

   /* @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(urlPath -> urlPath.path("/phonebook-service/**")
                    .uri("lb://PHONEBOOK-SERVICE"))
                .route(urlPath -> urlPath.path("/cdr-service/**")
                        .filters(filter -> filter.filter(f -> f.getRequest().getHeaders().containsKey(TENANT_UUID)))
                        .uri("lb://CDR-SERVICE"))
                .build();
    }*/
}
