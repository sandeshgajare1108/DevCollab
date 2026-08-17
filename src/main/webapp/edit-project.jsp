<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>DevCollab - Edit Project</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/edit-project.css">

</head>

<body>

	<div class="edit-container">

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

			<button class="logout-btn" type="button" onclick="logout()">
				🚪 Logout</button>

		</aside>


		<!-- ================= MAIN ================= -->

		<main class="main-content"> <header class="topbar">

			<div>
				<h1>Edit Project</h1>
				<p>Update project information</p>
			</div>

			<button class="back-btn" type="button" onclick="goBack()">←
				Back</button>

		</header> <!-- MESSAGE -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- LOADING -->

		<div id="loading" class="loading">Loading project...</div>


		<!-- ================= FORM ================= -->

		<section id="editCard" class="edit-card" style="display: none;">

			<form id="editProjectForm">

				<div class="form-group">

					<label> Project Name </label> <input type="text" id="projectName"
						required>

				</div>


				<div class="form-group">

					<label> Description </label>

					<textarea id="description" rows="5"></textarea>

				</div>


				<div class="form-row">

					<div class="form-group">

						<label> Owner ID </label> <input type="number" id="ownerId"
							required>

					</div>


					<div class="form-group">

						<label> Status </label> <select id="status">

							<option value="PLANNING">Planning</option>

							<option value="IN_PROGRESS">In Progress</option>

							<option value="COMPLETED">Completed</option>

						</select>

					</div>

				</div>


				<div class="form-row">

					<div class="form-group">

						<label> Visibility </label> <select id="visibility">

							<option value="PUBLIC">Public</option>

							<option value="PRIVATE">Private</option>

						</select>

					</div>


					<div class="form-group">

						<label> Start Date </label> <input type="date" id="startDate">

					</div>

				</div>


				<div class="form-group">

					<label> End Date </label> <input type="date" id="endDate">

				</div>


				<div class="actions">

					<button type="button" class="cancel-btn" onclick="goBack()">
						Cancel</button>

					<button type="submit" class="save-btn">💾 Update Project</button>

				</div>

			</form>

		</section>

		</main>

	</div>


	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<script src="${pageContext.request.contextPath}/js/edit-project.js"></script>

</body>
</html>