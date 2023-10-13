package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Author: associate2coder
 */

public class TtnCustomDeserializer extends JsonDeserializer<TTN[]> {
    @Override
    public TTN[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode parentNode = p.getCodec().readTree(p);

        TTN[] result = new TTN[parentNode.size()];

        for (int i = 0; i < parentNode.size(); i++) {
            JsonNode node = parentNode.get(i);

            String Ref = node.get("Ref").asText();
            String IntDocNumber = node.get("IntDocNumber").asText();
            String InfoRegClientBarcodes = node.get("InfoRegClientBarcodes").asText();
            String Cost = node.get("Cost").asText();
            String StateId = node.get("StateId").asText();
            String BackwardDeliverySum = node.get("BackwardDeliverySum").asText();
            LocalDateTime DatePayedKeeping = null; // перший день платного зберігання "DatePayedKeeping"

            result[i] = TTN.builder()
                    .Ref(Ref)
                    .number(IntDocNumber)
                    .order(InfoRegClientBarcodes)
                    .declaredValue(Cost)
                    .status(StateId)
                    .cashOnDeliverAmount(BackwardDeliverySum)
                    .firstDayPaidKeeping(null)
                    .build();
        }

        return result;
    }
}
