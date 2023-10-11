package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TtnTrackingCustomDeserializer extends JsonDeserializer<TtnTracking> {

    @Override
    public TtnTracking deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 2023-09-30 23:59:59
        var thing = node.get(0);
        String dateTime = thing.get("DatePayedKeeping").asText();

        LocalDateTime paidStorageDateTime = LocalDateTime.parse(dateTime, formatter);

        return TtnTracking.builder()
                .firstDayPaidKeeping(paidStorageDateTime)
                .build();
    }
}
