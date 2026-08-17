package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.TaskEntity;

@Repository
public interface TaskRepository
        extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByProjectId(
            Long projectId
    );

    long countByStatus(String status);

    long countByProjectId(
            Long projectId
    );

    long countByProjectIdAndStatus(
            Long projectId,
            String status
    );
}