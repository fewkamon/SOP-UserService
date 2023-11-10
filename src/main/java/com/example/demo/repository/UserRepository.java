package com.example.demo.repository;

import com.example.demo.pojo.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{email: '?0'}")
    public User findUserWithEmail(String email);

    @Query(value = "{email: '?0', password: '?1'}")
    public User checkLogin(String email, String password);
}
