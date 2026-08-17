<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Settings</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/settings.css">

</head>


<body>


	<div class="settings-container">


		<!-- SIDEBAR -->

		<aside class="sidebar">


			<div class="logo">

				<span>Dev</span>Collab

			</div>


			<div class="user-section">


				<div class="avatar" id="sidebarAvatar">S</div>


				<div>

					<h3 id="sidebarName">User</h3>

					<small id="sidebarRole"> USER </small>

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
					class="menu-item"> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a
					href="${pageContext.request.contextPath}/settings.jsp"
					class="menu-item active"> ⚙️ Settings </a>


			</nav>


			<button class="logout-btn" onclick="logout()">🚪 Logout</button>


		</aside>



		<!-- MAIN CONTENT -->

		<main class="main-content"> <header class="topbar">

			<h1>Settings</h1>

			<p>Manage your DevCollab preferences</p>

		</header>



		<div id="message" class="message"></div>



		<!-- ACCOUNT -->

		<section class="settings-card">


			<div class="section-header">

				<h2>👤 Account</h2>

				<p>Your current account information</p>

			</div>


			<div class="account-grid">


				<div class="info-box">

					<span> Full Name </span> <strong id="accountName">
						Loading... </strong>

				</div>


				<div class="info-box">

					<span> Email </span> <strong id="accountEmail"> Loading...
					</strong>

				</div>


				<div class="info-box">

					<span> Role </span> <strong id="accountRole"> Loading... </strong>

				</div>


				<div class="info-box">

					<span> Status </span> <strong id="accountStatus">
						Loading... </strong>

				</div>


			</div>


		</section>



		<!-- NOTIFICATIONS -->

		<section class="settings-card">


			<div class="section-header">

				<h2>🔔 Notification Preferences</h2>

				<p>Choose which notifications you want to receive</p>

			</div>



			<div class="setting-row">


				<div>

					<h3>Email Notifications</h3>

					<p>Receive important notifications through email</p>

				</div>


				<label class="switch"> <input type="checkbox"
					id="emailNotifications"> <span class="slider"></span>

				</label>


			</div>



			<div class="setting-row">


				<div>

					<h3>Task Notifications</h3>

					<p>Get notified when tasks are assigned or updated</p>

				</div>


				<label class="switch"> <input type="checkbox"
					id="taskNotifications"> <span class="slider"></span>

				</label>


			</div>



			<div class="setting-row">


				<div>

					<h3>Project Notifications</h3>

					<p>Receive updates about your projects</p>

				</div>


				<label class="switch"> <input type="checkbox"
					id="projectNotifications"> <span class="slider"></span>

				</label>


			</div>


		</section>



		<!-- APPEARANCE -->

		<section class="settings-card">


			<div class="section-header">

				<h2>🎨 Appearance</h2>

				<p>Customize the DevCollab interface</p>

			</div>


			<div class="setting-row">


				<div>

					<h3>Dark Mode</h3>

					<p>Use a dark interface for DevCollab</p>

				</div>


				<label class="switch"> <input type="checkbox" id="darkMode">

					<span class="slider"></span>

				</label>


			</div>


		</section>



		<!-- ACTIONS -->

		<section class="actions-card">


			<button type="button" class="reset-btn" onclick="loadSettings()">

				↻ Reset</button>


			<button type="button" class="save-btn" onclick="saveSettings()">

				💾 Save Settings</button>


		</section>


		</main>


	</div>



	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<script src="${pageContext.request.contextPath}/js/settings.js"></script>


</body>

</html>