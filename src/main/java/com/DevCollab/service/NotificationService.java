
package com.DevCollab.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.NotificationRepository;
import com.DevCollab.entity.NotificationEntity;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	// =====================================================
	// GET ALL NOTIFICATIONS
	// =====================================================

	public List<NotificationEntity> getUserNotifications(Long userId) {

		return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	// =====================================================
	// GET UNREAD NOTIFICATIONS
	// =====================================================

	public List<NotificationEntity> getUnreadNotifications(Long userId) {

		return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
	}

	// =====================================================
	// COUNT UNREAD
	// =====================================================

	public long countUnread(Long userId) {

		return notificationRepository.countByUserIdAndReadFalse(userId);
	}

	// =====================================================
	// GET BY ID
	// =====================================================

	public Optional<NotificationEntity> getNotificationById(Long notificationId) {

		return notificationRepository.findById(notificationId);
	}

	// =====================================================
	// CREATE
	// =====================================================

	public NotificationEntity createNotification(Long userId, String title, String message, String type) {

		NotificationEntity notification = new NotificationEntity();

		notification.setUserId(userId);

		notification.setTitle(title);

		notification.setMessage(message);

		notification.setType(type == null || type.trim().isEmpty() ? "SYSTEM" : type);

		notification.setRead(false);

		return notificationRepository.save(notification);
	}

	// =====================================================
	// MARK AS READ
	// =====================================================

	public boolean markAsRead(Long notificationId) {

		Optional<NotificationEntity> optional = notificationRepository.findById(notificationId);

		if (!optional.isPresent()) {

			return false;
		}

		NotificationEntity notification = optional.get();

		notification.setRead(true);

		notificationRepository.save(notification);

		return true;
	}

	// =====================================================
	// MARK ALL AS READ
	// =====================================================

	public int markAllAsRead(Long userId) {

		List<NotificationEntity> notifications = notificationRepository
				.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);

		int count = 0;

		for (NotificationEntity notification : notifications) {

			notification.setRead(true);

			notificationRepository.save(notification);

			count++;
		}

		return count;
	}

	// =====================================================
	// DELETE
	// =====================================================

	public boolean deleteNotification(Long notificationId) {

		if (!notificationRepository.existsById(notificationId)) {

			return false;
		}

		notificationRepository.deleteById(notificationId);

		return true;
	}

	// =====================================================
	// DELETE ALL
	// =====================================================
	@Transactional
	public int deleteAll(Long userId) {

		return notificationRepository.deleteByUserId(userId);
	}

	// =====================================================
	// MARK AS READ
	// =====================================================

	public boolean markAsRead(Long notificationId, Long userId) {

		Optional<NotificationEntity> optional = notificationRepository.findById(notificationId);

		if (!optional.isPresent()) {

			return false;
		}

		NotificationEntity notification = optional.get();

		if (!userId.equals(notification.getUserId())) {

			throw new RuntimeException("You cannot modify this notification");
		}

		notification.setRead(true);

		notificationRepository.save(notification);

		return true;
	}

	// =====================================================
	// DELETE
	// =====================================================

	public boolean deleteNotification(Long notificationId, Long userId) {

		Optional<NotificationEntity> optional = notificationRepository.findById(notificationId);

		if (!optional.isPresent()) {

			return false;
		}

		NotificationEntity notification = optional.get();

		if (!userId.equals(notification.getUserId())) {

			throw new RuntimeException("You cannot delete this notification");
		}

		notificationRepository.deleteById(notificationId);

		return true;
	}

	
	

}
