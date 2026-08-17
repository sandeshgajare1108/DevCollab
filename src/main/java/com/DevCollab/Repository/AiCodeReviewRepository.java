package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.AiCodeReviewEntity;

@Repository
public interface AiCodeReviewRepository
        extends JpaRepository<AiCodeReviewEntity, Long> {

    List<AiCodeReviewEntity>
    findByPullRequestIdOrderByCreatedAtDesc(
            Long pullRequestId
    );

    List<AiCodeReviewEntity>
    findByTaskIdOrderByCreatedAtDesc(
            Long taskId
    );

    List<AiCodeReviewEntity>
    findBySubmittedByOrderByCreatedAtDesc(
            Long submittedBy
    );
}