package org.project.rideservice.config;

import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;

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
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }
}