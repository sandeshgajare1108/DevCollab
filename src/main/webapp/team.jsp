
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Team</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/team.css">

</head>

<body>

	<div class="page-container">

		<!-- ================= SIDEBAR ================= -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>


			<!-- USER -->

			<div class="user-section">

				<div class="avatar" id="userAvatar">U</div>

				<div>

					<h3 id="userName">User</h3>

					<small id="userRole"> USER </small>

				</div>

			</div>


			<!-- MENU -->

			<nav class="menu">

				<a href="${pageContext.request.contextPath}/dashboard.jsp"
					class="menu-item"> 📊 Dashboard </a> <a
					href="${pageContext.request.contextPath}/project.jsp"
					class="menu-item"> 📁 Projects </a> <a
					href="${pageContext.request.contextPath}/tasks.jsp"
					class="menu-item"> ✅ Tasks </a> <a
					href="${pageContext.request.contextPath}/team.jsp"
					class="menu-item active"> 👥 Team </a> <a
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

		<main class="main-content"> <!-- HEADER --> <header
			class="topbar">

			<div>

				<h1>Team</h1>

				<p>Manage project team members</p>

			</div>

			<a href="${pageContext.request.contextPath}/team-chat.jsp"
				class="chat-link"> 💬 Team Chat </a>
			<button class="add-btn" type="button" onclick="openAddMemberModal()">

				+ Add Member</button>

		</header> <!-- MESSAGE -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- ================= FILTER ================= -->

		<section class="filter-card">

			<input type="text" id="searchInput"
				placeholder="Search team member..." autocomplete="off"> <input
				type="number" id="projectFilter" placeholder="Project ID"> <select
				id="roleFilter">

				<option value="ALL">All Roles</option>

				<option value="ADMIN">ADMIN</option>

				<option value="MANAGER">MANAGER</option>

				<option value="MEMBER">MEMBER</option>

				<option value="DEVELOPER">DEVELOPER</option>

				<option value="TESTER">TESTER</option>

			</select>


			<button type="button" class="clear-btn" onclick="clearFilters()">

				Clear</button>

		</section>


		<!-- ================= LOADING ================= -->

		<div id="loading" class="loading">Loading team members...</div>


		<!-- ================= TEAM GRID ================= -->

		<section id="teamContainer" class="team-grid"></section>


		<!-- ================= EMPTY ================= -->

		<div id="emptyMessage" class="empty-message" style="display: none;">

			No team members found.</div>

		</main>

	</div>


	<!-- =====================================================
     ADD / EDIT MEMBER MODAL
     ===================================================== -->

	<div id="memberModal" class="modal" style="display: none;">

		<div class="modal-content">


			<!-- MODAL HEADER -->

			<div class="modal-header">

				<h2 id="modalTitle">Add Team Member</h2>


				<button type="button" onclick="closeMemberModal()">×</button>

			</div>


			<!-- FORM -->

			<form id="memberForm">


				<!-- TEAM MEMBER ID -->

				<input type="hidden" id="teamMemberId">


				<!-- PROJECT ID -->

				<div class="form-group">

					<label for="projectId"> Project ID </label> <input type="number"
						id="projectId" min="1" placeholder="Enter project ID" required>

				</div>


				<!-- USER ID -->

				<div class="form-group">

					<label for="userId"> User ID </label> <input type="number"
						id="userId" min="1" placeholder="Enter user ID" required>

				</div>


				<!-- ROLE -->

				<div class="form-group">

					<label for="memberRole"> Role </label> <select id="memberRole">

						<option value="MEMBER">MEMBER</option>

						<option value="ADMIN">ADMIN</option>

						<option value="MANAGER">MANAGER</option>

						<option value="DEVELOPER">DEVELOPER</option>

						<option value="TESTER">TESTER</option>

					</select>

				</div>


				<!-- ACTIONS -->

				<div class="modal-actions">

					<button type="button" class="cancel-btn"
						onclick="closeMemberModal()">Cancel</button>


					<button type="submit" class="save-btn" id="saveMemberButton">

						Add Member</button>

				</div>

			</form>

		</div>

	</div>


	<!-- ================= CONTEXT PATH ================= -->

	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>


	<!-- ================= TEAM JS ================= -->

	<script src="${pageContext.request.contextPath}/js/team.js">
		
	</script>


</body>

</html>
