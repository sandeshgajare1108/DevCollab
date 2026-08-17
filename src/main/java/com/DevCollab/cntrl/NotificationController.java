
package com.DevCollab.cntrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.NotificationEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.NotificationService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	// =====================================================
	// GET ALL MY NOTIFICATIONS
	// =====================================================

	@GetMapping
	public ResponseEntity<?> getMyNotifications(Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		List<NotificationEntity> notifications = notificationService.getUserNotifications(userId);

		return ResponseEntity.ok(notifications);
	}

	// =====================================================
	// GET MY UNREAD NOTIFICATIONS
	// =====================================================

	@GetMapping("/unread")
	public ResponseEntity<?> getMyUnreadNotifications(Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		List<NotificationEntity> notifications = notificationService.getUnreadNotifications(userId);

		return ResponseEntity.ok(notifications);
	}

	// =====================================================
	// UNREAD COUNT
	// =====================================================

	@GetMapping("/unread/count")
	public ResponseEntity<?> getUnreadCount(Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		long count = notificationService.countUnread(userId);

		Map<String, Object> response = new HashMap<>();

		response.put("userId", userId);

		response.put("unreadCount", count);

		return ResponseEntity.ok(response);
	}

	// =====================================================
	// GET MY NOTIFICATION BY ID
	// =====================================================

	@GetMapping("/{notificationId}")
	public ResponseEntity<?> getNotification(@PathVariable Long notificationId, Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		Optional<NotificationEntity> optional = notificationService.getNotificationById(notificationId);

		if (!optional.isPresent()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
		}

		NotificationEntity notification = optional.get();

		if (!userId.equals(notification.getUserId())) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot access this notification");
		}

		return ResponseEntity.ok(notification);
	}

	// =====================================================
	// CREATE NOTIFICATION
	// =====================================================

	/*
	 * Internal/application use.
	 *
	 * Frontend should NOT be allowed to create arbitrary notifications for
	 * another user.
	 *
	 * Later we will call NotificationService from Task / Bug / PR / Review
	 * workflows.
	 */

	@PostMapping
	public ResponseEntity<?> createNotification(@RequestBody NotificationRequest request,
			Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		if (request == null) {

			return ResponseEntity.badRequest().body("Notification data is required");
		}

		if (request.getUserId() == null) {

			return ResponseEntity.badRequest().body("userId is required");
		}

		if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {

			return ResponseEntity.badRequest().body("title is required");
		}

		if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {

			return ResponseEntity.badRequest().body("message is required");
		}

		NotificationEntity notification = notificationService.createNotification(request.getUserId(),
				request.getTitle(), request.getMessage(), request.getType());

		return ResponseEntity.status(HttpStatus.CREATED).body(notification);
	}

	// =====================================================
	// MARK AS READ
	// =====================================================

	@PutMapping("/{notificationId}/read")
	public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		try {

			boolean result = notificationService.markAsRead(notificationId, userId);

			if (!result) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
			}

			return ResponseEntity.ok("Notification marked as read");

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

	// =====================================================
	// MARK ALL MY NOTIFICATIONS AS READ
	// =====================================================

	@PutMapping("/read-all")
	public ResponseEntity<?> markAllAsRead(Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		int count = notificationService.markAllAsRead(userId);

		Map<String, Object> response = new HashMap<>();

		response.put("message", "All notifications marked as read");

		response.put("updatedCount", count);

		return ResponseEntity.ok(response);
	}

	// =====================================================
	// DELETE MY NOTIFICATION
	// =====================================================

	@DeleteMapping("/{notificationId}")
	public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId, Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		try {

			boolean result = notificationService.deleteNotification(notificationId, userId);

			if (!result) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
			}

			return ResponseEntity.ok("Notification deleted");

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

	// =====================================================
	// DELETE ALL MY NOTIFICATIONS
	// =====================================================

	@DeleteMapping
	public ResponseEntity<?> deleteAll(Authentication authentication) {

		Long userId = getLoggedInUserId(authentication);

		if (userId == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
		}

		int count = notificationService.deleteAll(userId);

		Map<String, Object> response = new HashMap<>();

		response.put("message", "All notifications deleted");

		response.put("deletedCount", count);

		return ResponseEntity.ok(response);
	}

	// =====================================================
	// GET LOGGED-IN USER ID
	// =====================================================

	private Long getLoggedInUserId(Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {

			return null;
		}

		String email = authentication.getName();

		if (email == null || email.trim().isEmpty()) {

			return null;
		}

		List<UserEntity> users = userService.getByUserEmail(email);

		if (users == null || users.isEmpty()) {

			return null;
		}

		return users.get(0).getUserId();
	}

	// =====================================================
	// REQUEST DTO
	// =====================================================

	public static class NotificationRequest {

		private Long userId;

		private String title;

		private String message;

		private String type;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
