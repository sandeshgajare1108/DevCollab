// =====================================================
// DEV COLLAB PROJECT JS
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("Project JS Loaded");

    loadUserData();
    loadProjects();

});


// =====================================================
// LOAD USER DATA
// =====================================================

function loadUserData() {

    var fullName = localStorage.getItem("fullName");
    var role = localStorage.getItem("role");

    console.log("User Name:", fullName);
    console.log("User Role:", role);

    if (fullName) {

        var userName = document.getElementById("userName");
        var avatar = document.getElementById("userAvatar");

        if (userName) {
            userName.innerText = fullName;
        }

        if (avatar) {
            avatar.innerText =
                fullName.charAt(0).toUpperCase();
        }
    }

    if (role) {

        var userRole = document.getElementById("userRole");

        if (userRole) {
            userRole.innerText = role;
        }
    }
}


// =====================================================
// LOAD PROJECTS
// =====================================================

function loadProjects() {

    console.log("Loading projects...");

    var token = localStorage.getItem("token");

    console.log(
        "Token available:",
        token ? "YES" : "NO"
    );

    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }

    var loading = document.getElementById("loading");

    if (loading) {
        loading.style.display = "block";
    }

    fetch(
        contextPath + "/api/projects",
        {
            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        }
    )
    .then(function (response) {

        console.log(
            "Projects HTTP Status:",
            response.status
        );

        if (!response.ok) {

            if (response.status === 401) {
                throw new Error(
                    "401 Unauthorized - Please login again."
                );
            }

            if (response.status === 403) {
                throw new Error(
                    "403 Forbidden - You don't have permission."
                );
            }

            throw new Error(
                "Projects API Error: " +
                response.status
            );
        }

        return response.json();
    })
    .then(function (data) {

        console.log(
            "Projects API Response:",
            data
        );

        displayProjects(data);
    })
    .catch(function (error) {

        console.error(
            "Projects Error:",
            error
        );

        showMessage(
            error.message,
            "error"
        );
    })
    .finally(function () {

        if (loading) {
            loading.style.display = "none";
        }

    });
}


// =====================================================
// DISPLAY PROJECTS
// =====================================================

function displayProjects(projects) {

    var grid =
        document.getElementById("projectsGrid");

    var noProjects =
        document.getElementById("noProjects");

    if (!grid) {
        console.error(
            "projectsGrid element not found."
        );
        return;
    }

    if (!noProjects) {
        console.error(
            "noProjects element not found."
        );
        return;
    }

    grid.innerHTML = "";

    if (!projects ||
        !Array.isArray(projects) ||
        projects.length === 0) {

        noProjects.style.display = "block";

        return;
    }

    noProjects.style.display = "none";

    projects.forEach(function (project) {

        var card =
            document.createElement("div");

        card.className =
            "project-card";

        var status =
            project.status || "PLANNING";

        var statusText =
            status
                .replace(/_/g, " ")
                .toLowerCase()
                .replace(/\b\w/g, function (letter) {
                    return letter.toUpperCase();
                });

        var projectName =
            escapeHtml(
                project.projectName ||
                "Unnamed Project"
            );

        var description =
            escapeHtml(
                project.description ||
                "No description available"
            );

        var projectId =
            project.projectId || "-";

        card.innerHTML = `

            <div class="project-card-top">

                <div class="project-icon">
                    📁
                </div>

                <span class="status ${getStatusClass(status)}">
                    ${statusText}
                </span>

            </div>

            <h3>
                ${projectName}
            </h3>

            <p class="description">
                ${description}
            </p>

            <div class="project-footer">

                <span>
                    Project ID: ${projectId}
                </span>

                <button
                    type="button"
                    onclick="viewProject(${projectId})">

                    View

                </button>

            </div>
        `;

        grid.appendChild(card);

    });
}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

    if (status === "COMPLETED") {
        return "completed";
    }

    if (status === "IN_PROGRESS") {
        return "in-progress";
    }

    return "planning";
}


// =====================================================
// SHOW CREATE FORM
// =====================================================

function showCreateForm() {

    var formSection =
        document.getElementById("projectFormSection");

    if (!formSection) {
        console.error(
            "projectFormSection not found."
        );
        return;
    }

    formSection.style.display = "block";

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}


// =====================================================
// HIDE CREATE FORM
// =====================================================

function hideCreateForm() {

    var formSection =
        document.getElementById("projectFormSection");

    var form =
        document.getElementById("projectForm");

    if (formSection) {
        formSection.style.display = "none";
    }

    if (form) {
        form.reset();
    }
}


// =====================================================
// CREATE PROJECT
// =====================================================

function createProject(event) {

    event.preventDefault();

    console.log("Creating project...");

    // =================================================
    // JWT TOKEN
    // =================================================

    var token =
        localStorage.getItem("token");

    // =================================================
    // USER ID
    // =================================================

    var userId =
        localStorage.getItem("userId");

    console.log(
        "Token available:",
        token ? "YES" : "NO"
    );

    console.log(
        "Logged-in User ID:",
        userId
    );


    // =================================================
    // TOKEN CHECK
    // =================================================

    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    // =================================================
    // USER ID CHECK
    // =================================================

    if (!userId || isNaN(Number(userId))) {

        console.error(
            "Invalid userId:",
            userId
        );

        showMessage(
            "Valid User ID not found. Please login again.",
            "error"
        );

        return;
    }


    // =================================================
    // FORM ELEMENTS
    // =================================================

    var projectNameElement =
        document.getElementById("projectName");

    var descriptionElement =
        document.getElementById("projectDescription");

    var statusElement =
        document.getElementById("projectStatus");


    if (!projectNameElement ||
        !descriptionElement ||
        !statusElement) {

        console.error(
            "Project form elements not found."
        );

        return;
    }


    // =================================================
    // FORM VALUES
    // =================================================

    var projectName =
        projectNameElement.value.trim();

    var description =
        descriptionElement.value.trim();

    var status =
        statusElement.value;


    // =================================================
    // VALIDATION
    // =================================================

    if (!projectName) {

        showMessage(
            "Project name is required.",
            "error"
        );

        return;
    }


    // =================================================
    // PROJECT DATA
    // =================================================

    var projectData = {

        projectName: projectName,

        description: description,

        status: status,

        ownerId: Number(userId)

    };


    // =================================================
    // DEBUG
    // =================================================

    console.log(
        "Project Request JSON:",
        JSON.stringify(projectData)
    );


    // =================================================
    // CREATE PROJECT API
    // =================================================

    fetch(
        contextPath + "/api/projects",
        {
            method: "POST",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            },

            body:
                JSON.stringify(projectData)
        }
    )
    .then(function (response) {

        console.log(
            "Create Project Status:",
            response.status
        );

        return response.text()
            .then(function (text) {

                if (!response.ok) {

                    console.error(
                        "Create Project Error Response:",
                        text
                    );

                    throw new Error(
                        "Create Project Failed (" +
                        response.status +
                        "): " +
                        text
                    );
                }

                return text
                    ? JSON.parse(text)
                    : {};
            });
    })
    .then(function (data) {

        console.log(
            "Project Created:",
            data
        );

        showMessage(
            "Project created successfully!",
            "success"
        );

        hideCreateForm();

        loadProjects();
    })
    .catch(function (error) {

        console.error(
            "Create Project Error:",
            error
        );

        showMessage(
            error.message,
            "error"
        );
    });
}

//=====================================================
//VIEW PROJECT
//=====================================================

function viewProject(projectId) {

 if (!projectId) {

     showMessage(
         "Project ID not available.",
         "error"
     );

     return;
 }

 console.log("Opening Project Details:", projectId);

 window.location.href =
     contextPath +
     "/project-details.jsp?projectId=" +
     encodeURIComponent(projectId);
}

// =====================================================
// SHOW MESSAGE
// =====================================================

function showMessage(message, type) {

    var element =
        document.getElementById("message");

    if (!element) {
        console.error(
            "Message element not found."
        );
        return;
    }

    element.innerText =
        message;

    element.className =
        "message " + type;

    element.style.display =
        "block";

    setTimeout(function () {

        element.style.display =
            "none";

    }, 4000);
}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    console.log("Logging out...");

    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("fullName");
    localStorage.removeItem("email");
    localStorage.removeItem("role");

    window.location.href =
        contextPath + "/login.jsp";
}


// =====================================================
// HTML SECURITY
// =====================================================

function escapeHtml(value) {

    if (value === null ||
        value === undefined) {

        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}