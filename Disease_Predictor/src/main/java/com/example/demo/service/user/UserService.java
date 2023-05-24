package com.example.demo.service.user;

import com.example.demo.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUser();
    void saveUser(User user);
    User getUserById(long id);
    void deleteAllUser();
    void deleteUserById(long id);

    List<User> search(String keyword);
}
