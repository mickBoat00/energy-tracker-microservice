package com.meichel.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meichel.user_service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findBySub(String sub);

}
