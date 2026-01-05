package com.bazarya.repository;

import com.bazarya.domain.entities.Role;
import com.bazarya.domain.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
