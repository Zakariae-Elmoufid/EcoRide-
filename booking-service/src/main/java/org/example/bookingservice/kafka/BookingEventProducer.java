package org.example.bookingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookingservice.dto.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Kafka producer — only wires up when KafkaTemplate is actually available.
 * In local-dev profile Kafka is excluded, so a no-op fallback is used instead.
 */
public class BookingEventProducer {
    public void publishBookingCreated(BookingCreatedEvent event) {}
}

@Configuration
class BookingEventProducerConfig {

    private static final Logger log = LoggerFactory.getLogger(BookingEventProducerConfig.class);

    /** Real producer — only when Kafka is on the classpath & configured */
    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    public BookingEventProducer realProducer(KafkaTemplate<String, String> kafkaTemplate,
                                             ObjectMapper objectMapper) {
        return new BookingEventProducer() {
            private static final String TOPIC = "booking-created-event";

            @Override
            public void publishBookingCreated(BookingCreatedEvent event) {
                try {
                    String payload = objectMapper.writeValueAsString(event);
                    kafkaTemplate.send(TOPIC, String.valueOf(event.getBookingId()), payload);
                    log.info("Published booking-created-event bookingId={} rideId={}",
                            event.getBookingId(), event.getRideId());
                } catch (Exception e) {
                    log.error("Failed to publish booking-created-event: {}", e.getMessage(), e);
                }
            }
        };
    }

    /** No-op producer — used in local-dev when Kafka is disabled */
    @Bean
    @ConditionalOnMissingBean(BookingEventProducer.class)
    public BookingEventProducer noOpProducer() {
        return new BookingEventProducer() {
            @Override
            public void publishBookingCreated(BookingCreatedEvent event) {
                log.info("[local-dev] Skipping Kafka publish for bookingId={}", event.getBookingId());
            }
        };
    }
}
