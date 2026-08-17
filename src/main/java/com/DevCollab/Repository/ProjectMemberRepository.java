package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.ProjectMemberEntity;

@Repository
public interface ProjectMemberRepository
        extends JpaRepository<ProjectMemberEntity, Long> {

    List<ProjectMemberEntity> findByProjectId(Long projectId);

    List<ProjectMemberEntity> findByUserId(Long userId);

    boolean existsByProjectIdAndUserId(
            Long projectId,
            Long userId
    );

    void deleteByProjectId(Long projectId);

    // Dashboard role counts
    long countByMemberRole(
            ProjectMemberEntity.MemberRole memberRole
    );

    // Project member count
    long countByProjectId(Long projectId);

    // User project count
    long countByUserId(Long userId);

    // Duplicate validation during update
    boolean existsByProjectIdAndUserIdAndProjectMemberIdNot(
            Long projectId,
            Long userId,
            Long projectMemberId
    );
    boolean existsByProjectIdAndUserIdAndMemberRole(
            Long projectId,
            Long userId,
            ProjectMemberEntity.MemberRole memberRole
    );
    
    
}