package ua.com.beautysmart.servicebot.domain.services;

import ua.com.beautysmart.servicebot.domain.entities.User;

import java.util.List;

public interface UserService {

    User addUser(long chatId, String name, String role);
    User getUser(long chatId);
    List<User> getAllUsers();
    List<User> getAllAdmins();
    void removeUser(long chatId);

}
