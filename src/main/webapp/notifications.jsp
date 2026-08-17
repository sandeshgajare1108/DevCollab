
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Notifications</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/notifications.css">

</head>


<body>


	<div class="notification-container">


		<!-- SIDEBAR -->

		<aside class="sidebar">


			<div class="logo">

				<span>Dev</span>Collab

			</div>


			<div class="user-section">


				<div class="avatar" id="sidebarAvatar">U</div>


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
					class="menu-item "> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item active"> 🔔 Notifications </a> <a href="settings.jsp"
					class="menu-item "> <span>⚙️</span> Settings
				</a> 


			</nav>


			<button class="logout-btn" onclick="logout()">🚪 Logout</button>


		</aside>



		<!-- MAIN -->

		<main class="main-content"> <header class="topbar">


			<div>

				<h1>Notifications</h1>

				<p>Stay updated with your DevCollab activities</p>

			</div>


			<div class="top-actions">


				<button onclick="markAllAsRead()" class="read-all-btn">✓
					Mark All Read</button>


				<button onclick="deleteAllNotifications()" class="clear-btn">

					🗑 Clear All</button>


			</div>


		</header>



		<div id="message" class="message"></div>



		<!-- STATS -->

		<div class="notification-stats">


			<div class="stat-card">


				<div class="stat-icon">🔔</div>


				<div>

					<span>Total</span> <strong id="totalCount"> 0 </strong>

				</div>


			</div>


			<div class="stat-card">


				<div class="stat-icon unread">🔵</div>


				<div>

					<span>Unread</span> <strong id="unreadCount"> 0 </strong>

				</div>


			</div>


		</div>



		<!-- NOTIFICATIONS -->

		<section class="notification-card">


			<div class="card-header">


				<h2>Recent Notifications</h2>


				<button onclick="loadNotifications()" class="refresh-btn">

					↻ Refresh</button>


			</div>


			<div id="notificationList" class="notification-list">

				<div class="loading">Loading notifications...</div>

			</div>


		</section>


		</main>


	</div>



	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>


	<script src="${pageContext.request.contextPath}/js/notifications.js"></script>


</body>

</html>
