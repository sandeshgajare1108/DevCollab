<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">

<head>


<meta charset="UTF-8">

<title>DevCollab - Project Details</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="icon" href="data:,">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/project-details.css">


</head>

<body>

	<div class="details-container">


		<!-- =====================================================
     SIDEBAR
     ===================================================== -->

		<aside class="sidebar">

			<div class="logo">
				<span>Dev</span>Collab
			</div>

			<div class="user-section">

				<div class="avatar" id="userAvatar">D</div>

				<div>
					<h3 id="userName">User</h3>
					<small id="userRole">USER</small>
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
					class="menu-item"> 👤 Profile </a> <a
					href="${pageContext.request.contextPath}/notifications.jsp"
					class="menu-item"> 🔔 Notifications </a> <a
					href="${pageContext.request.contextPath}/settings.jsp"
					class="menu-item"> ⚙️ Settings </a>

			</nav>

			<button type="button" class="logout-btn" onclick="logout()">
				🚪 Logout</button>

		</aside>


		<!-- =====================================================
     MAIN CONTENT
     ===================================================== -->

		<main class="main-content"> <!-- TOP BAR --> <header
			class="topbar">

			<div>
				<h1>Project Details</h1>
				<p>View project information</p>
			</div>

			<button type="button" class="back-btn" onclick="goBack()">←
				Back to Projects</button>

		</header> <!-- GENERAL MESSAGE -->

		<div id="message" class="message" style="display: none;"></div>


		<!-- LOADING -->

		<div id="loading" class="loading">Loading project details...</div>


		<!-- =====================================================
         PROJECT DETAILS
         ===================================================== -->

		<section id="projectDetails" class="details-card"
			style="display: none;">

			<!-- PROJECT HEADER -->

			<div class="project-header">

				<div class="project-icon">📁</div>

				<div class="project-title">

					<h2 id="projectName">Project Name</h2>

					<p id="projectDescription">Project Description</p>

				</div>

				<span id="projectStatus" class="status planning"> Planning </span>

			</div>


			<!-- =================================================
             PROJECT INFORMATION
             ================================================= -->

			<div class="info-section">

				<h3>Project Information</h3>

				<div class="info-grid">

					<div class="info-box">
						<span class="label">Project ID</span> <strong id="projectId">-</strong>
					</div>

					<div class="info-box">
						<span class="label">Owner ID</span> <strong id="ownerId">-</strong>
					</div>

					<div class="info-box">
						<span class="label">Visibility</span> <strong id="visibility">-</strong>
					</div>

					<div class="info-box">
						<span class="label">Start Date</span> <strong id="startDate">-</strong>
					</div>

					<div class="info-box">
						<span class="label">End Date</span> <strong id="endDate">-</strong>
					</div>

					<div class="info-box">
						<span class="label">Created At</span> <strong id="createdAt">-</strong>
					</div>

				</div>

			</div>


			<!-- =================================================
             GITHUB REPOSITORY
             ================================================= -->

			<section class="github-card">

				<div class="github-header">

					<div>
						<h2>GitHub Repository</h2>
						<p>Connect this project with GitHub</p>
					</div>

				</div>


				<div id="githubMessage" class="github-message"
					style="display: none;"></div>


				<!-- CONNECT REPOSITORY -->

				<div id="githubConnectSection">

					<label for="githubUrl"> Repository URL </label> <input type="url"
						id="githubUrl" placeholder="https://github.com/owner/repository"
						autocomplete="off">

					<button type="button" onclick="connectGitHubRepository()">
						🔗 Connect Repository</button>

				</div>


				<!-- CONNECTED REPOSITORY -->

				<div id="githubRepositorySection" style="display: none;">

					<div class="github-info-row">

						<span>Repository</span> <strong id="githubRepository"> -
						</strong>

					</div>

					<div class="github-info-row">

						<span>Branch</span> <strong id="githubBranch"> - </strong>

					</div>

					<div class="github-actions">

						<button type="button" onclick="loadGitHubCommits()">📌
							Commits</button>

						<button type="button" onclick="loadGitHubPullRequests()">
							🔀 Pull Requests</button>

						<button type="button" class="disconnect-btn"
							onclick="disconnectGitHub()">🔌 Disconnect</button>

					</div>

				</div>


				<!-- =================================================
                 GITHUB COMMITS
                 ================================================= -->

				<div id="githubCommitsSection" style="display: none;">

					<h3>Latest Commits</h3>

					<div id="githubCommits"></div>

				</div>


				<!-- =================================================
                 GITHUB PULL REQUESTS
                 ================================================= -->

				<div id="githubPullsSection" style="display: none;">

					<h3>Pull Requests</h3>
					<div id="githubPullRequests"></div>

				</div>

			</section>


			<!-- =================================================
             HUMAN CODE REVIEW
             ================================================= -->

			<section class="github-card">

				<div class="github-header">

					<div>
						<h2>Human Code Review</h2>
						<p>Review Pull Request manually</p>
					</div>

				</div>


				<div id="humanReviewMessage" class="github-message"
					style="display: none;"></div>


				<div id="humanReviewForm">

					<label for="reviewPullRequestId"> Pull Request ID </label> <input
						type="number" id="reviewPullRequestId" placeholder="Example: 101"
						min="1"> <label for="reviewDecision"> Decision </label> <select
						id="reviewDecision">

						<option value="APPROVED">APPROVED</option>

						<option value="CHANGES_REQUESTED">CHANGES REQUESTED</option>

					</select> <label for="reviewComment"> Review Comment </label>

					<textarea id="reviewComment" rows="5"
						placeholder="Write your code review..."></textarea>


					<button type="button" onclick="submitHumanReview()">
						Submit Review</button>

				</div>


				<div id="humanReviewsContainer"></div>

			</section>


			<!-- =================================================
             AI CODE REVIEW
             ================================================= -->

			<section class="github-card ai-review-panel">

				<div class="github-header">

					<div>
						<h2>🤖 AI Code Review</h2>

						<p>Analyze source code for bugs, security risks and code
							quality.</p>
					</div>

				</div>


				<div id="aiReviewMessage" class="github-message"
					style="display: none;"></div>


				<!-- AI REVIEW FORM -->

				<div id="aiReviewForm">

					<label for="aiPullRequestId"> Pull Request ID </label> <input
						type="number" id="aiPullRequestId" placeholder="Example: 101"
						min="1"> <label for="aiTaskId"> Task ID </label> <input
						type="number" id="aiTaskId" placeholder="Example: 8" min="1">


					<label for="aiCodeInput"> Source Code </label>

					<textarea id="aiCodeInput" rows="18"
						placeholder="Paste Java source code here..."></textarea>


					<div class="ai-form-actions">

						<button type="button" id="aiAnalyzeButton"
							onclick="runAiCodeReview()">🤖 Analyze Code</button>

						<button type="button" class="ai-secondary-btn"
							onclick="loadLatestAiReviewFromForm()">↻ Load Latest
							Review</button>

						<button type="button" class="ai-secondary-btn"
							onclick="resetAiReview()">🔄 Reset</button>

					</div>

				</div>


				<!-- =================================================
                 AI REVIEW RESULT
                 ================================================= -->

				<div id="aiReviewResult" class="ai-review-result"
					style="display: none;">

					<div class="ai-score-summary">

						<div class="ai-score-circle">

							<span>AI Score</span> <strong id="aiScore"> -/100 </strong>

						</div>

					</div>


					<div class="info-grid">

						<div class="info-box ai-stat-box">

							<span class="label"> Bugs </span> <strong id="aiBugCount">
								0 </strong>

						</div>


						<div class="info-box ai-stat-box">

							<span class="label"> Security Issues </span> <strong
								id="aiSecurityCount"> 0 </strong>

						</div>


						<div class="info-box ai-stat-box">

							<span class="label"> Code Quality </span> <strong
								id="aiQualityCount"> 0 </strong>

						</div>


						<div class="info-box ai-stat-box">

							<span class="label"> Review Status </span> <strong
								id="aiReviewStatus"> - </strong>

						</div>

					</div>


					<!-- SCORE BAR -->

					<div class="description-section">

						<h3>AI Review Score</h3>

						<div class="ai-score-bar">

							<div id="aiScoreProgress" class="ai-score-progress"></div>

						</div>

					</div>


					<!-- FINDINGS -->

					<div class="description-section">

						<h3>🔍 Findings</h3>

						<pre id="aiFindings" class="ai-pre"></pre>

					</div>


					<!-- SUGGESTIONS -->

					<div class="description-section">

						<h3>💡 Suggestions</h3>

						<pre id="aiSuggestions" class="ai-pre"></pre>

					</div>

				</div>

			</section>


			<!-- =================================================
             PROJECT DESCRIPTION
             ================================================= -->

			<div class="description-section">

				<h3>Description</h3>

				<p id="fullDescription">-</p>

			</div>


			<!-- =================================================
             PROJECT ACTIONS
             ================================================= -->

			<div class="actions">

				<button type="button" class="edit-btn" onclick="editProject()">
					✏️ Edit Project</button>

				<button type="button" class="delete-btn" onclick="deleteProject()">
					🗑️ Delete Project</button>

			</div>

		</section>

		</main>


	</div>

	<!-- =====================================================
     CONTEXT PATH
     Must be defined BEFORE project-details.js
     ===================================================== -->

	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>

	<!-- =====================================================
     PROJECT DETAILS JAVASCRIPT
     ===================================================== -->

	<script src="${pageContext.request.contextPath}/js/project-details.js"></script>

</body>

</html>
