package org.example.bookingservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookingservice.dto.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {

    private static final Logger log = LoggerFactory.getLogger(BookingEventProducer.class);
    private static final String TOPIC = "booking-created-event";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BookingEventProducer(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishBookingCreated(BookingCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, String.valueOf(event.getBookingId()), payload);
            log.info("Published booking-created-event for bookingId={}, rideId={}",
                    event.getBookingId(), event.getRideId());
        } catch (Exception e) {
            log.error("Failed to publish booking-created-event: {}", e.getMessage(), e);
        }
    }
}

