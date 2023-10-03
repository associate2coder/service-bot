package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.paidstorage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtnTracking {

    private LocalDateTime firstDayPaidKeeping; // перший день платного зберігання

}
