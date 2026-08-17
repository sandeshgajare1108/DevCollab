
// =====================================================
// DEV COLLAB - PROJECT DETAILS FINAL JS
// COMPLETE CORRECTED VERSION
// =====================================================


// =====================================================
// GLOBAL CONTEXT PATH SAFETY
// =====================================================

if (typeof contextPath === "undefined") {
    var contextPath = "";
}


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("DevCollab Project Details JS Loaded");
    console.log("=================================");

    loadUserData();
    loadProjectDetails();
    loadGitHubRepository();
    loadDevCollabPullRequests();

});


// =====================================================
// LOAD USER DATA
// =====================================================

function loadUserData() {

    var fullName = localStorage.getItem("fullName");
    var role = localStorage.getItem("role");

    var userName = document.getElementById("userName");
    var userRole = document.getElementById("userRole");
    var avatar = document.getElementById("userAvatar");

    if (fullName && fullName.trim() !== "") {

        if (userName) {
            userName.innerText = fullName;
        }

        if (avatar) {
            avatar.innerText =
                fullName.charAt(0).toUpperCase();
        }
    }

    if (role && userRole) {
        userRole.innerText = role.toUpperCase();
    }
}


// =====================================================
// GET PROJECT ID
// =====================================================

function getProjectId() {

    var params =
        new URLSearchParams(window.location.search);

    return params.get("projectId");
}


// =====================================================
// GET JWT TOKEN
// =====================================================

function getToken() {

    return localStorage.getItem("token");
}


// =====================================================
// COMMON API RESPONSE PARSER
// =====================================================

async function parseApiResponse(response) {

    var text = await response.text();

    if (!text || text.trim() === "") {
        return {};
    }

    try {

        return JSON.parse(text);

    } catch (error) {

        return {
            message: text
        };
    }
}


// =====================================================
// LOAD PROJECT DETAILS
// =====================================================

function loadProjectDetails() {

    var token = getToken();
    var projectId = getProjectId();

    if (!token) {

        hideLoading();

        showMessage(
            "Login session expired. Please login again.",
            "error"
        );

        return;
    }

    if (!projectId) {

        hideLoading();

        showMessage(
            "Project ID not found in URL.",
            "error"
        );

        return;
    }

    var loading =
        document.getElementById("loading");

    if (loading) {
        loading.style.display = "block";
    }

    fetch(
        contextPath +
        "/api/projects/" +
        encodeURIComponent(projectId),
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        console.log(
            "Project API HTTP Status:",
            response.status
        );

        var data =
            await parseApiResponse(response);

        if (response.ok) {
            return data;
        }

        if (response.status === 401) {

            throw new Error(
                "401 Unauthorized - JWT token is invalid or expired."
            );
        }

        if (response.status === 403) {

            throw new Error(
                "403 Forbidden - You don't have permission to view this project."
            );
        }

        if (response.status === 404) {

            throw new Error(
                "404 Project not found."
            );
        }

        throw new Error(
            data.message ||
            data.error ||
            "Project API Error - HTTP " +
            response.status
        );

    })

    .then(function (project) {

        displayProject(project);

    })

    .catch(function (error) {

        console.error(
            "Project Details Error:",
            error
        );

        hideLoading();

        showMessage(
            error.message ||
            "Unable to load project details.",
            "error"
        );
    });
}


// =====================================================
// DISPLAY PROJECT
// =====================================================

function displayProject(project) {

    hideLoading();

    if (!project) {

        showMessage(
            "Project data not found.",
            "error"
        );

        return;
    }

    var details =
        document.getElementById("projectDetails");

    if (details) {
        details.style.display = "block";
    }

    setText(
        "projectId",
        project.projectId
    );

    setText(
        "projectName",
        project.projectName ||
        "Unnamed Project"
    );

    setText(
        "projectDescription",
        project.description ||
        "No description available"
    );

    setText(
        "fullDescription",
        project.description ||
        "No description available"
    );

    setText(
        "ownerId",
        project.ownerId
    );

    setText(
        "visibility",
        project.visibility
    );

    setText(
        "startDate",
        formatDate(project.startDate)
    );

    setText(
        "endDate",
        formatDate(project.endDate)
    );

    setText(
        "createdAt",
        formatDate(project.createdAt)
    );

    var status =
        project.status || "PLANNING";

    var statusElement =
        document.getElementById("projectStatus");

    if (statusElement) {

        statusElement.innerText =
            formatStatus(status);

        statusElement.className =
            "status " +
            getStatusClass(status);
    }
}


// =====================================================
// SET TEXT
// =====================================================

function setText(id, value) {

    var element =
        document.getElementById(id);

    if (!element) {
        return;
    }

    if (
        value === null ||
        value === undefined ||
        String(value).trim() === ""
    ) {

        element.innerText = "-";

    } else {

        element.innerText = String(value);
    }
}


// =====================================================
// FORMAT STATUS
// =====================================================

function formatStatus(status) {

    if (!status) {
        return "Planning";
    }

    return String(status)
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(/\b\w/g, function (letter) {
            return letter.toUpperCase();
        });
}


// =====================================================
// STATUS CSS CLASS
// =====================================================

function getStatusClass(status) {

    status =
        String(status || "").toUpperCase();

    if (status === "COMPLETED") {
        return "completed";
    }

    if (
        status === "IN_PROGRESS" ||
        status === "IN PROGRESS"
    ) {
        return "in-progress";
    }

    return "planning";
}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(value) {

    if (!value) {
        return "-";
    }

    try {

        var date =
            new Date(value);

        if (isNaN(date.getTime())) {

            return String(value)
                .replace("T", " ");
        }

        return date.toLocaleString();

    } catch (error) {

        return String(value)
            .replace("T", " ");
    }
}


// =====================================================
// HIDE LOADING
// =====================================================

function hideLoading() {

    var loading =
        document.getElementById("loading");

    if (loading) {
        loading.style.display = "none";
    }
}


// =====================================================
// BACK
// =====================================================

function goBack() {

    window.location.href =
        contextPath + "/project.jsp";
}


// =====================================================
// EDIT PROJECT
// =====================================================

function editProject() {

    var projectId =
        getProjectId();

    if (!projectId) {

        showMessage(
            "Project ID not found.",
            "error"
        );

        return;
    }

    window.location.href =
        contextPath +
        "/edit-project.jsp?projectId=" +
        encodeURIComponent(projectId);
}


// =====================================================
// DELETE PROJECT
// =====================================================

function deleteProject() {

    var projectId = getProjectId();
    var token = getToken();

    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }

    if (!projectId) {

        showMessage(
            "Project ID not found.",
            "error"
        );

        return;
    }

    if (
        !confirm(
            "Are you sure you want to delete this project?"
        )
    ) {
        return;
    }

    fetch(
        contextPath +
        "/api/projects/" +
        encodeURIComponent(projectId),
        {
            method: "DELETE",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (response.status === 401) {

            throw new Error(
                "401 Unauthorized - Please login again."
            );
        }

        if (response.status === 403) {

            throw new Error(
                "403 Forbidden - You don't have permission to delete this project."
            );
        }

        if (response.status === 404) {

            throw new Error(
                "404 Project not found."
            );
        }

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Delete failed. HTTP " +
                response.status
            );
        }

        return data;
    })

    .then(function (data) {

        alert(
            data.message ||
            "Project deleted successfully."
        );

        window.location.href =
            contextPath + "/project.jsp";
    })

    .catch(function (error) {

        console.error(
            "Delete Project Error:",
            error
        );

        showMessage(
            error.message ||
            "Unable to delete project.",
            "error"
        );
    });
}


// =====================================================
// GITHUB REPOSITORY
// =====================================================

function loadGitHubRepository() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {
        return;
    }

    fetch(
        contextPath +
        "/api/github/repositories/project/" +
        encodeURIComponent(projectId),
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (response.status === 404) {

            showGitHubConnectSection();

            return null;
        }

        if (response.status === 401) {

            throw new Error(
                "GitHub repository request unauthorized."
            );
        }

        if (response.status === 403) {

            throw new Error(
                "You don't have permission to access GitHub repository."
            );
        }

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load GitHub repository."
            );
        }

        return data;
    })

    .then(function (data) {

        if (!data) {
            return;
        }

        showGitHubRepository(data);

        loadGitHubCommits();
        loadGitHubPullRequests();
    })

    .catch(function (error) {

        console.error(
            "GitHub Load Error:",
            error
        );

        showGitHubMessage(
            error.message ||
            "Unable to load GitHub repository.",
            "error"
        );
    });
}


// =====================================================
// CONNECT GITHUB
// =====================================================

function connectGitHubRepository() {

    var projectId = getProjectId();
    var token = getToken();

    var githubInput =
        document.getElementById("githubUrl");

    if (!projectId || !token) {

        showGitHubMessage(
            "Project ID or JWT token missing.",
            "error"
        );

        return;
    }

    if (!githubInput) {

        showGitHubMessage(
            "GitHub URL input field not found.",
            "error"
        );

        return;
    }

    var githubUrl =
        githubInput.value.trim();

    if (!githubUrl) {

        showGitHubMessage(
            "GitHub repository URL is required.",
            "error"
        );

        return;
    }

    if (
        !/^https:\/\/github\.com\/[^\/]+\/[^\/]+\/?$/.test(
            githubUrl
        )
    ) {

        showGitHubMessage(
            "Enter a valid GitHub repository URL.",
            "error"
        );

        return;
    }

    fetch(
        contextPath +
        "/api/github/repositories/connect",
        {
            method: "POST",

            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json",
                "Accept": "application/json"
            },

            body: JSON.stringify({

                projectId:
                    Number(projectId),

                githubUrl:
                    githubUrl
            })
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (response.status === 401) {

            throw new Error(
                "401 Unauthorized - Please login again."
            );
        }

        if (response.status === 403) {

            throw new Error(
                "403 Forbidden - You cannot connect this repository."
            );
        }

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to connect GitHub repository."
            );
        }

        return data;
    })

    .then(function (data) {

        showGitHubRepository(data);

        showGitHubMessage(
            "GitHub repository connected successfully.",
            "success"
        );

        loadGitHubCommits();
        loadGitHubPullRequests();
    })

    .catch(function (error) {

        console.error(
            "GitHub Connect Error:",
            error
        );

        showGitHubMessage(
            error.message ||
            "Failed to connect GitHub repository.",
            "error"
        );
    });
}


// =====================================================
// DISPLAY GITHUB REPOSITORY
// =====================================================

function showGitHubRepository(data) {

    var connectSection =
        document.getElementById(
            "githubConnectSection"
        );

    var repositorySection =
        document.getElementById(
            "githubRepositorySection"
        );

    if (connectSection) {
        connectSection.style.display = "none";
    }

    if (repositorySection) {
        repositorySection.style.display = "block";
    }

    var repository =
        document.getElementById(
            "githubRepository"
        );

    var branch =
        document.getElementById(
            "githubBranch"
        );

    if (repository) {

        var owner =
            data.githubOwner ||
            data.owner ||
            "-";

        var repo =
            data.githubRepo ||
            data.repositoryName ||
            data.repo ||
            "-";

        repository.innerText =
            owner + "/" + repo;
    }

    if (branch) {

        branch.innerText =
            data.defaultBranch ||
            data.branch ||
            "-";
    }
}


// =====================================================
// SHOW GITHUB CONNECT SECTION
// =====================================================

function showGitHubConnectSection() {

    var connectSection =
        document.getElementById(
            "githubConnectSection"
        );

    var repositorySection =
        document.getElementById(
            "githubRepositorySection"
        );

    var commitsSection =
        document.getElementById(
            "githubCommitsSection"
        );

    var pullSection =
        document.getElementById(
            "githubRawPullsSection"
        );

    if (connectSection) {
        connectSection.style.display = "block";
    }

    if (repositorySection) {
        repositorySection.style.display = "none";
    }

    if (commitsSection) {
        commitsSection.style.display = "none";
    }

    if (pullSection) {
        pullSection.style.display = "none";
    }
}


// =====================================================
// LOAD GITHUB COMMITS
// =====================================================

function loadGitHubCommits() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {
        return;
    }

    fetch(
        contextPath +
        "/api/github/repositories/project/" +
        encodeURIComponent(projectId) +
        "/commits",
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load GitHub commits."
            );
        }

        return Array.isArray(data)
            ? data
            : [];
    })

    .then(function (commits) {

        renderGitHubCommits(commits);
    })

    .catch(function (error) {

        console.error(
            "GitHub Commits Error:",
            error
        );

        showGitHubMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// RENDER GITHUB COMMITS
// =====================================================

function renderGitHubCommits(commits) {

    var section =
        document.getElementById(
            "githubCommitsSection"
        );

    var container =
        document.getElementById(
            "githubCommits"
        );

    if (!section || !container) {
        return;
    }

    section.style.display = "block";

    container.innerHTML = "";

    if (
        !Array.isArray(commits) ||
        commits.length === 0
    ) {

        container.innerHTML =
            "<p>No commits found.</p>";

        return;
    }

    commits.forEach(function (commit) {

        var sha =
            commit.sha
                ? String(commit.sha).substring(0, 7)
                : "-";

        var message =
            commit.commit &&
            commit.commit.message
                ? commit.commit.message
                : "No commit message";

        var author =
            commit.author &&
            commit.author.login
                ? commit.author.login
                : (
                    commit.commit &&
                    commit.commit.author &&
                    commit.commit.author.name
                        ? commit.commit.author.name
                        : "Unknown"
                );

        var url =
            commit.html_url || "#";

        var item =
            document.createElement("div");

        item.className =
            "github-item";

        item.innerHTML =

            "<strong>" +
                escapeGitHubHtml(message) +
            "</strong>" +

            "<p>" +
                "Commit: " +
                escapeGitHubHtml(sha) +
                " | Author: " +
                escapeGitHubHtml(author) +
            "</p>" +

            (
                url !== "#"
                    ?

                "<a href=\"" +
                    escapeAttribute(url) +
                    "\" target=\"_blank\" rel=\"noopener noreferrer\">" +
                    "View Commit" +
                "</a>"

                    :

                ""
            );

        container.appendChild(item);
    });
}


// =====================================================
// LOAD GITHUB PULL REQUESTS
// =====================================================

function loadGitHubPullRequests() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {
        return;
    }

    fetch(
        contextPath +
        "/api/github/repositories/project/" +
        encodeURIComponent(projectId) +
        "/pulls?state=all",
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load GitHub pull requests."
            );
        }

        return Array.isArray(data)
            ? data
            : [];
    })

    .then(function (pullRequests) {

        renderGitHubPullRequests(
            pullRequests
        );
    })

    .catch(function (error) {

        console.error(
            "GitHub PR Error:",
            error
        );

        showGitHubMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// RENDER GITHUB RAW PULL REQUESTS
// =====================================================

function renderGitHubPullRequests(
    pullRequests
) {

    var section =
        document.getElementById(
            "githubRawPullsSection"
        );

    var container =
        document.getElementById(
            "githubRawPullRequests"
        );

    if (!container) {

        console.warn(
            "githubRawPullRequests container not found."
        );

        return;
    }

    if (section) {
        section.style.display = "block";
    }

    container.innerHTML = "";

    if (
        !Array.isArray(pullRequests) ||
        pullRequests.length === 0
    ) {

        container.innerHTML =
            "<p>No GitHub pull requests found.</p>";

        return;
    }

    pullRequests.forEach(function (pr) {

        var number =
            pr.number !== null &&
            pr.number !== undefined
                ? pr.number
                : "-";

        var title =
            pr.title ||
            "Untitled Pull Request";

        var state =
            pr.state ||
            "unknown";

        var url =
            pr.html_url ||
            "#";

        var item =
            document.createElement("div");

        item.className =
            "github-item";

        item.innerHTML =

            "<strong>" +
                "#" +
                escapeGitHubHtml(number) +
                " " +
                escapeGitHubHtml(title) +
            "</strong>" +

            "<p>" +
                "State: " +
                escapeGitHubHtml(state) +
            "</p>" +

            (
                url !== "#"
                    ?

                "<a href=\"" +
                    escapeAttribute(url) +
                    "\" target=\"_blank\" rel=\"noopener noreferrer\">" +
                    "Open Pull Request" +
                "</a>"

                    :

                ""
            );

        container.appendChild(item);
    });
}


// =====================================================
// DISCONNECT GITHUB
// =====================================================

function disconnectGitHub() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {

        showGitHubMessage(
            "Project ID or JWT token missing.",
            "error"
        );

        return;
    }

    if (
        !confirm(
            "Disconnect GitHub repository?"
        )
    ) {
        return;
    }

    fetch(
        contextPath +
        "/api/github/repositories/project/" +
        encodeURIComponent(projectId),
        {
            method: "DELETE",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to disconnect GitHub repository."
            );
        }

        return data;
    })

    .then(function (data) {

        showGitHubMessage(
            data.message ||
            "GitHub repository disconnected.",
            "success"
        );

        showGitHubConnectSection();
    })

    .catch(function (error) {

        console.error(
            "GitHub Disconnect Error:",
            error
        );

        showGitHubMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// SYNC DEV COLLAB PULL REQUESTS
// =====================================================

function syncDevCollabPullRequests() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {

        showPullRequestMessage(
            "Project ID or JWT token missing.",
            "error"
        );

        return;
    }

    fetch(
        contextPath +
        "/api/pull-requests/sync/project/" +
        encodeURIComponent(projectId),
        {
            method: "POST",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to sync pull requests."
            );
        }

        return data;
    })

    .then(function () {

        showPullRequestMessage(
            "Pull Requests synced successfully.",
            "success"
        );

        loadDevCollabPullRequests();
    })

    .catch(function (error) {

        console.error(
            "PR Sync Error:",
            error
        );

        showPullRequestMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// LOAD DEV COLLAB PULL REQUESTS
// =====================================================

function loadDevCollabPullRequests() {

    var projectId = getProjectId();
    var token = getToken();

    if (!projectId || !token) {
        return;
    }

    fetch(
        contextPath +
        "/api/pull-requests/project/" +
        encodeURIComponent(projectId),
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load DevCollab Pull Requests."
            );
        }

        return Array.isArray(data)
            ? data
            : [];
    })

    .then(function (data) {

        renderDevCollabPullRequests(data);

        autoFillLatestPullRequest(data);
    })

    .catch(function (error) {

        console.error(
            "DevCollab PR Load Error:",
            error
        );

        showPullRequestMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// RENDER DEV COLLAB PULL REQUESTS
// =====================================================

function renderDevCollabPullRequests(
    pullRequests
) {

    var container =
        document.getElementById(
            "githubPullRequests"
        );

    var section =
        document.getElementById(
            "githubPullsSection"
        );

    if (!container) {

        console.warn(
            "githubPullRequests container not found."
        );

        return;
    }

    container.innerHTML = "";

    if (
        !Array.isArray(pullRequests) ||
        pullRequests.length === 0
    ) {

        container.innerHTML =
            "<p>No Pull Requests synced yet.</p>";

        if (section) {
            section.style.display = "block";
        }

        return;
    }

    if (section) {
        section.style.display = "block";
    }

    pullRequests.forEach(function (pr) {

        var card =
            document.createElement("div");

        card.className =
            "github-item devcollab-pr-card";

        var statusOptions =
            createPRStatusOptions(
                pr.status
            );

        var pullRequestId =
            Number(pr.pullRequestId);

        var taskId =
            pr.taskId !== null &&
            pr.taskId !== undefined
                ? Number(pr.taskId)
                : null;

        var taskIdForButton =
            taskId !== null &&
            !isNaN(taskId)
                ? taskId
                : "null";

        var prNumber =
            pr.githubPrNumber !== null &&
            pr.githubPrNumber !== undefined
                ? pr.githubPrNumber
                : "-";

        card.innerHTML =

            "<div>" +

                "<strong>" +

                    "#" +

                    escapeGitHubHtml(
                        prNumber
                    ) +

                    " " +

                    escapeGitHubHtml(
                        pr.title ||
                        "Untitled Pull Request"
                    ) +

                "</strong>" +

            "</div>" +

            "<p>" +

                "Source: " +

                escapeGitHubHtml(
                    pr.sourceBranch || "-"
                ) +

                " → Target: " +

                escapeGitHubHtml(
                    pr.targetBranch || "-"
                ) +

            "</p>" +

            "<p>" +

                "GitHub State: " +

                escapeGitHubHtml(
                    pr.githubState || "-"
                ) +

            "</p>" +

            "<p>" +

                "DevCollab Status: " +

                escapeGitHubHtml(
                    pr.status || "-"
                ) +

            "</p>" +

            "<p>" +

                "Task ID: " +

                (
                    taskId !== null &&
                    !isNaN(taskId)

                        ?

                    escapeGitHubHtml(taskId)

                        :

                    "Not linked"
                ) +

            "</p>" +

            "<div class=\"github-actions\">" +

                (
                    !isNaN(pullRequestId) &&
                    pullRequestId > 0

                        ?

                    "<select onchange=\"" +

                        "updateDevCollabPRStatus(" +

                            pullRequestId +

                            ", this.value)" +

                        "\">" +

                            statusOptions +

                    "</select>"

                        :

                    ""
                ) +

                (
                    !isNaN(pullRequestId) &&
                    pullRequestId > 0

                        ?

                    "<button " +

                        "type=\"button\" " +

                        "onclick=\"" +

                        "selectPullRequestForReview(" +

                            pullRequestId +

                            "," +

                            taskIdForButton +

                        ")" +

                        "\">" +

                        "📝 Review" +

                    "</button>"

                        :

                    ""
                ) +

                (
                    !isNaN(pullRequestId) &&
                    pullRequestId > 0

                        ?

                    "<button " +

                        "type=\"button\" " +

                        "class=\"ai-pr-button\" " +

                        "onclick=\"" +

                        "runAutomaticAiReview(" +

                            pullRequestId +

                        ")" +

                        "\">" +

                        "🤖 AI Review" +

                    "</button>"

                        :

                    ""
                ) +

                (
                    pr.prUrl

                        ?

                    "<a href=\"" +

                        escapeAttribute(
                            pr.prUrl
                        ) +

                        "\" target=\"_blank\" rel=\"noopener noreferrer\">" +

                        "Open GitHub PR" +

                    "</a>"

                        :

                    ""
                ) +

            "</div>";

        container.appendChild(card);
    });
}


// =====================================================
// PR STATUS OPTIONS
// =====================================================

function createPRStatusOptions(
    currentStatus
) {

    var statuses = [

        "OPEN",
        "AI_REVIEW",
        "HUMAN_REVIEW",
        "CHANGES_REQUESTED",
        "APPROVED",
        "MERGED",
        "CLOSED"

    ];

    var html = "";

    statuses.forEach(function (status) {

        html +=

            "<option value=\"" +

            escapeAttribute(status) +

            "\" " +

            (
                String(
                    currentStatus || ""
                ).toUpperCase() === status

                    ? "selected"

                    : ""
            ) +

            ">" +

            escapeGitHubHtml(
                status.replace(
                    /_/g,
                    " "
                )
            ) +

            "</option>";
    });

    return html;
}


// =====================================================
// SELECT PR FOR HUMAN REVIEW
// =====================================================

function selectPullRequestForReview(
    pullRequestId,
    taskId
) {

    var input =
        document.getElementById(
            "reviewPullRequestId"
        );

    if (input) {
        input.value = pullRequestId;
    }

    var aiInput =
        document.getElementById(
            "aiPullRequestId"
        );

    if (aiInput) {
        aiInput.value = pullRequestId;
    }

    var aiTaskInput =
        document.getElementById(
            "aiTaskId"
        );

    if (
        aiTaskInput &&
        taskId !== null &&
        taskId !== undefined &&
        taskId !== "null"
    ) {

        aiTaskInput.value = taskId;
    }

    loadHumanReviews(pullRequestId);

    loadLatestAiReview(pullRequestId);

    scrollToSection("humanReviewForm");
}


// =====================================================
// SELECT PR FOR AI REVIEW
// =====================================================

function selectPullRequestForAIReview(
    pullRequestId,
    taskId
) {

    var prInput =
        document.getElementById(
            "aiPullRequestId"
        );

    var taskInput =
        document.getElementById(
            "aiTaskId"
        );

    if (prInput) {
        prInput.value = pullRequestId;
    }

    if (
        taskInput &&
        taskId !== null &&
        taskId !== undefined &&
        taskId !== "null"
    ) {

        taskInput.value = taskId;
    }

    loadLatestAiReview(pullRequestId);

    scrollToSection("aiReviewForm");
}


// =====================================================
// AUTOFILL LATEST PR
// =====================================================

function autoFillLatestPullRequest(
    pullRequests
) {

    if (
        !Array.isArray(pullRequests) ||
        pullRequests.length === 0
    ) {
        return;
    }

    var prInput =
        document.getElementById(
            "aiPullRequestId"
        );

    if (
        prInput &&
        !prInput.value
    ) {

        var latest =
            pullRequests[0];

        if (
            latest &&
            latest.pullRequestId
        ) {

            prInput.value =
                latest.pullRequestId;

            var taskInput =
                document.getElementById(
                    "aiTaskId"
                );

            if (
                taskInput &&
                latest.taskId !== null &&
                latest.taskId !== undefined
            ) {

                taskInput.value =
                    latest.taskId;
            }
        }
    }
}


// =====================================================
// UPDATE DEV COLLAB PR STATUS
// =====================================================

function updateDevCollabPRStatus(
    pullRequestId,
    status
) {

    var token = getToken();

    if (!token) {

        showPullRequestMessage(
            "JWT token not found.",
            "error"
        );

        return;
    }

    if (
        !pullRequestId ||
        Number(pullRequestId) <= 0
    ) {

        showPullRequestMessage(
            "Pull Request ID is required.",
            "error"
        );

        return;
    }

    if (!status) {

        showPullRequestMessage(
            "PR status is required.",
            "error"
        );

        return;
    }

    fetch(
        contextPath +
        "/api/pull-requests/" +
        encodeURIComponent(pullRequestId) +
        "/status?status=" +
        encodeURIComponent(status),
        {
            method: "PUT",

            headers: {
                "Authorization": "Bearer " + token,
                "Accept": "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to update PR status."
            );
        }

        return data;
    })

    .then(function (data) {

        console.log(
            "PR status updated:",
            data
        );

        showPullRequestMessage(
            "Pull Request status updated.",
            "success"
        );

        loadDevCollabPullRequests();
    })

    .catch(function (error) {

        console.error(
            "PR status update error:",
            error
        );

        showPullRequestMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// MANUAL AI CODE REVIEW
// =====================================================

async function runAiCodeReview() {

    var token = getToken();

    if (!token) {

        showAiReviewMessage(
            "Authentication required. Please login again.",
            "error"
        );

        return;
    }

    var prElement =
        document.getElementById(
            "aiPullRequestId"
        );

    var taskElement =
        document.getElementById(
            "aiTaskId"
        );

    var codeElement =
        document.getElementById(
            "aiCodeInput"
        );

    if (!prElement || !codeElement) {

        showAiReviewMessage(
            "AI review form elements are missing.",
            "error"
        );

        return;
    }

    var pullRequestId =
        prElement.value.trim();

    var taskId =
        taskElement
            ? taskElement.value.trim()
            : "";

    var code =
        codeElement.value.trim();

    if (!pullRequestId) {

        showAiReviewMessage(
            "Pull Request ID is required.",
            "error"
        );

        return;
    }

    if (
        isNaN(Number(pullRequestId)) ||
        Number(pullRequestId) <= 0
    ) {

        showAiReviewMessage(
            "Invalid Pull Request ID.",
            "error"
        );

        return;
    }

    if (!code) {

        showAiReviewMessage(
            "Please enter source code.",
            "error"
        );

        return;
    }

    var button =
        document.getElementById(
            "aiAnalyzeButton"
        );

    try {

        if (button) {

            button.disabled = true;
            button.innerText =
                "🤖 Analyzing...";
        }

        hideAiReviewMessage();

        var response =
            await fetch(
                contextPath +
                "/api/ai-reviews/analyze",
                {
                    method: "POST",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Content-Type":
                            "application/json",

                        "Accept":
                            "application/json"
                    },

                    body: JSON.stringify({

                        pullRequestId:
                            Number(pullRequestId),

                        taskId:
                            taskId &&
                            !isNaN(Number(taskId))
                                ? Number(taskId)
                                : null,

                        code:
                            code
                    })
                }
            );

        var data =
            await parseApiResponse(response);

        console.log(
            "AI Manual Review HTTP Status:",
            response.status
        );

        console.log(
            "AI Manual Review Response:",
            data
        );

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "AI code review failed."
            );
        }

        displayAiReviewResult(data);

        showAiReviewMessage(
            "AI code review completed successfully.",
            "success"
        );

    } catch (error) {

        console.error(
            "AI Manual Review Error:",
            error
        );

        showAiReviewMessage(
            error.message ||
            "AI code review failed.",
            "error"
        );

    } finally {

        if (button) {

            button.disabled = false;

            button.innerText =
                "🤖 Analyze Code";
        }
    }
}


// =====================================================
// AUTOMATIC GITHUB PR AI REVIEW
// =====================================================

async function runAutomaticAiReview(
    pullRequestId
) {

    var token = getToken();

    if (!token) {

        showPullRequestMessage(
            "Authentication required. Please login again.",
            "error"
        );

        return;
    }

    if (
        !pullRequestId ||
        Number(pullRequestId) <= 0
    ) {

        showPullRequestMessage(
            "Invalid Pull Request ID.",
            "error"
        );

        return;
    }

    var confirmed =
        confirm(
            "Run automatic AI review for this Pull Request?"
        );

    if (!confirmed) {
        return;
    }

    showAiReviewMessage(
        "🤖 Fetching GitHub changes and running AI review...",
        "info"
    );

    try {

        var response =
            await fetch(
                contextPath +
                "/api/ai-reviews/pull-request/" +
                encodeURIComponent(
                    pullRequestId
                ),
                {
                    method: "POST",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Accept":
                            "application/json"
                    }
                }
            );

        var data =
            await parseApiResponse(response);

        console.log(
            "Automatic AI Review HTTP Status:",
            response.status
        );

        console.log(
            "Automatic AI Review Response:",
            data
        );

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Automatic AI review failed."
            );
        }

        displayAiReviewResult(data);

        var prInput =
            document.getElementById(
                "aiPullRequestId"
            );

        if (prInput) {

            prInput.value =
                data.pullRequestId ||
                pullRequestId;
        }

        var taskInput =
            document.getElementById(
                "aiTaskId"
            );

        if (
            taskInput &&
            data.taskId !== null &&
            data.taskId !== undefined
        ) {

            taskInput.value =
                data.taskId;
        }

        showAiReviewMessage(
            "✅ GitHub Pull Request AI review completed successfully.",
            "success"
        );

        scrollToSection("aiReviewForm");

    } catch (error) {

        console.error(
            "Automatic AI Review Error:",
            error
        );

        showAiReviewMessage(
            error.message ||
            "Automatic GitHub AI review failed.",
            "error"
        );
    }
}


// =====================================================
// LOAD LATEST AI REVIEW
// =====================================================

function loadLatestAiReview(
    pullRequestId
) {

    var token = getToken();

    if (
        !pullRequestId ||
        !token
    ) {
        return;
    }

    fetch(
        contextPath +
        "/api/ai-reviews/pull-request/" +
        encodeURIComponent(pullRequestId),
        {
            method: "GET",

            headers: {
                "Authorization":
                    "Bearer " + token,

                "Accept":
                    "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load AI review."
            );
        }

        return data;
    })

    .then(function (reviews) {

        if (
            !Array.isArray(reviews) ||
            reviews.length === 0
        ) {
            return;
        }

        displayAiReviewResult(
            reviews[0]
        );
    })

    .catch(function (error) {

        console.error(
            "AI Review Load Error:",
            error
        );
    });
}


// =====================================================
// LOAD LATEST REVIEW FROM FORM
// =====================================================

function loadLatestAiReviewFromForm() {

    var element =
        document.getElementById(
            "aiPullRequestId"
        );

    if (!element) {

        showAiReviewMessage(
            "AI Pull Request field not found.",
            "error"
        );

        return;
    }

    var pullRequestId =
        element.value.trim();

    if (!pullRequestId) {

        showAiReviewMessage(
            "Pull Request ID is required.",
            "error"
        );

        return;
    }

    loadLatestAiReview(
        pullRequestId
    );
}


// =====================================================
// DISPLAY AI REVIEW RESULT
// =====================================================

function displayAiReviewResult(data) {

    var result =
        document.getElementById(
            "aiReviewResult"
        );

    if (!result) {

        console.error(
            "aiReviewResult element not found."
        );

        return;
    }

    result.style.display = "block";

    var score =
        data.score !== null &&
        data.score !== undefined
            ? Number(data.score)
            : 0;

    if (isNaN(score)) {
        score = 0;
    }

    setText(
        "aiScore",
        score + "/100"
    );

    setText(
        "aiBugCount",
        data.bugCount || 0
    );

    setText(
        "aiSecurityCount",
        data.securityCount || 0
    );

    setText(
        "aiQualityCount",
        data.qualityCount || 0
    );

    setText(
        "aiReviewStatus",
        data.reviewStatus ||
        "COMPLETED"
    );

    var findings =
        document.getElementById(
            "aiFindings"
        );

    if (findings) {

        findings.innerText =
            data.findings ||
            "✅ No major issues detected.";
    }

    var suggestions =
        document.getElementById(
            "aiSuggestions"
        );

    if (suggestions) {

        suggestions.innerText =
            data.suggestions ||
            "No suggestions available.";
    }

    var progress =
        document.getElementById(
            "aiScoreProgress"
        );

    if (progress) {

        var safeScore =
            Math.max(
                0,
                Math.min(
                    100,
                    score
                )
            );

        progress.style.width =
            safeScore + "%";

        progress.className =
            "ai-score-progress " +
            getScoreClass(score);
    }

    var prInput =
        document.getElementById(
            "aiPullRequestId"
        );

    if (
        prInput &&
        data.pullRequestId !== null &&
        data.pullRequestId !== undefined
    ) {

        prInput.value =
            data.pullRequestId;
    }

    var taskInput =
        document.getElementById(
            "aiTaskId"
        );

    if (
        taskInput &&
        data.taskId !== null &&
        data.taskId !== undefined
    ) {

        taskInput.value =
            data.taskId;
    }

    result.scrollIntoView({

        behavior: "smooth",

        block: "start"
    });
}


// =====================================================
// SCORE CLASS
// =====================================================

function getScoreClass(score) {

    score =
        Number(score || 0);

    if (score >= 80) {
        return "score-good";
    }

    if (score >= 60) {
        return "score-medium";
    }

    return "score-low";
}


// =====================================================
// RESET AI REVIEW
// =====================================================

function resetAiReview() {

    var codeInput =
        document.getElementById(
            "aiCodeInput"
        );

    if (codeInput) {
        codeInput.value = "";
    }

    var result =
        document.getElementById(
            "aiReviewResult"
        );

    if (result) {
        result.style.display = "none";
    }

    setText(
        "aiScore",
        "-/100"
    );

    setText(
        "aiBugCount",
        "0"
    );

    setText(
        "aiSecurityCount",
        "0"
    );

    setText(
        "aiQualityCount",
        "0"
    );

    setText(
        "aiReviewStatus",
        "-"
    );

    var findings =
        document.getElementById(
            "aiFindings"
        );

    if (findings) {
        findings.innerText = "";
    }

    var suggestions =
        document.getElementById(
            "aiSuggestions"
        );

    if (suggestions) {
        suggestions.innerText = "";
    }

    var progress =
        document.getElementById(
            "aiScoreProgress"
        );

    if (progress) {

        progress.style.width =
            "0%";

        progress.className =
            "ai-score-progress";
    }

    hideAiReviewMessage();
}


// =====================================================
// HUMAN CODE REVIEW
// =====================================================

function submitHumanReview() {

    var token = getToken();

    var pullRequestElement =
        document.getElementById(
            "reviewPullRequestId"
        );

    var decisionElement =
        document.getElementById(
            "reviewDecision"
        );

    var commentElement =
        document.getElementById(
            "reviewComment"
        );

    if (
        !pullRequestElement ||
        !decisionElement ||
        !commentElement
    ) {

        showHumanReviewMessage(
            "Human review form elements are missing.",
            "error"
        );

        return;
    }

    var pullRequestId =
        pullRequestElement.value.trim();

    var decision =
        decisionElement.value;

    var reviewComment =
        commentElement.value.trim();

    if (!token) {

        showHumanReviewMessage(
            "JWT token not found.",
            "error"
        );

        return;
    }

    if (!pullRequestId) {

        showHumanReviewMessage(
            "Pull Request ID is required.",
            "error"
        );

        return;
    }

    if (
        isNaN(Number(pullRequestId)) ||
        Number(pullRequestId) <= 0
    ) {

        showHumanReviewMessage(
            "Invalid Pull Request ID.",
            "error"
        );

        return;
    }

    if (!reviewComment) {

        showHumanReviewMessage(
            "Review comment is required.",
            "error"
        );

        return;
    }

    fetch(
        contextPath +
        "/api/code-reviews/pull-request/" +
        encodeURIComponent(pullRequestId),
        {
            method: "POST",

            headers: {
                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json",

                "Accept":
                    "application/json"
            },

            body: JSON.stringify({

                reviewComment:
                    reviewComment,

                decision:
                    decision
            })
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Human review failed."
            );
        }

        return data;
    })

    .then(function (review) {

        console.log(
            "Human Review:",
            review
        );

        showHumanReviewMessage(
            "Code review submitted successfully.",
            "success"
        );

        commentElement.value = "";

        loadHumanReviews(
            pullRequestId
        );
    })

    .catch(function (error) {

        console.error(
            "Human Review Error:",
            error
        );

        showHumanReviewMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// LOAD HUMAN REVIEWS
// =====================================================

function loadHumanReviews(
    pullRequestId
) {

    var token = getToken();

    if (!pullRequestId || !token) {
        return;
    }

    fetch(
        contextPath +
        "/api/code-reviews/pull-request/" +
        encodeURIComponent(pullRequestId),
        {
            method: "GET",

            headers: {
                "Authorization":
                    "Bearer " + token,

                "Accept":
                    "application/json"
            }
        }
    )

    .then(async function (response) {

        var data =
            await parseApiResponse(response);

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Failed to load code reviews."
            );
        }

        return Array.isArray(data)
            ? data
            : [];
    })

    .then(function (reviews) {

        renderHumanReviews(
            reviews
        );
    })

    .catch(function (error) {

        console.error(
            "Load Human Reviews Error:",
            error
        );
    });
}


// =====================================================
// RENDER HUMAN REVIEWS
// =====================================================

function renderHumanReviews(
    reviews
) {

    var container =
        document.getElementById(
            "humanReviewsContainer"
        );

    if (!container) {
        return;
    }

    container.innerHTML = "";

    if (
        !Array.isArray(reviews) ||
        reviews.length === 0
    ) {

        container.innerHTML =
            "<p>No human reviews yet.</p>";

        return;
    }

    reviews.forEach(function (review) {

        var card =
            document.createElement("div");

        card.className =
            "human-review-item";

        card.innerHTML =

            "<div>" +

                "<strong>" +

                    "Reviewer #" +

                    escapeGitHubHtml(
                        review.reviewerId ||
                        "-"
                    ) +

                "</strong>" +

                "<span class=\"review-decision\">" +

                    escapeGitHubHtml(
                        review.decision ||
                        "-"
                    ) +

                "</span>" +

            "</div>" +

            "<p>" +

                escapeGitHubHtml(
                    review.reviewComment ||
                    ""
                ) +

            "</p>" +

            "<small>" +

                formatReviewDate(
                    review.createdAt
                ) +

            "</small>";

        container.appendChild(card);
    });
}


// =====================================================
// AI MESSAGE
// =====================================================

function showAiReviewMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "aiReviewMessage"
        );

    if (!element) {

        console.log(message);

        return;
    }

    element.innerText =
        message;

    element.className =
        "github-message " +
        (type || "info");

    element.style.display =
        "block";
}


// =====================================================
// HIDE AI MESSAGE
// =====================================================

function hideAiReviewMessage() {

    var element =
        document.getElementById(
            "aiReviewMessage"
        );

    if (!element) {
        return;
    }

    element.innerText = "";

    element.style.display =
        "none";
}


// =====================================================
// HUMAN REVIEW MESSAGE
// =====================================================

function showHumanReviewMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "humanReviewMessage"
        );

    if (!element) {

        console.log(message);

        return;
    }

    element.innerText =
        message;

    element.className =
        "github-message " +
        (type || "info");

    element.style.display =
        "block";
}


// =====================================================
// PULL REQUEST MESSAGE
// =====================================================

function showPullRequestMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "githubMessage"
        );

    if (!element) {

        console.log(message);

        return;
    }

    element.innerText =
        message;

    element.className =
        "github-message " +
        (type || "info");

    element.style.display =
        "block";
}


// =====================================================
// GITHUB MESSAGE
// =====================================================

function showGitHubMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "githubMessage"
        );

    if (!element) {

        console.log(message);

        return;
    }

    element.innerText =
        message;

    element.className =
        "github-message " +
        (type || "info");

    element.style.display =
        "block";
}


// =====================================================
// SCROLL TO SECTION
// =====================================================

function scrollToSection(id) {

    var element =
        document.getElementById(id);

    if (!element) {
        return;
    }

    element.scrollIntoView({

        behavior: "smooth",

        block: "start"
    });
}


// =====================================================
// HTML ESCAPE
// =====================================================

function escapeGitHubHtml(value) {

    return String(

        value === null ||
        value === undefined
            ? ""
            : value

    )
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}


// =====================================================
// ATTRIBUTE ESCAPE
// =====================================================

function escapeAttribute(value) {

    return String(

        value === null ||
        value === undefined
            ? ""
            : value

    )
    .replace(/&/g, "&amp;")
    .replace(/"/g, "&quot;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}


// =====================================================
// HUMAN REVIEW DATE
// =====================================================

function formatReviewDate(value) {

    if (!value) {
        return "-";
    }

    try {

        var date =
            new Date(value);

        if (isNaN(date.getTime())) {
            return String(value);
        }

        return date.toLocaleString();

    } catch (error) {

        return String(value);
    }
}


// =====================================================
// SHOW GENERAL MESSAGE
// =====================================================

function showMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "message"
        );

    if (!element) {

        console.log(message);

        return;
    }

    element.innerText =
        message;

    element.className =
        "message " +
        (type || "error");

    element.style.display =
        "block";
}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    console.log(
        "Logging out..."
    );

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("fullName");
    localStorage.removeItem("email");
    localStorage.removeItem("role");

    window.location.href =
        contextPath + "/login.jsp";
}
