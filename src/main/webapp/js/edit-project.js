// =====================================================
// DEV COLLAB - EDIT PROJECT
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("Edit Project JS Loaded");
    console.log("=================================");

    loadUserData();

    loadProject();

});


// =====================================================
// LOAD USER DATA
// =====================================================

function loadUserData() {

    var fullName =
        localStorage.getItem("fullName");

    var role =
        localStorage.getItem("role");

    if (fullName) {

        document.getElementById("userName").innerText =
            fullName;

        document.getElementById("userAvatar").innerText =
            fullName.charAt(0).toUpperCase();

    }

    if (role) {

        document.getElementById("userRole").innerText =
            role;

    }

}


// =====================================================
// GET PROJECT ID
// =====================================================

function getProjectId() {

    var params =
        new URLSearchParams(
            window.location.search
        );

    return params.get("projectId");

}


// =====================================================
// LOAD PROJECT
// =====================================================

function loadProject() {

    var token =
        localStorage.getItem("token");

    var projectId =
        getProjectId();


    console.log("Project ID:", projectId);


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


    fetch(
        contextPath +
        "/api/projects/" +
        projectId,
        {

            method: "GET",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            }

        }
    )

    .then(function (response) {

        console.log(
            "GET Project Status:",
            response.status
        );


        if (!response.ok) {

            if (response.status === 401) {

                throw new Error(
                    "Unauthorized. Please login again."
                );
            }


            if (response.status === 403) {

                throw new Error(
                    "You don't have permission."
                );
            }


            if (response.status === 404) {

                throw new Error(
                    "Project not found."
                );
            }


            throw new Error(
                "Failed to load project. HTTP " +
                response.status
            );

        }


        return response.json();

    })

    .then(function (project) {

        console.log(
            "Project:",
            project
        );

        fillForm(project);

    })

    .catch(function (error) {

        console.error(
            "Load Project Error:",
            error
        );

        showMessage(
            error.message,
            "error"
        );

    });

}


// =====================================================
// FILL FORM
// =====================================================

function fillForm(project) {

    document.getElementById("loading")
        .style.display = "none";


    document.getElementById("editCard")
        .style.display = "block";


    document.getElementById("projectName").value =
        project.projectName || "";


    document.getElementById("description").value =
        project.description || "";


    document.getElementById("ownerId").value =
        project.ownerId || "";


    document.getElementById("status").value =
        project.status || "PLANNING";


    document.getElementById("visibility").value =
        project.visibility || "PUBLIC";


    document.getElementById("startDate").value =
        project.startDate || "";


    document.getElementById("endDate").value =
        project.endDate || "";

}


// =====================================================
// UPDATE PROJECT
// =====================================================

document.getElementById("editProjectForm")
    .addEventListener(
        "submit",
        function (event) {

            event.preventDefault();

            updateProject();

        }
    );


// =====================================================
// UPDATE API
// =====================================================

function updateProject() {

    var token =
        localStorage.getItem("token");

    var projectId =
        getProjectId();


    if (!token) {

        showMessage(
            "JWT token not found.",
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


    var project = {

        projectName:
            document.getElementById("projectName").value,

        description:
            document.getElementById("description").value,

        ownerId:
            Number(
                document.getElementById("ownerId").value
            ),

        status:
            document.getElementById("status").value,

        visibility:
            document.getElementById("visibility").value,

        startDate:
            document.getElementById("startDate").value,

        endDate:
            document.getElementById("endDate").value

    };


    console.log(
        "Updating Project:",
        project
    );


    fetch(
        contextPath +
        "/api/projects/" +
        projectId,
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"

            },

            body:
                JSON.stringify(project)

        }
    )

    .then(function (response) {

        console.log(
            "UPDATE Status:",
            response.status
        );


        if (!response.ok) {

            if (response.status === 401) {

                throw new Error(
                    "Unauthorized. Please login again."
                );

            }


            if (response.status === 403) {

                throw new Error(
                    "You don't have permission to update this project."
                );

            }


            if (response.status === 404) {

                throw new Error(
                    "Project not found."
                );

            }


            throw new Error(
                "Update failed. HTTP " +
                response.status
            );

        }


        return response.json();

    })

    .then(function (updatedProject) {

        console.log(
            "Updated Project:",
            updatedProject
        );


        alert(
            "Project updated successfully!"
        );


        window.location.href =
            contextPath +
            "/project-details.jsp?projectId=" +
            projectId;

    })

    .catch(function (error) {

        console.error(
            "Update Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });

}


// =====================================================
// BACK
// =====================================================

function goBack() {

    var projectId =
        getProjectId();


    if (projectId) {

        window.location.href =
            contextPath +
            "/project-details.jsp?projectId=" +
            projectId;

    } else {

        window.location.href =
            contextPath +
            "/project.jsp";

    }

}


// =====================================================
// MESSAGE
// =====================================================

function showMessage(message, type) {

    var element =
        document.getElementById("message");


    if (!element) {

        return;

    }


    element.innerText =
        message;


    element.className =
        "message " + type;


    element.style.display =
        "block";

}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("userId");

    localStorage.removeItem("fullName");

    localStorage.removeItem("email");

    localStorage.removeItem("role");


    window.location.href =
        contextPath +
        "/login.jsp";

}