<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>DevCollab - Dashboard</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dashboard.css">

</head>

<body>

	<div class="dashboard-container">

		<!-- SIDEBAR -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>

			<div class="user-section">

				<div class="avatar" id="userAvatar">D</div>

				<div>
					<h3 id="userName">User</h3>
					<small id="userRole">ADMIN</small>
				</div>

			</div>

			<nav class="menu">

				<a href="${pageContext.request.contextPath}/dashboard.jsp"
					class="menu-item active"> <span>📊</span> Dashboard
				</a> <a href="${pageContext.request.contextPath}/project.jsp"
					class="menu-item"> <span>📁</span> Projects
				</a> <a href="${pageContext.request.contextPath}/tasks.jsp"
					class="menu-item"> <span>✅</span> Tasks
				</a> <a href="${pageContext.request.contextPath}/team.jsp"
					class="menu-item"> <span>👥</span> Team
				</a> <a href="${pageContext.request.contextPath}/profile.jsp"
					class="menu-item "> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/bugs.jsp"
					class="menu-item"> 🐞 Bugs </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item"> <span>⚙️</span> Settings
				</a>

			</nav>

			<button class="logout-btn" onclick="logout()">🚪 Logout</button>

		</aside>


		<!-- MAIN CONTENT -->

		<main class="main-content"> <!-- TOP BAR --> <header
			class="topbar">

			<div>
				<h1>Dashboard</h1>
				<p>Welcome back to DevCollab 👋</p>
			</div>

			<div class="top-right">

				<div class="notification">🔔</div>

				<div class="profile">

					<div class="profile-avatar" id="profileAvatar">D</div>

					<span id="profileName"> User </span>

				</div>

			</div>

		</header> <!-- LOADING -->

		<div id="loadingMessage" class="loading-message">Loading
			dashboard...</div>


		<!-- ERROR -->

		<div id="errorMessage" class="error-message" style="display: none;">

		</div>


		<!-- STATISTICS -->

		<section class="stats-grid">


			<!-- TOTAL PROJECTS -->

			<div class="stat-card">

				<div class="stat-icon project-icon">📁</div>

				<div>

					<p>Total Projects</p>

					<h2 id="totalProjects">0</h2>

					<span>All Projects</span>

				</div>

			</div>


			<!-- ACTIVE PROJECTS -->

			<div class="stat-card">

				<div class="stat-icon active-icon">🚀</div>

				<div>

					<p>Active Projects</p>

					<!-- IMPORTANT ID -->

					<h2 id="inProgressProjects">0</h2>

					<span>In Progress</span>

				</div>

			</div>


			<!-- COMPLETED PROJECTS -->

			<div class="stat-card">

				<div class="stat-icon completed-icon">✔</div>

				<div>

					<p>Completed Projects</p>

					<h2 id="completedProjects">0</h2>

					<span>Successfully completed</span>

				</div>

			</div>


			<!-- TOTAL TASKS -->

			<div class="stat-card">

				<div class="stat-icon task-icon">📝</div>

				<div>

					<p>Total Tasks</p>

					<h2 id="totalTasks">0</h2>

					<span>Assigned Tasks</span>

				</div>

			</div>

		</section>


		<!-- CONTENT GRID -->

		<section class="content-grid">


			<!-- PROJECT OVERVIEW -->

			<div class="card">

				<div class="card-header">

					<div>

						<h2>Project Overview</h2>

						<p>Current project status</p>

					</div>

					<button class="view-btn" onclick="viewProjects()">View All

					</button>

				</div>


				<div class="project-status">


					<!-- PLANNING -->

					<div class="status-row">

						<div class="status-info">

							<span class="dot planning"></span> <span>Planning</span>

						</div>

						<!-- IMPORTANT ID -->

						<strong id="planningProjects">0</strong>

					</div>


					<div class="progress-bar">

						<div class="progress planning-progress" id="planningProgress">
						</div>

					</div>


					<!-- IN PROGRESS -->

					<div class="status-row">

						<div class="status-info">

							<span class="dot progress"></span> <span>In Progress</span>

						</div>

						<!-- IMPORTANT ID -->

						<strong id="projectInProgress">0</strong>

					</div>


					<div class="progress-bar">

						<div class="progress progress-project" id="projectProgress">
						</div>

					</div>


					<!-- COMPLETED -->

					<div class="status-row">

						<div class="status-info">

							<span class="dot completed"></span> <span>Completed</span>

						</div>

						<!-- IMPORTANT ID -->

						<strong id="projectCompleted">0</strong>

					</div>


					<div class="progress-bar">

						<div class="progress completed-progress" id="completedProgress">
						</div>

					</div>

				</div>

			</div>


			<!-- TASK OVERVIEW -->

			<div class="card">

				<div class="card-header">

					<div>

						<h2>Task Overview</h2>

						<p>Task distribution</p>

					</div>

				</div>


				<div class="task-list">


					<div class="task-item">

						<div class="task-left">

							<span class="task-dot todo"></span> <span>To Do</span>

						</div>

						<strong id="todoTasks">0</strong>

					</div>


					<div class="task-item">

						<div class="task-left">

							<span class="task-dot task-progress"></span> <span>In
								Progress</span>

						</div>

						<strong id="inProgressTasks">0</strong>

					</div>


					<div class="task-item">

						<div class="task-left">

							<span class="task-dot review"></span> <span>Review</span>

						</div>

						<strong id="reviewTasks">0</strong>

					</div>


					<div class="task-item">

						<div class="task-left">

							<span class="task-dot task-completed"></span> <span>Completed</span>

						</div>

						<strong id="completedTasks">0</strong>

					</div>

				</div>

			</div>

		</section>

		<!-- =====================================================
     TEAM OVERVIEW
===================================================== -->

		<section class="team-overview">

			<div class="card">

				<div class="card-header">

					<div>

						<h2>Team Overview</h2>

						<p>Project member distribution</p>

					</div>

					<button class="view-btn" onclick="viewTeam()">View Team</button>

				</div>


				<!-- TOTAL MEMBERS -->

				<div class="task-item">

					<div class="task-left">

						<span>👥 Total Members</span>

					</div>

					<strong id="totalMembers"> 0 </strong>

				</div>


				<!-- OWNER -->

				<div class="task-item">

					<div class="task-left">

						<span>👑 Owners</span>

					</div>

					<strong id="ownerMembers"> 0 </strong>

				</div>


				<!-- DEVELOPER -->

				<div class="task-item">

					<div class="task-left">

						<span>💻 Developers</span>

					</div>

					<strong id="developerMembers"> 0 </strong>

				</div>


				<!-- TESTER -->

				<div class="task-item">

					<div class="task-left">

						<span>🧪 Testers</span>

					</div>

					<strong id="testerMembers"> 0 </strong>

				</div>


				<!-- DESIGNER -->

				<div class="task-item">

					<div class="task-left">

						<span>🎨 Designers</span>

					</div>

					<strong id="designerMembers"> 0 </strong>

				</div>


				<!-- MANAGER -->

				<div class="task-item">

					<div class="task-left">

						<span>📋 Managers</span>

					</div>

					<strong id="managerMembers"> 0 </strong>

				</div>

			</div>

		</section>
		<!-- QUICK ACTIONS -->

		<section class="quick-section">

			<h2>Quick Actions</h2>

			<div class="quick-grid">


				<div class="quick-card" onclick="createProject()">

					<div class="quick-icon">➕</div>

					<div>

						<h3>Create Project</h3>

						<p>Start a new project</p>

					</div>

				</div>


				<div class="quick-card" onclick="createTask()">

					<div class="quick-icon">📝</div>

					<div>

						<h3>Create Task</h3>

						<p>Add a new task</p>

					</div>

				</div>


				<div class="quick-card" onclick="viewTeam()">

					<div class="quick-icon">👥</div>

					<div>

						<h3>Manage Team</h3>

						<p>View development team</p>

					</div>

				</div>

			</div>
			<div class="progress-card">

				<div class="progress-header">

					<div>
						<h3>Project Progress</h3>
						<p>Project #1</p>
					</div>

					<strong id="projectProgressPercent"> 0% </strong>

				</div>


				<div class="progress-bar">

					<div id="projectProgressBar" class="progress-fill"></div>

				</div>


				<div class="progress-stats">

					<span> Tasks: <strong id="completedTasks"> 0 </strong> / <strong
						id="totalTasks"> 0 </strong>
					</span> <span> Open Bugs: <strong id="openBugs"> 0 </strong>
					</span> <span> Approved PRs: <strong id="approvedPullRequests">
							0 </strong>
					</span>

				</div>

			</div>

		</section>

		</main>

	</div>


	<!-- CONTEXT PATH -->

	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<!-- DASHBOARD JS -->

	<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>

</html>