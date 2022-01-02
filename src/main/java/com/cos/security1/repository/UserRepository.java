package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//JPARepository가 기본적으로 CRUD 메서드를 들고있다
//@Repository 어노테이션 없이 IoC가 된다. 상속받았으므로
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username); //JPA query method
}
