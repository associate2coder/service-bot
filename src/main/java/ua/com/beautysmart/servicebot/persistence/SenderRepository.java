package ua.com.beautysmart.servicebot.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.beautysmart.servicebot.domain.entities.Sender;

import java.util.Optional;

/**
 * Author: associate2coder
 */

public interface SenderRepository extends JpaRepository<Sender, Long> {
    Optional<Sender> findByPhone(String phone);
}
