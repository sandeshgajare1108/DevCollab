<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - My Profile</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/profile.css">

</head>


<body>


	<div class="profile-container">


		<!-- SIDEBAR -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>


			<div class="user-section">

				<div class="avatar" id="sidebarAvatar">S</div>

				<div>

					<h3 id="sidebarName">User</h3>

					<small id="sidebarRole"> ADMIN </small>

				</div>

			</div>


			<nav class="menu">

				<a href="${pageContext.request.contextPath}/dashboard.jsp"
					class="menu-item"> 📊 Dashboard </a> <a
					href="${pageContext.request.contextPath}/project.jsp"
					class="menu-item"> 📁 Projects </a> <a
					href="${pageContext.request.contextPath}/tasks.jsp"
					class="menu-item"> ✅ Tasks </a> <a
					href="${pageContext.request.contextPath}/team.jsp"
					class="menu-item"> 👥 Team </a> <a
					href="${pageContext.request.contextPath}/profile.jsp"
					class="menu-item active"> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item"> <span>⚙️</span> Settings
				</a>
			</nav>


			<button class="logout-btn" onclick="logout()">🚪 Logout</button>

		</aside>



		<!-- MAIN -->

		<main class="main-content"> <header class="topbar">

			<div>

				<h1>My Profile</h1>

				<p>Manage your DevCollab account</p>

			</div>

		</header> <!-- MESSAGE -->

		<div id="message" class="message"></div>



		<!-- PROFILE CARD -->

		<section class="profile-card">


			<div class="profile-header">

				<div class="large-avatar" id="profileAvatar">S</div>


				<div>

					<h2 id="displayName">Loading...</h2>

					<p id="displayRole">Loading...</p>

				</div>

			</div>



			<!-- FORM -->

			<form id="profileForm">


				<div class="form-grid">


					<!-- USER ID -->

					<div class="form-group">

						<label> User ID </label> <input type="text" id="userId" readonly>

					</div>


					<!-- FULL NAME -->

					<div class="form-group">

						<label> Full Name </label> <input type="text" id="fullName"
							required>

					</div>


					<!-- EMAIL -->

					<div class="form-group">

						<label> Email </label> <input type="email" id="email" required>

					</div>


					<!-- MOBILE -->

					<div class="form-group">

						<label> Mobile </label> <input type="text" id="mobile">

					</div>


					<!-- STATUS -->

					<div class="form-group">

						<label> Status </label> <input type="text" id="status" readonly>

					</div>


					<!-- ROLE -->

					<div class="form-group">

						<label> Role </label> <input type="text" id="role" readonly>

					</div>


					<!-- CREATED -->

					<div class="form-group">

						<label> Created At </label> <input type="text" id="createdAt"
							readonly>

					</div>


					<!-- UPDATED -->

					<div class="form-group">

						<label> Updated At </label> <input type="text" id="updatedAt"
							readonly>

					</div>


				</div>



				<!-- BUTTONS -->

				<div class="form-actions">

					<button type="button" class="cancel-btn" onclick="loadProfile()">

						Cancel</button>


					<button type="submit" class="save-btn">💾 Save Changes</button>

				</div>


			</form>


		</section>


		</main>

	</div>



	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<script src="${pageContext.request.contextPath}/js/profile.js"></script>


</body>

</html>