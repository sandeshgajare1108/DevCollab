package com.DevCollab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.GitRepositoryEntity;

@Repository
public interface GitRepositoryRepository
        extends JpaRepository<GitRepositoryEntity, Long> {

    Optional<GitRepositoryEntity>
    findByProjectId(Long projectId);

    boolean existsByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);
}