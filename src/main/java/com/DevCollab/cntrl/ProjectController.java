
package com.DevCollab.cntrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.service.ProjectService;
import com.DevCollab.service.ProjectWorkflowService;
import com.DevCollab.service.UserService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin("*")
public class ProjectController {

	@Autowired
	private ProjectService service;

	@Autowired
	private UserService userService;

	@Autowired
	private ProjectWorkflowService projectWorkflowService;
	// =====================================================
	// TEST
	// =====================================================

	@GetMapping("/test")
	public ResponseEntity<String> test() {

		return ResponseEntity.ok("Project Controller Working");
	}

	// =====================================================
	// CREATE PROJECT
	// =====================================================

	@PostMapping
	public ResponseEntity<?> createProject(@RequestBody ProjectEntity project, Authentication authentication) {

		try {

			if (authentication == null || !authentication.isAuthenticated()) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
			}

			if (project == null) {

				return ResponseEntity.badRequest().body("Project data is required.");
			}

			if (project.getProjectName() == null || project.getProjectName().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Project name is required.");
			}

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
			}

			/*
			 * Owner is taken from JWT user. Client cannot create project for
			 * another owner.
			 */
			project.setOwnerId(loggedInUser.getUserId());

			if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {

				project.setStatus("PLANNING");
			}

			if (project.getVisibility() == null || project.getVisibility().trim().isEmpty()) {

				project.setVisibility("PRIVATE");
			}

			ProjectEntity saved = service.createProject(project);

			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to create project: " + e.getMessage());
		}
	}

	// =====================================================
	// GET ALL PROJECTS
	// =====================================================

	@GetMapping
	public ResponseEntity<?> getAllProjects(Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		return ResponseEntity.ok(service.getAllProjects());
	}

	// =====================================================
	// GET PROJECT BY ID
	// =====================================================

	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable Long projectId, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		if (projectId == null || projectId <= 0) {

			return ResponseEntity.badRequest().body("Invalid project ID.");
		}

		Optional<ProjectEntity> optionalProject = service.getProjectById(projectId);

		if (!optionalProject.isPresent()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
		}

		ProjectEntity project = optionalProject.get();

		/*
		 * ADMIN can view any project.
		 *
		 * Other authenticated users can view project information. Change this
		 * rule later if you want member-only visibility.
		 */
		return ResponseEntity.ok(project);
	}

	// =====================================================
	// UPDATE PROJECT
	// =====================================================

	@PutMapping("/{projectId}")
	public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectEntity newProject,
			Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
			}

			if (projectId == null || projectId <= 0) {

				return ResponseEntity.badRequest().body("Invalid project ID.");
			}

			if (newProject == null) {

				return ResponseEntity.badRequest().body("Project data is required.");
			}

			Optional<ProjectEntity> existingProject = service.getProjectById(projectId);

			if (!existingProject.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
			}

			ProjectEntity project = existingProject.get();

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
			}

			/*
			 * ADMIN can update any project. Normal user can update only own
			 * project.
			 */
			if (!isAdmin(authentication) && !loggedInUser.getUserId().equals(project.getOwnerId())) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update another user's project.");
			}

			// ---------------------------------------------
			// PROJECT NAME
			// ---------------------------------------------

			if (newProject.getProjectName() != null && !newProject.getProjectName().trim().isEmpty()) {

				project.setProjectName(newProject.getProjectName().trim());
			}

			// ---------------------------------------------
			// DESCRIPTION
			// ---------------------------------------------

			project.setDescription(newProject.getDescription());

			// ---------------------------------------------
			// OWNER
			// ---------------------------------------------

			/*
			 * Do not allow normal users to transfer ownership. ADMIN can change
			 * owner if explicitly supplied.
			 */
			if (isAdmin(authentication) && newProject.getOwnerId() != null) {

				project.setOwnerId(newProject.getOwnerId());
			}

			// ---------------------------------------------
			// STATUS
			// ---------------------------------------------

			if (newProject.getStatus() != null && !newProject.getStatus().trim().isEmpty()) {

				project.setStatus(newProject.getStatus().trim());
			}

			// ---------------------------------------------
			// VISIBILITY
			// ---------------------------------------------

			if (newProject.getVisibility() != null && !newProject.getVisibility().trim().isEmpty()) {

				project.setVisibility(newProject.getVisibility().trim());
			}

			// ---------------------------------------------
			// START DATE
			// ---------------------------------------------

			project.setStartDate(newProject.getStartDate());

			// ---------------------------------------------
			// END DATE
			// ---------------------------------------------

			project.setEndDate(newProject.getEndDate());

			/*
			 * Correct update operation.
			 */
			ProjectEntity updated = service.updateProject(projectId, project);

			if (updated == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
			}

			return ResponseEntity.ok(updated);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update project: " + e.getMessage());
		}
	}

	// =====================================================
	// DELETE PROJECT
	// =====================================================

	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable Long projectId, Authentication authentication) {

		try {

			if (!isAuthenticated(authentication)) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
			}

			if (projectId == null || projectId <= 0) {

				return ResponseEntity.badRequest().body("Invalid project ID.");
			}

			Optional<ProjectEntity> optionalProject = service.getProjectById(projectId);

			if (!optionalProject.isPresent()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
			}

			ProjectEntity project = optionalProject.get();

			UserEntity loggedInUser = getLoggedInUser(authentication);

			if (loggedInUser == null) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
			}

			/*
			 * ADMIN can delete any project. Normal user can delete only own
			 * project.
			 */
			if (!isAdmin(authentication) && !loggedInUser.getUserId().equals(project.getOwnerId())) {

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete another user's project.");
			}

			boolean deleted = service.deleteProjectById(projectId);

			if (!deleted) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
			}

			return ResponseEntity.ok("Project deleted successfully.");

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete project: " + e.getMessage());
		}
	}

	// =====================================================
	// GET PROJECTS BY OWNER
	// =====================================================

	@GetMapping("/owner/{ownerId}")
	public ResponseEntity<?> getProjectsByOwner(@PathVariable Long ownerId, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		UserEntity loggedInUser = getLoggedInUser(authentication);

		if (loggedInUser == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
		}

		/*
		 * ADMIN can view any owner's projects. Normal user can view only own
		 * projects.
		 */
		if (!isAdmin(authentication) && !loggedInUser.getUserId().equals(ownerId)) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot view another user's projects.");
		}

		return ResponseEntity.ok(service.getProjectsByOwner(ownerId));
	}

	// =====================================================
	// GET PROJECTS BY STATUS
	// =====================================================

	@GetMapping("/status/{status}")
	public ResponseEntity<?> getProjectsByStatus(@PathVariable String status, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		return ResponseEntity.ok(service.getProjectsByStatus(status));
	}

	// =====================================================
	// GET PROJECTS BY VISIBILITY
	// =====================================================

	@GetMapping("/visibility/{visibility}")
	public ResponseEntity<?> getProjectsByVisibility(@PathVariable String visibility, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		return ResponseEntity.ok(service.getProjectsByVisibility(visibility));
	}

	// =====================================================
	// GET OWNER PROJECTS BY STATUS
	// =====================================================

	@GetMapping("/owner/{ownerId}/status/{status}")
	public ResponseEntity<?> getOwnerProjectsByStatus(@PathVariable Long ownerId, @PathVariable String status,
			Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		UserEntity loggedInUser = getLoggedInUser(authentication);

		if (loggedInUser == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
		}

		if (!isAdmin(authentication) && !loggedInUser.getUserId().equals(ownerId)) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot view another user's projects.");
		}

		return ResponseEntity.ok(service.getOwnerProjectsByStatus(ownerId, status));
	}

	// =====================================================
	// SEARCH PROJECT
	// =====================================================

	@GetMapping("/search/{projectName}")
	public ResponseEntity<?> searchProjects(@PathVariable String projectName, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		return ResponseEntity.ok(service.searchProjects(projectName));
	}

	// =====================================================
	// COUNT PROJECTS BY STATUS
	// =====================================================

	@GetMapping("/count/status/{status}")
	public ResponseEntity<?> countProjectsByStatus(@PathVariable String status, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		return ResponseEntity.ok(service.countProjectsByStatus(status));
	}

	// =====================================================
	// COUNT PROJECTS BY OWNER
	// =====================================================

	@GetMapping("/count/owner/{ownerId}")
	public ResponseEntity<?> countProjectsByOwner(@PathVariable Long ownerId, Authentication authentication) {

		if (!isAuthenticated(authentication)) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		UserEntity loggedInUser = getLoggedInUser(authentication);

		if (loggedInUser == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logged-in user not found.");
		}

		if (!isAdmin(authentication) && !loggedInUser.getUserId().equals(ownerId)) {

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot view another user's project count.");
		}

		return ResponseEntity.ok(service.countProjectsByOwner(ownerId));
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

		if (authentication == null) {

			return false;
		}

		return authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
	}

	// =====================================================
	// GET LOGGED-IN USER
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

	@GetMapping("/{projectId}/recalculate-progress")
	public ResponseEntity<?> recalculateProjectProgress(@PathVariable Long projectId) {

		String status = projectWorkflowService.recalculateProject(projectId);

		if ("Project not found".equals(status)) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(status);
		}

		return ResponseEntity.ok("Project status updated to: " + status);
	}
}
