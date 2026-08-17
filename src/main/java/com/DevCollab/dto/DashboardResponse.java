
package com.DevCollab.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.DevCollab.Repository.BugRepository;
import com.DevCollab.Repository.PullRequestRepository;
import com.DevCollab.Repository.TaskRepository;
import com.DevCollab.entity.BugEntity;
import com.DevCollab.entity.PullRequestEntity;
import com.DevCollab.entity.TaskEntity;

public class DashboardResponse {
	@Autowired
	private PullRequestRepository pullRequestRepository;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private BugRepository bugRepository;

	// =====================================================
	// PROJECTS
	// =====================================================

	private long totalProjects;
	private long planningProjects;
	private long inProgressProjects;
	private long completedProjects;

	// =====================================================
	// TASKS
	// =====================================================

	private long totalTasks;
	private long todoTasks;
	private long inProgressTasks;
	private long reviewTasks;
	private long completedTasks;

	// =====================================================
	// PROJECT MEMBERS
	// =====================================================

	private long totalMembers;
	private long ownerMembers;
	private long developerMembers;
	private long testerMembers;
	private long designerMembers;
	private long managerMembers;

	// =====================================================
	// CONSTRUCTOR
	// =====================================================

	public DashboardResponse() {
	}

	// =====================================================
	// PROJECT GETTERS / SETTERS
	// =====================================================

	public long getTotalProjects() {
		return totalProjects;
	}

	public void setTotalProjects(long totalProjects) {
		this.totalProjects = totalProjects;
	}

	public long getPlanningProjects() {
		return planningProjects;
	}

	public void setPlanningProjects(long planningProjects) {
		this.planningProjects = planningProjects;
	}

	public long getInProgressProjects() {
		return inProgressProjects;
	}

	public void setInProgressProjects(long inProgressProjects) {
		this.inProgressProjects = inProgressProjects;
	}

	public long getCompletedProjects() {
		return completedProjects;
	}

	public void setCompletedProjects(long completedProjects) {
		this.completedProjects = completedProjects;
	}

	// =====================================================
	// TASK GETTERS / SETTERS
	// =====================================================

	public long getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(long totalTasks) {
		this.totalTasks = totalTasks;
	}

	public long getTodoTasks() {
		return todoTasks;
	}

	public void setTodoTasks(long todoTasks) {
		this.todoTasks = todoTasks;
	}

	public long getInProgressTasks() {
		return inProgressTasks;
	}

	public void setInProgressTasks(long inProgressTasks) {
		this.inProgressTasks = inProgressTasks;
	}

	public long getReviewTasks() {
		return reviewTasks;
	}

	public void setReviewTasks(long reviewTasks) {
		this.reviewTasks = reviewTasks;
	}

	public long getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(long completedTasks) {
		this.completedTasks = completedTasks;
	}

	// =====================================================
	// MEMBER GETTERS / SETTERS
	// =====================================================

	public long getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(long totalMembers) {
		this.totalMembers = totalMembers;
	}

	public long getOwnerMembers() {
		return ownerMembers;
	}

	public void setOwnerMembers(long ownerMembers) {
		this.ownerMembers = ownerMembers;
	}

	public long getDeveloperMembers() {
		return developerMembers;
	}

	public void setDeveloperMembers(long developerMembers) {
		this.developerMembers = developerMembers;
	}

	public long getTesterMembers() {
		return testerMembers;
	}

	public void setTesterMembers(long testerMembers) {
		this.testerMembers = testerMembers;
	}

	public long getDesignerMembers() {
		return designerMembers;
	}

	public void setDesignerMembers(long designerMembers) {
		this.designerMembers = designerMembers;
	}

	public long getManagerMembers() {
		return managerMembers;
	}

	public void setManagerMembers(long managerMembers) {
		this.managerMembers = managerMembers;
	}

	public Map<String, Object> getProjectProgress(Long projectId) {

		Map<String, Object> response = new HashMap<>();

		List<TaskEntity> tasks = taskRepository.findByProjectId(projectId);

		int totalTasks = tasks.size();

		int completedTasks = 0;

		for (TaskEntity task : tasks) {

			if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {

				completedTasks++;
			}
		}

		List<BugEntity> bugs = bugRepository.findByProjectIdOrderByCreatedAtDesc(projectId);

		int totalBugs = bugs.size();

		int openBugs = 0;

		for (BugEntity bug : bugs) {

			if (!"CLOSED".equalsIgnoreCase(bug.getStatus())) {

				openBugs++;
			}
		}

		List<PullRequestEntity> pullRequests = pullRequestRepository.findByProjectIdOrderByCreatedAtDesc(projectId);

		int totalPullRequests = pullRequests.size();

		int approvedPullRequests = 0;

		for (PullRequestEntity pr : pullRequests) {

			if ("APPROVED".equalsIgnoreCase(pr.getStatus())) {

				approvedPullRequests++;
			}
		}

		int progress = 0;

		if (totalTasks > 0) {

			progress = (completedTasks * 100) / totalTasks;
		}

		response.put("projectId", projectId);

		response.put("totalTasks", totalTasks);

		response.put("completedTasks", completedTasks);

		response.put("totalBugs", totalBugs);

		response.put("openBugs", openBugs);

		response.put("totalPullRequests", totalPullRequests);

		response.put("approvedPullRequests", approvedPullRequests);

		response.put("progress", progress);

		return response;
	}
}
