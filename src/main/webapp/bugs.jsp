<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Bugs</title>

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<link rel="icon" href="data:,">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/bugs.css">

</head>

<body>

<div class="page-container">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="logo">
            <span>Dev</span>Collab
        </div>


        <div class="user-section">

            <div
                class="avatar"
                id="userAvatar">
                U
            </div>

            <div>

                <h3 id="userName">
                    User
                </h3>

                <small id="userRole">
                    USER
                </small>

            </div>

        </div>


        <nav class="menu">

            <a
                href="${pageContext.request.contextPath}/dashboard.jsp"
                class="menu-item">
                📊 Dashboard
            </a>

            <a
                href="${pageContext.request.contextPath}/project.jsp"
                class="menu-item">
                📁 Projects
            </a>

            <a
                href="${pageContext.request.contextPath}/tasks.jsp"
                class="menu-item">
                ✅ Tasks
            </a>

            <a
                href="${pageContext.request.contextPath}/team.jsp"
                class="menu-item">
                👥 Team
            </a>

            <a
                href="${pageContext.request.contextPath}/bugs.jsp"
                class="menu-item active">
                🐞 Bugs
            </a>

            <a
                href="${pageContext.request.contextPath}/profile.jsp"
                class="menu-item">
                👤 Profile
            </a>

            <a
                href="${pageContext.request.contextPath}/notifications.jsp"
                class="menu-item">
                🔔 Notifications
            </a>

        </nav>


        <button
            class="logout-btn"
            onclick="logout()">

            🚪 Logout

        </button>

    </aside>


    <!-- MAIN -->

    <main class="main-content">

        <header class="topbar">

            <div>

                <h1>
                    Bug Management
                </h1>

                <p>
                    Track, assign and resolve project bugs
                </p>

            </div>

            <button
                class="add-btn"
                onclick="openBugModal()">

                + Report Bug

            </button>

        </header>


        <div
            id="message"
            class="message"
            style="display:none;">
        </div>


        <!-- FILTER -->

        <section class="filter-card">

            <input
                type="text"
                id="searchInput"
                placeholder="Search bug...">


            <select id="statusFilter">

                <option value="ALL">
                    All Status
                </option>

                <option value="OPEN">
                    OPEN
                </option>

                <option value="ASSIGNED">
                    ASSIGNED
                </option>

                <option value="IN_PROGRESS">
                    IN PROGRESS
                </option>

                <option value="FIXED">
                    FIXED
                </option>

                <option value="RETESTING">
                    RETESTING
                </option>

                <option value="CLOSED">
                    CLOSED
                </option>

                <option value="REOPENED">
                    REOPENED
                </option>

            </select>

        </section>


        <div
            id="loading"
            class="loading">

            Loading bugs...

        </div>


        <section
            id="bugContainer"
            class="bug-grid">
        </section>


        <div
            id="emptyMessage"
            class="empty-message"
            style="display:none;">

            No bugs found.

        </div>

    </main>

</div>


<!-- REPORT BUG MODAL -->

<div
    id="bugModal"
    class="modal"
    style="display:none;">

    <div class="modal-content">

        <div class="modal-header">

            <h2>
                Report Bug
            </h2>

            <button onclick="closeBugModal()">
                ×
            </button>

        </div>


        <div class="form-group">

            <label>
                Project ID
            </label>

            <input
                type="number"
                id="projectId"
                min="1"
                required>

        </div>


        <div class="form-group">

            <label>
                Task ID
            </label>

            <input
                type="number"
                id="taskId"
                min="1"
                placeholder="Optional">

        </div>


        <div class="form-group">

            <label>
                Pull Request ID
            </label>

            <input
                type="number"
                id="pullRequestId"
                min="1"
                placeholder="Optional">

        </div>


        <div class="form-group">

            <label>
                Bug Title
            </label>

            <input
                type="text"
                id="bugTitle"
                maxlength="200"
                placeholder="Enter bug title">

        </div>


        <div class="form-group">

            <label>
                Description
            </label>

            <textarea
                id="bugDescription"
                rows="5"
                placeholder="Describe the bug">
            </textarea>

        </div>


        <div class="form-row">

            <div class="form-group">

                <label>
                    Severity
                </label>

                <select id="bugSeverity">

                    <option value="LOW">
                        LOW
                    </option>

                    <option value="MEDIUM" selected>
                        MEDIUM
                    </option>

                    <option value="HIGH">
                        HIGH
                    </option>

                    <option value="CRITICAL">
                        CRITICAL
                    </option>

                </select>

            </div>


            <div class="form-group">

                <label>
                    Priority
                </label>

                <select id="bugPriority">

                    <option value="LOW">
                        LOW
                    </option>

                    <option value="MEDIUM" selected>
                        MEDIUM
                    </option>

                    <option value="HIGH">
                        HIGH
                    </option>

                    <option value="URGENT">
                        URGENT
                    </option>

                </select>

            </div>

        </div>


        <div class="modal-actions">

            <button
                class="cancel-btn"
                onclick="closeBugModal()">

                Cancel

            </button>

            <button
                class="save-btn"
                onclick="createBug()">

                Report Bug

            </button>

        </div>

    </div>

</div>


<script>

var contextPath =
    "${pageContext.request.contextPath}";

</script>


<script
    src="${pageContext.request.contextPath}/js/bugs.js">
</script>

</body>

</html>