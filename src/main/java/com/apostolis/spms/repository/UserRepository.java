package com.apostolis.spms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apostolis.spms.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
