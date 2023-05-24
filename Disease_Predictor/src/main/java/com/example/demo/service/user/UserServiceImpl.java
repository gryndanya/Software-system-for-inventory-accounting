package com.example.demo.service.user;

import com.example.demo.model.Category;
import com.example.demo.model.Stock;
import com.example.demo.model.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return user;
    }

    @Override
    public void deleteAllUser() {
        this.userRepository.deleteAll();
    }

    @Override
    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public List<User> search(String keyword) {
        return this.userRepository.search(keyword);
    }
}
