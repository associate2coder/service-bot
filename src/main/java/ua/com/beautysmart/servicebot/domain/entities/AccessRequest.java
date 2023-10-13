package ua.com.beautysmart.servicebot.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: associate2coder
 */

@Entity
@Table(name = "applications")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccessRequest {

    @Id
    @GeneratedValue
    private long id;
    private long userId;
    private String phoneNumber;
    private String name;
    private String username;

}
