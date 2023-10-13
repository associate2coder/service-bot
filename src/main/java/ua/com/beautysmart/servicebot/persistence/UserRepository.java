package ua.com.beautysmart.servicebot.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.beautysmart.servicebot.domain.entities.User;

/**
 * Author: associate2coder
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
