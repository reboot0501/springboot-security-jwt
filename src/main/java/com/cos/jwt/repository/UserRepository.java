package com.cos.jwt.repository;

import com.cos.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IOC가 됨, 이유는 JpaRepository를 상속했기 때문에 .....
public interface UserRepository extends JpaRepository<User, Integer > {
    public User findByUsername(String username);
}
