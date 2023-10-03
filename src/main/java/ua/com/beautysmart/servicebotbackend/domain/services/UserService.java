package ua.com.beautysmart.servicebotbackend.domain.services;

import ua.com.beautysmart.servicebotbackend.domain.bot.common.Role;
import ua.com.beautysmart.servicebotbackend.domain.entities.User;

import java.util.List;

public interface UserService {

    User addUser(long chatId, String name, String role);
    User getUser(long chatId);
    List<User> getAllUsers();
    List<User> getAllAdmins();
    void removeUser(long chatId);

}
