package ua.com.beautysmart.servicebot.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.beautysmart.servicebot.domain.entities.AccessRequest;

import java.util.Optional;

/**
 * Author: associate2coder
 */

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    Optional<AccessRequest> findByUserId(long userId);
    Optional<AccessRequest> findByPhoneNumber(String phone);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUserId(long userId);

}
