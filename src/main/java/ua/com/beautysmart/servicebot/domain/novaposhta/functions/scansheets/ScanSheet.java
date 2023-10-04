package ua.com.beautysmart.servicebot.domain.novaposhta.functions.scansheets;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ScanSheet implements Comparable<ScanSheet> {

    @JsonAlias(value = "Ref")
    private String ref; // Ref реєстру згідно з даними Нової пошти
    @JsonAlias(value = "Number")
    private String number; // Номер реєстру для цілей пошуку чи друку штрих-коду
    @JsonAlias(value = "DateTime")
    private String dateTime; // Дата реєстру для цілей сортування
    @JsonAlias(value = "Printed")
    private String printed; // 0 - не роздрукований, 1 - роздрукований
    @JsonAlias(value = "Description")
    private String description; // Опис реєстру
    @JsonAlias(value = "MarketplacePartnerDescription")
    private String marketplacePartnerDescription; // Бізнес-кабінет
    @JsonAlias(value = "Count")
    private String count; // кількість накладних

    @Override
    public int compareTo(ScanSheet o) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 2023-09-30 23:59:59
        LocalDateTime timeOfThis = LocalDateTime.parse(dateTime, formatter);
        LocalDateTime timeOfOther = LocalDateTime.parse(o.getDateTime(), formatter);
        return timeOfThis.compareTo(timeOfOther);
    }
}
