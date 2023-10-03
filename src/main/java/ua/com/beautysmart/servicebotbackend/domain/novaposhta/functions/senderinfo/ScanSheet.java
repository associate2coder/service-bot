package ua.com.beautysmart.servicebotbackend.domain.novaposhta.functions.senderinfo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ScanSheet {

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
}
