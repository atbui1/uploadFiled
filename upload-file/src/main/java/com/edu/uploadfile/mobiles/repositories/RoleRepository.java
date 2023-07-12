package com.edu.uploadfile.mobiles.repositories;

import com.edu.uploadfile.mobiles.models.ERole;
import com.edu.uploadfile.mobiles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(ERole roleName);
}
