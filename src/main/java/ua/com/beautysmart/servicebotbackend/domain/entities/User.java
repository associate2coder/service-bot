package ua.com.beautysmart.servicebotbackend.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import ua.com.beautysmart.servicebotbackend.domain.bot.common.Role;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Data
public class User {
    @Id
    private long chatId;
    private String name;
    @Enumerated
    private Role role;

}
