package ua.com.beautysmart.servicebot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "senders")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Data
public class Sender {

    @Id
    @GeneratedValue
    private long id;
    private String phone;
    private String alias;
    private String apiKey;
    private String Ref;
    private String fullName;
}
