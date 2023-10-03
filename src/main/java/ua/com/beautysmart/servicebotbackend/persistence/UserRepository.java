package ua.com.beautysmart.servicebotbackend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.beautysmart.servicebotbackend.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
