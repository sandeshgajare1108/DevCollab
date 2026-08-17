package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.CodeReviewEntity;

@Repository
public interface CodeReviewRepository
        extends JpaRepository<CodeReviewEntity, Long> {

    List<CodeReviewEntity>
    findByPullRequestIdOrderByCreatedAtDesc(
            Long pullRequestId
    );


    List<CodeReviewEntity>
    findByProjectIdOrderByCreatedAtDesc(
            Long projectId
    );


    List<CodeReviewEntity>
    findByTaskIdOrderByCreatedAtDesc(
            Long taskId
    );
    
    //dvsdfg
}