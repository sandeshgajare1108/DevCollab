package com.DevCollab.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.PullRequestEntity;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequestEntity, Long> {

	List<PullRequestEntity> findByProjectIdOrderByCreatedAtDesc(Long projectId);

	List<PullRequestEntity> findByTaskIdOrderByCreatedAtDesc(Long taskId);

	Optional<PullRequestEntity> findByProjectIdAndGithubPrNumber(Long projectId, Long githubPrNumber);

	boolean existsByProjectIdAndGithubPrNumber(Long projectId, Long githubPrNumber);

	List<PullRequestEntity> findByStatus(String status);

	long countByProjectIdAndTaskIdIsNotNull(Long projectId);

	long countByProjectIdAndStatusAndTaskIdIsNotNull(Long projectId, String status);
}