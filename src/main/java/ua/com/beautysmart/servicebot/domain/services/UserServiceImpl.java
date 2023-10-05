package ua.com.beautysmart.servicebot.domain.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebot.domain.bot.common.Role;
import ua.com.beautysmart.servicebot.domain.entities.User;
import ua.com.beautysmart.servicebot.persistence.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    @Value(value = "${super-user-chat-id}")
    private long superAdmin;

    @Override
    public User addUser(long chatId, String name, String roleString) {

        Role role = switch (roleString) {
            case "admin" -> Role.ADMIN;
            case "user" -> Role.USER;
            default -> Role.NONE;
        };

        User user = User.builder()
                .chatId(chatId)
                .name(name)
                .role(role)
                .build();
        return userRepo.save(user);
    }

    @PostConstruct
    private void init() {
        addUser(superAdmin, "admin", "admin");
    }



    @Override
    public User getUser(long chatId) {
        Optional<User> userSearch = userRepo.findById(chatId);
        return userSearch.orElseGet(() -> addUser(chatId, "unnamed", "none"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(usr -> usr.getRole().equals(Role.ADMIN) || usr.getRole().equals(Role.USER))
                .toList();
    }

    @Override
    public List<User> getAllAdmins() {
        return userRepo.findAll().stream()
                .filter(usr -> usr.getRole().equals(Role.ADMIN))
                .toList();
    }

    @Override
    public void removeUser(long chatId) {
        if (userRepo.existsById(chatId)) {
            userRepo.deleteById(chatId);
        }
    }
}
