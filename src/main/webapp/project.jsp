
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>DevCollab - Projects</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/project.css">

</head>

<body>

	<div class="projects-container">

		<!-- ================= SIDEBAR ================= -->

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
					class="menu-item"> 📊 Dashboard </a> <a
					href="${pageContext.request.contextPath}/project.jsp"
					class="menu-item active"> 📁 Projects </a> <a
					href="${pageContext.request.contextPath}/tasks.jsp"
					class="menu-item"> ✅ Tasks </a> <a
					href="${pageContext.request.contextPath}/team.jsp"
					class="menu-item"> 👥 Team </a> <a
					href="${pageContext.request.contextPath}/profile.jsp"
					class="menu-item "> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item"> <span>⚙️</span> Settings
				</a>

			</nav>

			<button class="logout-btn" onclick="logout()">🚪 Logout</button>

		</aside>


		<!-- ================= MAIN CONTENT ================= -->

		<main class="main-content"> <!-- TOPBAR --> <header
			class="topbar">

			<div>
				<h1>Projects</h1>
				<p>Manage your DevCollab projects</p>
			</div>

			<button class="create-btn" onclick="showCreateForm()">+
				Create Project</button>

		</header> <!-- MESSAGE -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- ================= CREATE PROJECT FORM ================= -->

		<section id="projectFormSection" class="form-card"
			style="display: none;">

			<div class="form-header">

				<div>
					<h2>Create New Project</h2>
					<p>Add a new project to DevCollab</p>
				</div>

				<button class="close-btn" type="button" onclick="hideCreateForm()">
					✕</button>

			</div>


			<form id="projectForm" onsubmit="createProject(event)">

				<div class="form-grid">

					<!-- PROJECT NAME -->

					<div class="form-group">

						<label for="projectName"> Project Name </label> <input type="text"
							id="projectName" placeholder="Enter project name" required>

					</div>


					<!-- STATUS -->

					<div class="form-group">

						<label for="projectStatus"> Status </label> <select
							id="projectStatus" required>

							<option value="PLANNING">Planning</option>

							<option value="IN_PROGRESS">In Progress</option>

							<option value="COMPLETED">Completed</option>

						</select>

					</div>

				</div>


				<!-- DESCRIPTION -->

				<div class="form-group">

					<label for="projectDescription"> Description </label>

					<textarea id="projectDescription" rows="4"
						placeholder="Enter project description"></textarea>

				</div>


				<!-- BUTTONS -->

				<div class="form-actions">

					<button type="button" class="cancel-btn" onclick="hideCreateForm()">
						Cancel</button>

					<button type="submit" class="save-btn">Create Project</button>

				</div>

			</form>

		</section>


		<!-- ================= PROJECT LIST ================= -->

		<section class="projects-section">

			<div class="section-header">

				<div>
					<h2>All Projects</h2>
					<p>Your current projects</p>
				</div>

				<button class="refresh-btn" onclick="loadProjects()">🔄
					Refresh</button>

			</div>


			<!-- LOADING -->

			<div id="loading" class="loading">Loading projects...</div>


			<!-- PROJECT GRID -->

			<div id="projectsGrid" class="projects-grid"></div>


			<!-- NO PROJECTS -->

			<div id="noProjects" class="no-projects" style="display: none;">

				<div class="empty-icon">📁</div>

				<h3>No Projects Found</h3>

				<p>Create your first project to get started.</p>

				<button onclick="showCreateForm()">+ Create Project</button>

			</div>

		</section>

		</main>

	</div>


	<!-- ================= CONTEXT PATH ================= -->

	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<!-- ================= PROJECT JS ================= -->

	<script src="${pageContext.request.contextPath}/js/project.js"></script>

</body>
</html>

