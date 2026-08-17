package com.DevCollab.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.BugRepository;
import com.DevCollab.Repository.ProjectMemberRepository;
import com.DevCollab.Repository.ProjectRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.Repository.TaskRepository;
import com.DevCollab.dto.DashboardResponse;
import com.DevCollab.entity.BugEntity;
import com.DevCollab.entity.ProjectEntity;
import com.DevCollab.entity.ProjectMemberEntity;
import com.DevCollab.entity.PullRequestEntity;
import com.DevCollab.entity.TaskEntity;

@Service
public class DashboardService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectMemberRepository projectMemberRepository;

	@Autowired
	private BugRepository bugRepository;

	@Autowired
	private PullRequestRepository pullRequestRepository;

	// =====================================================
	// GET DASHBOARD DATA
	// =====================================================

	public DashboardResponse getDashboard() {

		System.out.println("=================================");

		System.out.println("Dashboard Service Working");

		System.out.println("=================================");

		DashboardResponse response = new DashboardResponse();

		// =====================================================
		// PROJECT STATISTICS
		// =====================================================

		long totalProjects = projectRepository.count();

		long planningProjects = projectRepository.countByStatus("PLANNING");

		long inProgressProjects = projectRepository.countByStatus("IN_PROGRESS");

		long completedProjects = projectRepository.countByStatus("COMPLETED");

		response.setTotalProjects(totalProjects);

		response.setPlanningProjects(planningProjects);

		response.setInProgressProjects(inProgressProjects);

		response.setCompletedProjects(completedProjects);

		// =====================================================
		// TASK STATISTICS
		// =====================================================

		long totalTasks = taskRepository.count();

		long todoTasks = taskRepository.countByStatus("TODO");

		long inProgressTasks = taskRepository.countByStatus("IN_PROGRESS");

		long reviewTasks = taskRepository.countByStatus("REVIEW");

		long completedTasks = taskRepository.countByStatus("COMPLETED");

		response.setTotalTasks(totalTasks);

		response.setTodoTasks(todoTasks);

		response.setInProgressTasks(inProgressTasks);

		response.setReviewTasks(reviewTasks);

		response.setCompletedTasks(completedTasks);

		// =====================================================
		// PROJECT MEMBER STATISTICS
		// =====================================================

		long totalMembers = projectMemberRepository.count();

		long ownerMembers = projectMemberRepository.countByMemberRole(ProjectMemberEntity.MemberRole.OWNER);

		long developerMembers = projectMemberRepository.countByMemberRole(ProjectMemberEntity.MemberRole.DEVELOPER);

		long testerMembers = projectMemberRepository.countByMemberRole(ProjectMemberEntity.MemberRole.TESTER);

		long designerMembers = projectMemberRepository.countByMemberRole(ProjectMemberEntity.MemberRole.DESIGNER);

		long managerMembers = projectMemberRepository.countByMemberRole(ProjectMemberEntity.MemberRole.MANAGER);

		response.setTotalMembers(totalMembers);

		response.setOwnerMembers(ownerMembers);

		response.setDeveloperMembers(developerMembers);

		response.setTesterMembers(testerMembers);

		response.setDesignerMembers(designerMembers);

		response.setManagerMembers(managerMembers);

		// =====================================================
		// CONSOLE LOG
		// =====================================================

		System.out.println("Total Projects       = " + totalProjects);

		System.out.println("Planning Projects    = " + planningProjects);

		System.out.println("In Progress Projects = " + inProgressProjects);

		System.out.println("Completed Projects   = " + completedProjects);

		System.out.println("Total Tasks          = " + totalTasks);

		System.out.println("TODO Tasks           = " + todoTasks);

		System.out.println("In Progress Tasks    = " + inProgressTasks);

		System.out.println("Review Tasks         = " + reviewTasks);

		System.out.println("Completed Tasks      = " + completedTasks);

		System.out.println("Total Members        = " + totalMembers);

		System.out.println("OWNER Members        = " + ownerMembers);

		System.out.println("DEVELOPER Members    = " + developerMembers);

		System.out.println("TESTER Members       = " + testerMembers);

		System.out.println("DESIGNER Members     = " + designerMembers);

		System.out.println("MANAGER Members      = " + managerMembers);

		System.out.println("=================================");

		return response;
	}

	// =====================================================
	// GET PROJECT PROGRESS
	// =====================================================

	public Map<String, Object> getProjectProgress(Long projectId) {

		System.out.println("======================================");

		System.out.println("PROJECT PROGRESS SERVICE");

		System.out.println("PROJECT ID = " + projectId);

		System.out.println("======================================");

		Map<String, Object> response = new HashMap<String, Object>();

		// =====================================================
		// VALID PROJECT ID
		// =====================================================

		if (projectId == null || projectId <= 0) {

			response.put("projectId", projectId);

			response.put("message", "Invalid project ID");

			return response;
		}

		// =====================================================
		// CHECK PROJECT
		// =====================================================

		Optional<ProjectEntity> optionalProject = projectRepository.findById(projectId);

		if (!optionalProject.isPresent()) {

			response.put("projectId", projectId);

			response.put("message", "Project not found");

			return response;
		}

		ProjectEntity project = optionalProject.get();

		// =====================================================
		// TASKS
		// =====================================================

		List<TaskEntity> tasks = taskRepository.findByProjectId(projectId);

		if (tasks == null) {

			tasks = new java.util.ArrayList<TaskEntity>();
		}

		int totalTasks = tasks.size();

		int completedTasks = 0;

		for (TaskEntity task : tasks) {

			if (task != null && task.getStatus() != null && "COMPLETED".equalsIgnoreCase(task.getStatus())) {

				completedTasks++;
			}
		}

		// =====================================================
		// BUGS
		// =====================================================

		List<BugEntity> bugs = bugRepository.findByProjectIdOrderByCreatedAtDesc(projectId);

		if (bugs == null) {

			bugs = new java.util.ArrayList<BugEntity>();
		}

		int totalBugs = bugs.size();

		int openBugs = 0;

		for (BugEntity bug : bugs) {

			if (bug == null) {
				continue;
			}

			String bugStatus = bug.getStatus();

			if (bugStatus == null || !"CLOSED".equalsIgnoreCase(bugStatus)) {

				openBugs++;
			}
		}

		// =====================================================
		// PULL REQUESTS
		// =====================================================

		// =====================================================
		// DEV COLLAB PULL REQUESTS
		// Only PRs linked to a Task
		// =====================================================

		long totalPullRequests = pullRequestRepository.countByProjectIdAndTaskIdIsNotNull(projectId);

		long approvedPullRequests = pullRequestRepository.countByProjectIdAndStatusAndTaskIdIsNotNull(projectId,
				"APPROVED");

		// =====================================================
		// PROGRESS %
		// =====================================================

		int progress = 0;

		if (totalTasks > 0) {

			progress = (completedTasks * 100) / totalTasks;
		}

		// =====================================================
		// PROJECT STATUS
		// =====================================================

		String projectStatus = project.getStatus();

		// =====================================================
		// RESPONSE
		// =====================================================

		response.put("projectId", projectId);

		response.put("projectName", project.getProjectName());

		response.put("projectStatus", projectStatus);

		response.put("totalTasks", totalTasks);

		response.put("completedTasks", completedTasks);

		response.put("totalBugs", totalBugs);

		response.put("openBugs", openBugs);

		response.put("totalPullRequests", totalPullRequests);

		response.put("approvedPullRequests", approvedPullRequests);

		response.put("progress", progress);

		System.out.println("Project Name        = " + project.getProjectName());

		System.out.println("Project Status      = " + projectStatus);

		System.out.println("Total Tasks         = " + totalTasks);

		System.out.println("Completed Tasks     = " + completedTasks);

		System.out.println("Total Bugs          = " + totalBugs);

		System.out.println("Open Bugs           = " + openBugs);

		System.out.println("Total Pull Requests = " + totalPullRequests);

		System.out.println("Approved PRs        = " + approvedPullRequests);

		System.out.println("Progress            = " + progress + "%");

		System.out.println("======================================");

		return response;
	}
}