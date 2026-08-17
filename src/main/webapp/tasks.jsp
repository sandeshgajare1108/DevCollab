<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>DevCollab - Tasks</title>

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<link rel="icon" href="data:,">

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/tasks.css">

</head>

<body>

<div class="page-container">

    <!-- =====================================================
         SIDEBAR
    ====================================================== -->

    <aside class="sidebar">

        <div class="logo">
            <span>Dev</span>Collab
        </div>


        <div class="user-section">

            <div class="avatar"
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


        <!-- =================================================
             MENU
        ================================================== -->

        <nav class="menu">

            <a href="${pageContext.request.contextPath}/dashboard.jsp"
               class="menu-item">
                📊 Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/project.jsp"
               class="menu-item">
                📁 Projects
            </a>

            <a href="${pageContext.request.contextPath}/tasks.jsp"
               class="menu-item active">
                ✅ Tasks
            </a>

            <a href="${pageContext.request.contextPath}/team.jsp"
               class="menu-item">
                👥 Team
            </a>

            <a href="${pageContext.request.contextPath}/profile.jsp"
               class="menu-item">
                👤 Profile
            </a>

            <a href="${pageContext.request.contextPath}/notifications.jsp"
               class="menu-item">
                🔔 Notifications
            </a>

            <a href="${pageContext.request.contextPath}/settings.jsp"
               class="menu-item">
                ⚙️ Settings
            </a>

        </nav>


        <!-- =================================================
             LOGOUT
        ================================================== -->

        <button class="logout-btn"
                type="button"
                onclick="logout()">

            🚪 Logout

        </button>

    </aside>


    <!-- =====================================================
         MAIN CONTENT
    ====================================================== -->

    <main class="main-content">


        <!-- =================================================
             HEADER
        ================================================== -->

        <header class="topbar">

            <div>

                <h1>Tasks</h1>

                <p>
                    Manage project tasks
                </p>

            </div>


            <button class="add-btn"
                    type="button"
                    onclick="openCreateTask()">

                + Add Task

            </button>

        </header>


        <!-- =================================================
             MESSAGE
        ================================================== -->

        <div id="message"
             class="message"
             style="display:none;">
        </div>


        <!-- =================================================
             FILTER
        ================================================== -->

        <section class="filter-card">

            <input
                type="text"
                id="searchInput"
                placeholder="Search task..."
                autocomplete="off">


            <select id="statusFilter">

                <option value="ALL">
                    All Status
                </option>

                <option value="TODO">
                    TODO
                </option>

                <option value="IN_PROGRESS">
                    IN PROGRESS
                </option>

                <option value="CODE_REVIEW">
                    CODE REVIEW
                </option>

                <option value="TESTING">
                    TESTING
                </option>

                <option value="BUG_FOUND">
                    BUG FOUND
                </option>

                <option value="FIXING">
                    FIXING
                </option>

                <option value="RETESTING">
                    RETESTING
                </option>

                <option value="APPROVED">
                    APPROVED
                </option>

                <option value="COMPLETED">
                    COMPLETED
                </option>

            </select>

        </section>


        <!-- =================================================
             LOADING
        ================================================== -->

        <div id="loading"
             class="loading">

            Loading tasks...

        </div>


        <!-- =================================================
             TASK CONTAINER
        ================================================== -->

        <section id="taskContainer"
                 class="task-grid">
        </section>


        <!-- =================================================
             EMPTY
        ================================================== -->

        <div id="emptyMessage"
             class="empty-message"
             style="display:none;">

            No tasks found.

        </div>

    </main>

</div>


<!-- =====================================================
     CREATE TASK MODAL
====================================================== -->

<div id="taskModal"
     class="modal"
     style="display:none;">

    <div class="modal-content">


        <div class="modal-header">

            <h2>
                Create Task
            </h2>

            <button type="button"
                    onclick="closeTaskModal()">
                ×
            </button>

        </div>


        <form id="taskForm">


            <!-- TASK NAME -->

            <div class="form-group">

                <label for="taskTitle">
                    Task Title
                </label>

                <input
                    type="text"
                    id="taskTitle"
                    maxlength="150"
                    placeholder="Enter task title"
                    required>

            </div>


            <!-- DESCRIPTION -->

            <div class="form-group">

                <label for="taskDescription">
                    Description
                </label>

                <textarea
                    id="taskDescription"
                    rows="4"
                    placeholder="Enter task description">
                </textarea>

            </div>


            <!-- PROJECT + ASSIGNED -->

            <div class="form-row">


                <div class="form-group">

                    <label for="projectId">
                        Project ID
                    </label>

                    <input
                        type="number"
                        id="projectId"
                        min="1"
                        required>

                </div>


                <div class="form-group">

                    <label for="assignedTo">
                        Assigned To
                    </label>

                    <input
                        type="number"
                        id="assignedTo"
                        min="1"
                        placeholder="User ID">

                </div>

            </div>


            <!-- STATUS + PRIORITY -->

            <div class="form-row">


                <div class="form-group">

                    <label for="taskStatus">
                        Initial Status
                    </label>

                    <select id="taskStatus">

                        <option value="TODO">
                            TODO
                        </option>

                        <option value="IN_PROGRESS">
                            IN_PROGRESS
                        </option>

                    </select>

                </div>


                <div class="form-group">

                    <label for="priority">
                        Priority
                    </label>

                    <select id="priority">

                        <option value="LOW">
                            LOW
                        </option>

                        <option value="MEDIUM"
                                selected>
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


            <!-- DATES -->

            <div class="form-row">


                <div class="form-group">

                    <label for="startDate">
                        Start Date
                    </label>

                    <input
                        type="date"
                        id="startDate">

                </div>


                <div class="form-group">

                    <label for="dueDate">
                        Due Date
                    </label>

                    <input
                        type="date"
                        id="dueDate">

                </div>

            </div>


            <!-- ACTIONS -->

            <div class="modal-actions">


                <button
                    type="button"
                    class="cancel-btn"
                    onclick="closeTaskModal()">

                    Cancel

                </button>


                <button
                    type="submit"
                    class="save-btn"
                    id="saveTaskButton">

                    Save Task

                </button>

            </div>

        </form>

    </div>

</div>


<!-- =====================================================
     CONTEXT PATH
====================================================== -->

<script>

    var contextPath =
        "${pageContext.request.contextPath}";

</script>


<!-- =====================================================
     TASK JS
====================================================== -->

<script
    src="${pageContext.request.contextPath}/js/tasks.js">
</script>

</body>

</html>