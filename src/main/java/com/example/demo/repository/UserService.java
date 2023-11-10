package com.example.demo.repository;

import com.example.demo.pojo.Users;
import com.example.demo.pojo.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Users getAllWizards() {
        Users users = new Users();
        users.users = (ArrayList<User>) this.repository.findAll();
        return users;
    }
    public User findUserWithEmail(String email) {
        return this.repository.findUserWithEmail(email);
    }
    public User CheckLogin(String email, String password) {
        return this.repository.checkLogin(email, password);
    }

    public Optional<User> Me(String id) {
        return this.repository.findById(id);
    }

    public void addUser(User wz) {
        repository.insert(wz);
    }

    public void updateUser(User wz) {
        repository.save(wz);
    }

    public void deleteUser(String _id) {
        repository.deleteById(_id);
    }
}
