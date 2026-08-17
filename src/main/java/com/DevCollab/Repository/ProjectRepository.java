package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.ProjectEntity;

@Repository
public interface ProjectRepository 
        extends JpaRepository<ProjectEntity, Long> {

    // Find projects by owner
    List<ProjectEntity> findByOwnerId(Long ownerId);

    // Find projects by status
    List<ProjectEntity> findByStatus(String status);

    // Find projects by visibility
    List<ProjectEntity> findByVisibility(String visibility);

    // Find projects by owner and status
    List<ProjectEntity> findByOwnerIdAndStatus(
            Long ownerId, String status);

    // Search project by name
    List<ProjectEntity> findByProjectNameContaining(
            String projectName);

    // Count projects by status
    long countByStatus(String status);

    // Count projects by owner
    long countByOwnerId(Long ownerId);
}