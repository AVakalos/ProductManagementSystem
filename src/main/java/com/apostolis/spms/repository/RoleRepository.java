package com.apostolis.spms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apostolis.spms.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(String name);
}
