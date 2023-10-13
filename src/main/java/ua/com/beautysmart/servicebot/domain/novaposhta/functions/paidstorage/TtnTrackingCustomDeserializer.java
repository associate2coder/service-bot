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
import java.time.format.DateTimeFormatter;

/**
 * Author: associate2coder
 */

@Slf4j
public class TtnTrackingCustomDeserializer extends JsonDeserializer<TtnTracking> {

    @Override
    public TtnTracking deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 2023-09-30 23:59:59
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 2023-09-30

        var item = node.get(0);
        String dateTimeStorage = item.get("DatePayedKeeping").asText();
        String dateReturnCargo = item.get("DateReturnCargo").asText();

        LocalDateTime paidStorageDateTime = null;
        LocalDate returnCargoDate = null;
        if (!dateTimeStorage.isEmpty()) {
            paidStorageDateTime = LocalDateTime.parse(dateTimeStorage, dateTimeFormatter);
        }
        if (!dateReturnCargo.isEmpty()) {
            returnCargoDate = LocalDate.parse(dateReturnCargo, dateFormater);
        }
        return TtnTracking.builder()
                .firstDayPaidKeeping(paidStorageDateTime)
                .dateReturnCargo(returnCargoDate)
                .build();
    }
}
