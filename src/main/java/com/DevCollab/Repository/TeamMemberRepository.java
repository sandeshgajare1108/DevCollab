
package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.TeamMemberEntity;

@Repository
public interface TeamMemberRepository
        extends JpaRepository<TeamMemberEntity, Long> {

    List<TeamMemberEntity> findByProjectId(Long projectId);

    List<TeamMemberEntity> findByUserId(Long userId);

    boolean existsByProjectIdAndUserId(
            Long projectId,
            Long userId
    );

    void deleteByProjectIdAndUserId(
            Long projectId,
            Long userId
    );
}
