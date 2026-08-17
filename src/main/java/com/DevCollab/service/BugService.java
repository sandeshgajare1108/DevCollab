package com.DevCollab.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.BugRepository;
import com.DevCollab.Repository.ProjectMemberRepository;
import com.DevCollab.Repository.ProjectRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.Repository.UserRepository;
import com.DevCollab.entity.BugEntity;
import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.ProjectMemberEntity;

@Service
public class BugService {

	@Autowired
	private BugRepository bugRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private PullRequestRepository pullRequestRepository;

	@Autowired
	private ProjectMemberRepository projectMemberRepository;
	@Autowired
	private ProjectWorkflowService projectWorkflowService;

	// =====================================================
	// CREATE BUG
	// =====================================================

	public BugEntity createBug(BugEntity bug) {

		if (bug.getProjectId() == null) {
			throw new RuntimeException("Project ID is required");
		}

		if (bug.getReportedBy() == null) {
			throw new RuntimeException("Reported by user is required");
		}

		if (bug.getTitle() == null || bug.getTitle().trim().isEmpty()) {

			throw new RuntimeException("Bug title is required");
		}

		if (!projectRepository.existsById(bug.getProjectId())) {

			throw new RuntimeException("Project not found");
		}

		if (!userRepository.existsById(bug.getReportedBy())) {

			throw new RuntimeException("Reporter user not found");
		}

		if (bug.getTaskId() != null) {

			// Task existence can be checked through your
			// existing TaskRepository if required.
		}

		if (bug.getPullRequestId() != null) {

			if (!pullRequestRepository.existsById(bug.getPullRequestId())) {

				throw new RuntimeException("Pull Request not found");
			}
		}

		if (bug.getSeverity() == null || bug.getSeverity().trim().isEmpty()) {

			bug.setSeverity("MEDIUM");
		}

		if (bug.getPriority() == null || bug.getPriority().trim().isEmpty()) {

			bug.setPriority("MEDIUM");
		}

		bug.setStatus("OPEN");

		Timestamp now = new Timestamp(System.currentTimeMillis());

		bug.setCreatedAt(now);
		bug.setUpdatedAt(now);

		return bugRepository.save(bug);
	}

	// =====================================================
	// GET ALL BUGS
	// =====================================================

	public List<BugEntity> getAllBugs() {

		return bugRepository.findAll();
	}

	// =====================================================
	// GET BUG BY ID
	// =====================================================

	public Optional<BugEntity> getBugById(Long bugId) {

		return bugRepository.findById(bugId);
	}

	// =====================================================
	// GET PROJECT BUGS
	// =====================================================

	public List<BugEntity> getProjectBugs(Long projectId) {

		return bugRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
	}

	// =====================================================
	// GET TASK BUGS
	// =====================================================

	public List<BugEntity> getTaskBugs(Long taskId) {

		return bugRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
	}

	// =====================================================
	// GET PR BUGS
	// =====================================================

	public List<BugEntity> getPullRequestBugs(Long pullRequestId) {

		return bugRepository.findByPullRequestIdOrderByCreatedAtDesc(pullRequestId);
	}

	// =====================================================
	// GET ASSIGNED BUGS
	// =====================================================

	public List<BugEntity> getAssignedBugs(Long assignedTo) {

		return bugRepository.findByAssignedToOrderByCreatedAtDesc(assignedTo);
	}

	// =====================================================
	// ASSIGN BUG
	// =====================================================

	public BugEntity assignBug(Long bugId, Long assignedTo) {

		Optional<BugEntity> optional = bugRepository.findById(bugId);

		if (!optional.isPresent()) {

			return null;
		}

		if (assignedTo == null) {

			throw new RuntimeException("Assigned user is required");
		}

		if (!userRepository.existsById(assignedTo)) {

			throw new RuntimeException("Assigned user not found");
		}

		BugEntity bug = optional.get();

		bug.setAssignedTo(assignedTo);

		if ("OPEN".equals(bug.getStatus())) {

			bug.setStatus("ASSIGNED");
		}

		bug.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		return bugRepository.save(bug);
	}

	// =====================================================
	// UPDATE STATUS
	// =====================================================

	public BugEntity updateStatus(Long bugId, String status, String resolution) {

		Optional<BugEntity> optional = bugRepository.findById(bugId);

		if (!optional.isPresent()) {

			return null;
		}

		String normalized = normalizeStatus(status);

		if (!isValidStatus(normalized)) {

			throw new RuntimeException("Invalid bug status");
		}

		BugEntity bug = optional.get();

		bug.setStatus(normalized);

		if (resolution != null) {

			bug.setResolution(resolution);
		}

		bug.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		BugEntity saved = bugRepository.save(bug);

		// =================================================
		// WORKFLOW INTEGRATION
		// =================================================

		projectWorkflowService.handleBugStatusChange(saved);

		return saved;
	}

	// =====================================================
	// UPDATE BUG
	// =====================================================

	public BugEntity updateBug(Long bugId, BugEntity request) {

		Optional<BugEntity> optional = bugRepository.findById(bugId);

		if (!optional.isPresent()) {

			return null;
		}

		BugEntity existing = optional.get();

		if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {

			existing.setTitle(request.getTitle());
		}

		if (request.getDescription() != null) {

			existing.setDescription(request.getDescription());
		}

		if (request.getSeverity() != null && !request.getSeverity().trim().isEmpty()) {

			existing.setSeverity(request.getSeverity());
		}

		if (request.getPriority() != null && !request.getPriority().trim().isEmpty()) {

			existing.setPriority(request.getPriority());
		}

		existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		return bugRepository.save(existing);
	}

	// =====================================================
	// DELETE BUG
	// =====================================================

	public boolean deleteBug(Long bugId) {

		if (!bugRepository.existsById(bugId)) {

			return false;
		}

		bugRepository.deleteById(bugId);

		return true;
	}

	// =====================================================
	// STATUS NORMALIZE
	// =====================================================

	private String normalizeStatus(String status) {

		if (status == null) {
			return "";
		}

		return status.trim().toUpperCase();
	}

	// =====================================================
	// VALID STATUS
	// =====================================================

	private boolean isValidStatus(String status) {

		switch (status) {

		case "OPEN":
		case "ASSIGNED":
		case "IN_PROGRESS":
		case "FIXED":
		case "RETESTING":
		case "CLOSED":
		case "REOPENED":

			return true;

		default:
			return false;
		}
	}
}