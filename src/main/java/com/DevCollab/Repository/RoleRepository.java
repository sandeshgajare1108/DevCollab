
package com.DevCollab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.RoleEntity;

@Repository
public interface RoleRepository
        extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(
            String roleName
    );
}