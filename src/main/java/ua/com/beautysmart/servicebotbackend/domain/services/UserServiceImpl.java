package ua.com.beautysmart.servicebotbackend.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.beautysmart.servicebotbackend.domain.bot.common.Role;
import ua.com.beautysmart.servicebotbackend.domain.entities.User;
import ua.com.beautysmart.servicebotbackend.persistence.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

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
