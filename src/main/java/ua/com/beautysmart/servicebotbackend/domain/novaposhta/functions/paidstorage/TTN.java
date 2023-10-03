package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TTN {

    private String Ref;
    private String number;
    private String order; // номер замовлення хорошоп
    private String declaredValue; // оголошена вартість
    private String status; // 7 -> прибув у відділення
    private String cashOnDeliverAmount; // сума накладеного платежу
    private LocalDateTime firstDayPaidKeeping; // перший день платного зберігання

}
