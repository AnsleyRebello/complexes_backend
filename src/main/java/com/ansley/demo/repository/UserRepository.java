package com.ansley.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ansley.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

//List<User> findByLastName(String lastName);
//User findByUsernameAndPassword(String username, String password);
//List<User> findByAgeGreaterThan(int age);
//
//SELECT * FROM users WHERE last_name = ?;
//SELECT * FROM users WHERE username = ? AND password = ?;
//SELECT * FROM users WHERE age > ?;

// CRUD = create , r , update , delete