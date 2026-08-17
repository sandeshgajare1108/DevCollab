package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.BugEntity;

@Repository
public interface BugRepository
        extends JpaRepository<BugEntity, Long> {

     default List<BugEntity>
    findByProjectIdOrderByCreatedAtDesc(
            Long projectId
    ) {
		// TODO Auto-generated method stub
		return null;
	}

    List<BugEntity>
    findByTaskIdOrderByCreatedAtDesc(
            Long taskId
    );

    List<BugEntity>
    findByPullRequestIdOrderByCreatedAtDesc(
            Long pullRequestId
    );

    List<BugEntity>
    findByAssignedToOrderByCreatedAtDesc(
            Long assignedTo
    );

    List<BugEntity>
    findByStatusOrderByCreatedAtDesc(
            String status
    );
    long countByTaskIdAndStatus(
            Long taskId,
            String status
    );
    
}