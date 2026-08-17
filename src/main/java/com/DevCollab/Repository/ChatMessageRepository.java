package com.DevCollab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevCollab.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository
        extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity>
    findByProjectIdOrderBySentAtAsc(
            Long projectId
    );
}