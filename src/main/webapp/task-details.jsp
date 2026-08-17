
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Task Details</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/task-details.css">

</head>

<body>

	<div class="layout">

		<!-- ================= SIDEBAR ================= -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>

			<div class="user-section">

				<div class="avatar" id="userAvatar">U</div>

				<div class="user-info">

					<strong id="userName"> User </strong> <span id="userRole">
						USER </span>

				</div>

			</div>

			<div class="divider"></div>

			<nav class="menu">

				<a href="${pageContext.request.contextPath}/dashboard.jsp"> 📊
					Dashboard </a> <a href="${pageContext.request.contextPath}/project.jsp">
					📁 Projects </a> <a class="active"
					href="${pageContext.request.contextPath}/tasks.jsp"> ✅ Tasks </a> <a
					href="${pageContext.request.contextPath}/team.jsp"> 👥 Team </a> <a
					href="${pageContext.request.contextPath}/profile.jsp"
					class="menu-item s"> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item"> <span>⚙️</span> Settings
				</a>

			</nav>

			<button class="logout-btn" type="button" onclick="logout()">

				🚪 Logout</button>

		</aside>


		<!-- ================= MAIN ================= -->

		<main class="main-content"> <!-- HEADER -->

		<div class="page-header">

			<div>

				<h1>Task Details</h1>

				<p>View complete task information</p>

			</div>

			<div class="header-actions">

				<button class="back-btn" type="button" onclick="goBack()">

					← Back to Tasks</button>

				<button class="edit-btn" type="button" onclick="editTask()">

					✏ Edit Task</button>

				<button class="delete-btn" type="button" onclick="deleteTask()">

					🗑 Delete</button>

			</div>

		</div>


		<!-- MESSAGE -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- LOADING -->

		<div id="loading" class="loading">Loading task details...</div>


		<!-- TASK DETAILS -->

		<div id="taskContainer" class="details-container"
			style="display: none;">

			<!-- TASK HEADER CARD -->

			<section class="task-header-card">

				<div class="task-title-section">

					<span id="taskId" class="task-id"> #- </span>

					<h2 id="taskName">Task Name</h2>

					<p id="taskDescription">No description</p>

				</div>

				<div class="task-status-section">

					<span id="taskStatus" class="status"> TODO </span> <span
						id="taskPriority" class="priority"> MEDIUM </span>

				</div>

			</section>


			<!-- INFORMATION -->

			<section class="info-card">

				<div class="card-title">Task Information</div>


				<div class="info-grid">

					<!-- PROJECT -->

					<div class="info-item">

						<span class="label"> 📁 Project ID </span> <strong id="projectId">
							- </strong>

					</div>


					<!-- ASSIGNED -->

					<div class="info-item">

						<span class="label"> 👤 Assigned To </span> <strong
							id="assignedTo"> - </strong>

					</div>


					<!-- CREATED BY -->

					<div class="info-item">

						<span class="label"> 👨‍💻 Created By </span> <strong
							id="createdBy"> - </strong>

					</div>


					<!-- START DATE -->

					<div class="info-item">

						<span class="label"> 📅 Start Date </span> <strong id="startDate">
							- </strong>

					</div>


					<!-- DUE DATE -->

					<div class="info-item">

						<span class="label"> ⏰ Due Date </span> <strong id="dueDate">
							- </strong>

					</div>


					<!-- CREATED -->

					<div class="info-item">

						<span class="label"> 🕐 Created At </span> <strong id="createdAt">
							- </strong>

					</div>


					<!-- UPDATED -->

					<div class="info-item">

						<span class="label"> 🔄 Updated At </span> <strong id="updatedAt">
							- </strong>

					</div>

				</div>

			</section>


			<!-- DESCRIPTION -->

			<section class="description-card">

				<div class="card-title">Description</div>

				<p id="fullDescription">No description available.</p>

			</section>

		</div>

		</main>

	</div>


	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>


	<script src="${pageContext.request.contextPath}/js/task-details.js"></script>

</body>

</html>
