
package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.TaskEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.NotificationService;
import com.DevCollab.service.ProjectService;
import com.DevCollab.service.TaskService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	// =====================================================
	// GET ALL TASKS
	// =====================================================

	@GetMapping
	public ResponseEntity<?> getAllTasks(Authentication authentication) {

		if (!isAuthenticated(authentication)) {
			return unauthorized();
		}

		return ResponseEntity.ok(taskService.getAllTasks());
	}

	// =====================================================
	// GET TASK BY ID
	// =====================================================

	@GetMapping("/{taskId}")
	public ResponseEntity<?> getTaskById(@PathVariable Long taskId, Authentication authentication) {

		if (!isAuthenticated(authentication)) {
			return unauthorized();
		}

		if (taskId == null || taskId <= 0) {

			return ResponseEntity.badRequest().body("Invalid task ID");
		}

		Optional<TaskEntity> task = taskService.getTaskById(taskId);

		if (!task.isPresent()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with ID: " + taskId);
		}

		return ResponseEntity.ok(task.get());
	}

	// =====================================================
	// CREATE TASK
	// =====================================================

	@PostMapping
	public ResponseEntity<?> createTask(@RequestBody TaskEntity task, Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {
				return unauthorized();
			}

			if (task == null) {

				return ResponseEntity.badRequest().body("Task data is required");
			}

			if (task.getProjectId() == null) {

				return ResponseEntity.badRequest().body("Project ID is required");
			}

			if (task.getTaskName() == null || task.getTaskName().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Task name is required");
			}

			// =================================================
			// PROJECT CHECK
			// =================================================

			Optional<ProjectEntity> project = projectService.getProjectById(task.getProjectId());

			if (!project.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
			}

			// =================================================
			// GET LOGGED USER
			// =================================================

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found");
			}

			// =================================================
			// NEVER TRUST CREATED BY
			// =================================================

			task.setCreatedBy(loggedInUser.getUserId());

			// =================================================
			// DEFAULT STATUS
			// =================================================

			if (task.getStatus() == null || task.getStatus().trim().isEmpty()) {

				task.setStatus("TODO");
			}

			task.setStatus(task.getStatus().trim().toUpperCase());

			// =================================================
			// STATUS VALIDATION
			// =================================================

			if (!isValidStatus(task.getStatus())) {

				return ResponseEntity.badRequest().body("Invalid task status. " + getAllowedStatusesMessage());
			}

			// =================================================
			// CREATE TASK
			// =================================================

			TaskEntity savedTask = taskService.createTask(task);

			return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to create task: " + e.getMessage());
		}
	}

	// =====================================================
	// UPDATE TASK
	// =====================================================

	@PutMapping("/{taskId}")
	public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody TaskEntity task,
			Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {
				return unauthorized();
			}

			if (taskId == null || taskId <= 0) {

				return ResponseEntity.badRequest().body("Invalid task ID");
			}

			Optional<TaskEntity> optionalTask = taskService.getTaskById(taskId);

			if (!optionalTask.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with ID: " + taskId);
			}

			TaskEntity existingTask = optionalTask.get();

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found");
			}

			// =================================================
			// AUTHORIZATION
			// =================================================

			boolean allowed = isAdmin(authentication)
					|| isProjectOwner(existingTask.getProjectId(), loggedInUser.getUserId())
					|| isTaskCreator(existingTask, loggedInUser.getUserId())
					|| isAssignedUser(existingTask, loggedInUser.getUserId());

			if (!allowed) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update this task");
			}

			if (task == null) {

				return ResponseEntity.badRequest().body("Task data is required");
			}

			// =================================================
			// CREATED BY CANNOT CHANGE
			// =================================================

			task.setCreatedBy(existingTask.getCreatedBy());

			// =================================================
			// STATUS VALIDATION
			// =================================================

			if (task.getStatus() != null && !task.getStatus().trim().isEmpty()) {

				String newStatus = task.getStatus().trim().toUpperCase();

				if (!isValidStatus(newStatus)) {

					return ResponseEntity.badRequest().body("Invalid task status. " + getAllowedStatusesMessage());
				}

				task.setStatus(newStatus);
			}

			// =================================================
			// UPDATE TASK
			// =================================================

			TaskEntity updatedTask = taskService.updateTask(taskId, task);

			if (updatedTask == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
			}

			return ResponseEntity.ok(updatedTask);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update task: " + e.getMessage());
		}
	}

	// =====================================================
	// UPDATE TASK STATUS ONLY
	// =====================================================

	@PutMapping("/{taskId}/status")
	public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status,
			Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {
				return unauthorized();
			}

			if (taskId == null || taskId <= 0) {

				return ResponseEntity.badRequest().body("Invalid task ID");
			}

			if (status == null || status.trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Status is required");
			}

			Optional<TaskEntity> optionalTask = taskService.getTaskById(taskId);

			if (!optionalTask.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
			}

			TaskEntity task = optionalTask.get();

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found");
			}

			String newStatus = status.trim().toUpperCase();

			// =================================================
			// VALIDATE STATUS
			// =================================================

			if (!isValidStatus(newStatus)) {

				return ResponseEntity.badRequest().body("Invalid task status. " + getAllowedStatusesMessage());
			}

			// =================================================
			// ADMIN
			// =================================================

			if (isAdmin(authentication)) {

				task.setStatus(newStatus);

				TaskEntity updated = taskService.updateStatus(task);

				createStatusNotification(updated);

				return ResponseEntity.ok(updated);
			}

			// =================================================
			// TESTER
			// =================================================

			if (hasRole(authentication, "ROLE_TESTER")) {

				if (!isTesterAllowedStatus(newStatus)) {

					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
							"Tester can change task status only to: " + "TESTING, BUG_FOUND, " + "RETESTING, APPROVED");
				}

				/*
				 * Tester should only change status of an assigned task.
				 */

				if (!isAssignedUser(task, loggedInUser.getUserId())) {

					return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot change this task status");
				}

				task.setStatus(newStatus);

				TaskEntity updated = taskService.updateStatus(task);

				createStatusNotification(updated);

				return ResponseEntity.ok(updated);
			}

			// =================================================
			// NORMAL USER
			// =================================================

			boolean allowed = isProjectOwner(task.getProjectId(), loggedInUser.getUserId())
					|| isTaskCreator(task, loggedInUser.getUserId()) || isAssignedUser(task, loggedInUser.getUserId());

			if (!allowed) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot change this task status");
			}

			task.setStatus(newStatus);

			TaskEntity updated = taskService.updateStatus(task);

			createStatusNotification(updated);

			return ResponseEntity.ok(updated);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update task status: " + e.getMessage());
		}
	}

	// =====================================================
	// DELETE TASK
	// =====================================================

	@DeleteMapping("/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable Long taskId, Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {
				return unauthorized();
			}

			if (taskId == null || taskId <= 0) {

				return ResponseEntity.badRequest().body("Invalid task ID");
			}

			Optional<TaskEntity> optionalTask = taskService.getTaskById(taskId);

			if (!optionalTask.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
			}

			TaskEntity existingTask = optionalTask.get();

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found");
			}

			boolean allowed = isAdmin(authentication)
					|| isProjectOwner(existingTask.getProjectId(), loggedInUser.getUserId())
					|| isTaskCreator(existingTask, loggedInUser.getUserId());

			if (!allowed) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete this task");
			}

			boolean deleted = taskService.deleteTask(taskId);

			if (!deleted) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
			}

			return ResponseEntity.ok("Task deleted successfully");

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete task: " + e.getMessage());
		}
	}

	// =====================================================
	// GET TASKS BY PROJECT
	// =====================================================

	@GetMapping("/project/{projectId}")
	public ResponseEntity<?> getTasksByProject(@PathVariable Long projectId, Authentication authentication) {

		if (!isAuthenticated(authentication)) {
			return unauthorized();
		}

		if (projectId == null || projectId <= 0) {

			return ResponseEntity.badRequest().body("Invalid project ID");
		}

		return ResponseEntity.ok(taskService.getTasksByProject(projectId));
	}

	// =====================================================
	// CREATE STATUS NOTIFICATION
	// =====================================================

	private void createStatusNotification(TaskEntity task) {

		if (task == null) {

			System.out.println("STATUS NOTIFICATION SKIPPED: task is null");

			return;
		}

		Long assignedTo = task.getAssignedTo();

		if (assignedTo == null) {

			System.out.println("STATUS NOTIFICATION SKIPPED: assignedTo is null");

			return;
		}

		try {

			notificationService.createNotification(

					assignedTo,

					"Task Status Updated",

					"Task '" + task.getTaskName() + "' status changed to " + task.getStatus(),

					"TASK_STATUS");

			System.out.println("======================================");

			System.out.println("TASK STATUS NOTIFICATION CREATED");

			System.out.println("USER ID -> " + assignedTo);

			System.out.println("TASK ID -> " + task.getTaskId());

			System.out.println("STATUS -> " + task.getStatus());

			System.out.println("======================================");

		} catch (Exception e) {

			System.out.println("TASK STATUS NOTIFICATION ERROR -> " + e.getMessage());

			e.printStackTrace();
		}
	}

	// =====================================================
	// VALID TASK STATUS
	// =====================================================

	private boolean isValidStatus(String status) {

		if (status == null) {
			return false;
		}

		switch (status.toUpperCase()) {

		case "TODO":
		case "IN_PROGRESS":
		case "CODE_REVIEW":
		case "TESTING":
		case "BUG_FOUND":
		case "FIXING":
		case "RETESTING":
		case "APPROVED":
		case "COMPLETED":

			return true;

		default:

			return false;
		}
	}

	// =====================================================
	// TESTER ALLOWED STATUS
	// =====================================================

	private boolean isTesterAllowedStatus(String status) {

		if (status == null) {
			return false;
		}

		switch (status.toUpperCase()) {

		case "TESTING":
		case "BUG_FOUND":
		case "RETESTING":
		case "APPROVED":

			return true;

		default:

			return false;
		}
	}

	// =====================================================
	// AUTHENTICATION CHECK
	// =====================================================

	private boolean isAuthenticated(Authentication authentication) {

		return authentication != null && authentication.isAuthenticated();
	}

	// =====================================================
	// ADMIN CHECK
	// =====================================================

	private boolean isAdmin(Authentication authentication) {

		return hasRole(authentication, "ROLE_ADMIN");
	}

	// =====================================================
	// GENERIC ROLE CHECK
	// =====================================================

	private boolean hasRole(Authentication authentication, String role) {

		if (authentication == null || role == null) {

			return false;
		}

		return authentication.getAuthorities().stream().anyMatch(authority -> role.equals(authority.getAuthority()));
	}

	// =====================================================
	// GET LOGGED USER
	// =====================================================

	private UserEntity getLoggedInUser(Authentication authentication) {

		if (authentication == null) {
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

		return users.get(0);
	}

	// =====================================================
	// PROJECT OWNER CHECK
	// =====================================================

	private boolean isProjectOwner(Long projectId, Long userId) {

		if (projectId == null || userId == null) {

			return false;
		}

		Optional<ProjectEntity> project = projectService.getProjectById(projectId);

		if (!project.isPresent()) {
			return false;
		}

		return userId.equals(project.get().getOwnerId());
	}

	// =====================================================
	// TASK CREATOR CHECK
	// =====================================================

	private boolean isTaskCreator(TaskEntity task, Long userId) {

		if (task == null || userId == null) {

			return false;
		}

		return task.getCreatedBy() != null && task.getCreatedBy().equals(userId);
	}

	// =====================================================
	// ASSIGNED USER CHECK
	// =====================================================

	private boolean isAssignedUser(TaskEntity task, Long userId) {

		if (task == null || userId == null) {

			return false;
		}

		return task.getAssignedTo() != null && task.getAssignedTo().equals(userId);
	}

	// =====================================================
	// UNAUTHORIZED RESPONSE
	// =====================================================

	private ResponseEntity<?> unauthorized() {

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
	}

	// =====================================================
	// ALLOWED STATUS MESSAGE
	// =====================================================

	private String getAllowedStatusesMessage() {

		return "Allowed: TODO, IN_PROGRESS, CODE_REVIEW, " + "TESTING, BUG_FOUND, FIXING, "
				+ "RETESTING, APPROVED, COMPLETED";
	}
}
