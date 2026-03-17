package org.project.rideservice.config;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Feign to use Spring's HttpMessageConverters for decoding.
 *
 * Spring Boot 4.x uses Jackson 3.x (tools.jackson package).
 * Feign's default JacksonDecoder uses Jackson 2.x (com.fasterxml.jackson),
 * causing deserialization failures. SpringDecoder wraps the Boot-managed
 * converters which already use Jackson 3.x correctly.
 */
@Configuration
public class FeignDecoderConfig {

    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringDecoder(messageConverters);
    }
}
