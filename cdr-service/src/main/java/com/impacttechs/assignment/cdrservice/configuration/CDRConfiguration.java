package com.impacttechs.assignment.cdrservice.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class CDRConfiguration {
    @Bean
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
        return  new RestTemplate();
    }

    @Primary
    @Bean
    public RestTemplate externalRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }
}
