<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Edit Task</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/edit-task.css">

</head>

<body>

	<div class="layout">

		<!-- ================= SIDEBAR ================= -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>


			<!-- USER -->

			<div class="user-section">

				<div class="avatar" id="userAvatar">U</div>

				<div class="user-info">

					<strong id="userName"> User </strong> <span id="userRole">
						USER </span>

				</div>

			</div>


			<div class="divider"></div>


			<!-- MENU -->

			<nav class="menu">

				<a href="${pageContext.request.contextPath}/dashboard.jsp"> 📊
					Dashboard </a> <a href="${pageContext.request.contextPath}/project.jsp">
					📁 Projects </a> <a class="active"
					href="${pageContext.request.contextPath}/tasks.jsp"> ✅ Tasks </a> <a
					href="${pageContext.request.contextPath}/team.jsp"> 👥 Team </a> <a
					href="${pageContext.request.contextPath}/profile.jsp"
					class="menu-item "> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item"> <span>⚙️</span> Settings
				</a>

			</nav>


			<!-- LOGOUT -->

			<button class="logout-btn" type="button" onclick="logout()">
				🚪 Logout</button>

		</aside>


		<!-- ================= MAIN CONTENT ================= -->

		<main class="main-content"> <!-- PAGE HEADER -->

		<div class="page-header">

			<div>

				<h1>Edit Task</h1>

				<p>Update task information</p>

			</div>

			<button class="back-btn" type="button" onclick="goBack()">←
				Back to Task</button>

		</div>


		<!-- ================= MESSAGE ================= -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- ================= LOADING ================= -->

		<div id="loading" class="loading">Loading task information...</div>


		<!-- ================= FORM ================= -->

		<div id="formContainer" class="form-container" style="display: none;">


			<form id="taskForm">


				<!-- TASK ID -->

				<div class="form-group">

					<label> Task ID </label> <input type="text" id="taskId" readonly>

				</div>


				<!-- PROJECT ID -->

				<div class="form-group">

					<label for="projectId"> Project ID </label> <input type="number"
						id="projectId" required>

				</div>


				<!-- TASK NAME -->

				<div class="form-group">

					<label for="taskName"> Task Name </label> <input type="text"
						id="taskName" maxlength="150" placeholder="Enter task name"
						required>

				</div>


				<!-- DESCRIPTION -->

				<div class="form-group">

					<label for="description"> Description </label>

					<textarea id="description" rows="5"
						placeholder="Enter task description"></textarea>

				</div>


				<!-- ASSIGNED TO -->

				<div class="form-group">

					<label for="assignedTo"> Assigned To </label> <input type="number"
						id="assignedTo" placeholder="User ID">

				</div>





				<!-- STATUS -->

				<div class="form-group">

					<label for="status"> Status </label> <select id="status">

						<option value="TODO">TODO</option>

						<option value="IN_PROGRESS">IN_PROGRESS</option>

						<option value="COMPLETED">COMPLETED</option>

						<option value="CANCELLED">CANCELLED</option>

					</select>

				</div>


				<!-- PRIORITY -->

				<div class="form-group">

					<label for="priority"> Priority </label> <select id="priority">

						<option value="LOW">LOW</option>

						<option value="MEDIUM">MEDIUM</option>

						<option value="HIGH">HIGH</option>

						<option value="URGENT">URGENT</option>

					</select>

				</div>


				<!-- START DATE -->

				<div class="form-group">

					<label for="startDate"> Start Date </label> <input type="date"
						id="startDate">

				</div>


				<!-- DUE DATE -->

				<div class="form-group">

					<label for="dueDate"> Due Date </label> <input type="date"
						id="dueDate">

				</div>


				<!-- ACTIONS -->

				<div class="form-actions">

					<button type="button" class="cancel-btn" onclick="goBack()">

						Cancel</button>


					<button type="submit" class="update-btn" id="updateButton">

						💾 Update Task</button>

				</div>


			</form>

		</div>

		</main>

	</div>


	<!-- ================= CONTEXT PATH ================= -->

	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>


	<!-- ================= EDIT TASK JS ================= -->

	<script src="${pageContext.request.contextPath}/js/edit-task.js"></script>

</body>

</html>