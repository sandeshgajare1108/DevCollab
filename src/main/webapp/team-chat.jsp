<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>DevCollab - Team Chat</title>

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <link rel="icon" href="data:,">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/team-chat.css">

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
                class="menu-item active">

                👥 Team

            </a>


            <a
                href="${pageContext.request.contextPath}/profile.jsp"
                class="menu-item">

                👤 Profile

            </a>

        </nav>


        <button
            class="logout-btn"
            onclick="logout()">

            🚪 Logout

        </button>

    </aside>


    <!-- =====================================================
         MAIN
    ====================================================== -->

    <main class="main-content">

        <header class="topbar">

            <div>

                <h1>
                    Team Chat
                </h1>

                <p>
                    Real-time project collaboration
                </p>

            </div>

            <div
                id="connectionStatus"
                class="connection offline">

                ● Disconnected

            </div>

        </header>


        <div
            id="message"
            class="message"
            style="display:none;">
        </div>


        <!-- =================================================
             PROJECT SELECT
        ================================================== -->

        <section class="project-selector">

            <label for="projectId">
                Project ID
            </label>

            <input
                type="number"
                id="projectId"
                min="1"
                value="1">

            <button
                type="button"
                onclick="loadChat()">

                Load Chat

            </button>

        </section>


        <!-- =================================================
             CHAT
        ================================================== -->

        <section class="chat-card">

            <div class="chat-header">

                <div>

                    <h2>
                        Project Chat
                    </h2>

                    <span id="chatProjectLabel">
                        Project #1
                    </span>

                </div>

            </div>


            <div
                id="chatMessages"
                class="chat-messages">

                <div class="empty-chat">
                    Loading messages...
                </div>

            </div>


            <form
                id="chatForm"
                class="chat-form">

                <input
                    type="text"
                    id="chatInput"
                    maxlength="1000"
                    placeholder="Type your message..."
                    autocomplete="off"
                    required>

                <button
                    type="submit">

                    Send

                </button>

            </form>

        </section>

    </main>

</div>


<script>

    var contextPath =
        "${pageContext.request.contextPath}";

</script>


<!-- SockJS -->

<script
    src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js">
</script>


<!-- STOMP -->

<script
    src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js">
</script>


<script
    src="${pageContext.request.contextPath}/js/team-chat.js">
</script>

</body>

</html>
