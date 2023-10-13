package ua.com.beautysmart.servicebot.domain.novaposhta.functions.paidstorage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Author: associate2coder
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtnTracking {

    private LocalDateTime firstDayPaidKeeping; // перший день платного зберігання
    private LocalDate dateReturnCargo; // дата автоматичного повернення (якщо вона є, то немає платного зберігання)

}
