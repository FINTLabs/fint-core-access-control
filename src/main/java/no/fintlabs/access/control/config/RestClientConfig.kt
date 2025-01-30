package no.fintlabs.access.control.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
open class RestClientConfig(
    @Value("\${fint.metamodel-url}")
    private val metamodelUrl: String
) {
    @Bean
    open fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(metamodelUrl)
            .build()
    }
}
