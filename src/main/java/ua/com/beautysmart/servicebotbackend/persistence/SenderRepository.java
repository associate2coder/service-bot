package ua.com.beautysmart.servicebotbackend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.beautysmart.servicebotbackend.domain.entities.Sender;

public interface SenderRepository extends JpaRepository<Sender, Long> {
}
