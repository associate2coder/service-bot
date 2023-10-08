package ua.com.beautysmart.servicebot.domain.novaposhta.functions.senderinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SenderRequest {

    private String phone;
    private String apiKey;
    private String alias;

}
