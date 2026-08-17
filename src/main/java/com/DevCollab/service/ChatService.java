 package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.ChatMessageRepository;
import com.DevCollab.Repository.ProjectMemberRepository;
import com.DevCollab.entity.ChatMessageEntity;

@Service
public class ChatService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private ProjectMemberRepository projectMemberRepository;

	public ChatMessageEntity saveMessage(Long projectId, Long senderId, String message) {

		if (projectId == null) {

			throw new RuntimeException("Project ID is required");
		}

		if (senderId == null) {

			throw new RuntimeException("Sender ID is required");
		}

		if (message == null || message.trim().isEmpty()) {

			throw new RuntimeException("Message is required");
		}

		ChatMessageEntity chat = new ChatMessageEntity();

		chat.setProjectId(projectId);

		chat.setSenderId(senderId);

		chat.setMessageText(message.trim());

		chat.setSentAt(new Timestamp(System.currentTimeMillis()));

		return chatMessageRepository.save(chat);
	}

	public List<ChatMessageEntity>
    getProjectMessages(
            Long projectId) {

        return chatMessageRepository
                .findByProjectIdOrderBySentAtAsc(
                        projectId
                );
    }
	public boolean canAccessProject(
	        Long projectId,
	        Long userId) {

	    if (projectId == null ||
	        userId == null) {

	        return false;
	    }

	    return projectMemberRepository
	            .existsByProjectIdAndUserId(
	                    projectId,
	                    userId
	            );
	}
	
}
