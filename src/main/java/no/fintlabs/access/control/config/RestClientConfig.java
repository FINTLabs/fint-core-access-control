package no.fintlabs.access.control.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${fint.metamodel-url}")
    private String metamodelUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(metamodelUrl)
                .build();
    }

}
