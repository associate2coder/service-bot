package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;

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

@Slf4j
public class TtnTrackingCustomDeserializer extends JsonDeserializer<TtnTracking> {

    @Override
    public TtnTracking deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);
        String[] datePayedKeeping = node.get("DatePayedKeeping").asText().split(" ");

        String[] dateString = datePayedKeeping[0].split("-");

        LocalDate date = LocalDate.of(
                parseInt(dateString[0]), //year
                parseInt(dateString[1]), // month
                parseInt(dateString[2])  // day
        );

        String[] timeString = datePayedKeeping[1].split(":");

        LocalTime time = LocalTime.of(
                parseInt(timeString[0]),
                parseInt(timeString[1]),
                parseInt(timeString[2])
        );
        return TtnTracking.builder()
                .firstDayPaidKeeping(LocalDateTime.of(date, time))
                .build();
    }

    int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return -1;
    }

}
