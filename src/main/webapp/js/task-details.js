
// =====================================================
// DEV COLLAB - TASK DETAILS
// =====================================================

var currentTaskId = null;
var currentTask = null;


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("Task Details JS Loaded");
    console.log("=================================");

    loadUserData();

    currentTaskId = getTaskIdFromUrl();

    console.log("Task ID:", currentTaskId);

    if (!currentTaskId) {

        hideElement("loading");

        showMessage(
            "Task ID not found in URL.",
            "error"
        );

        return;
    }

    loadTask();

});


// =====================================================
// GET TASK ID
// =====================================================

function getTaskIdFromUrl() {

    var params =
        new URLSearchParams(
            window.location.search
        );

    return params.get("taskId");
}


// =====================================================
// USER DATA
// =====================================================

function loadUserData() {

    var fullName =
        localStorage.getItem("fullName");

    var role =
        localStorage.getItem("role");


    var userName =
        document.getElementById("userName");

    var userRole =
        document.getElementById("userRole");

    var userAvatar =
        document.getElementById("userAvatar");


    if (fullName && userName) {

        userName.innerText =
            fullName;
    }


    if (fullName && userAvatar) {

        userAvatar.innerText =
            fullName
                .charAt(0)
                .toUpperCase();
    }


    if (role && userRole) {

        userRole.innerText =
            role;
    }
}


// =====================================================
// LOAD TASK
// =====================================================

function loadTask() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        hideElement("loading");

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    fetch(
        contextPath +
        "/api/tasks/" +
        currentTaskId,
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
            "Task Details Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "Unauthorized. Please login again."
            );
        }


        if (response.status === 403) {

            throw new Error(
                "You don't have permission to view this task."
            );
        }


        if (response.status === 404) {

            throw new Error(
                "Task not found."
            );
        }


        if (!response.ok) {

            throw new Error(
                "Failed to load task. HTTP " +
                response.status
            );
        }


        return response.json();

    })

    .then(function (task) {

        console.log(
            "Task:",
            task
        );


        if (!task) {

            throw new Error(
                "Task data not found."
            );
        }


        currentTask =
            task;


        displayTask(task);


        hideElement("loading");

        showElement("taskContainer");

    })

    .catch(function (error) {

        console.error(
            "Load Task Error:",
            error
        );


        hideElement("loading");

        showMessage(
            error.message,
            "error"
        );

    });
}


// =====================================================
// DISPLAY TASK
// =====================================================

function displayTask(task) {

    setText(
        "taskId",
        "#" + (task.taskId || "-")
    );


    setText(
        "taskName",
        task.taskName || "Untitled Task"
    );


    setText(
        "taskDescription",
        task.description ||
        "No description"
    );


    setText(
        "fullDescription",
        task.description ||
        "No description available."
    );


    setText(
        "projectId",
        task.projectId || "-"
    );


    setText(
        "assignedTo",
        task.assignedTo || "-"
    );


    setText(
        "createdBy",
        task.createdBy || "-"
    );


    setText(
        "startDate",
        formatDate(task.startDate)
    );


    setText(
        "dueDate",
        formatDate(task.dueDate)
    );


    setText(
        "createdAt",
        formatDateTime(task.createdAt)
    );


    setText(
        "updatedAt",
        formatDateTime(task.updatedAt)
    );


    setStatus(
        task.status || "TODO"
    );


    setPriority(
        task.priority || "MEDIUM"
    );
}


// =====================================================
// STATUS
// =====================================================

function setStatus(status) {

    var element =
        document.getElementById(
            "taskStatus"
        );


    if (!element) {
        return;
    }


    element.innerText =
        formatStatus(status);


    element.className =
        "status " +
        getStatusClass(status);
}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

    switch (status) {

        case "COMPLETED":
            return "completed";

        case "IN_PROGRESS":
            return "in-progress";

        case "CANCELLED":
            return "cancelled";

        default:
            return "todo";
    }
}


// =====================================================
// FORMAT STATUS
// =====================================================

function formatStatus(status) {

    if (!status) {
        return "-";
    }


    return String(status)
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(/\b\w/g, function (letter) {

            return letter.toUpperCase();

        });
}


// =====================================================
// PRIORITY
// =====================================================

function setPriority(priority) {

    var element =
        document.getElementById(
            "taskPriority"
        );


    if (!element) {
        return;
    }


    element.innerText =
        priority;


    element.className =
        "priority " +
        String(priority)
            .toLowerCase();
}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(dateValue) {

    if (!dateValue) {
        return "-";
    }


    if (
        typeof dateValue === "string" &&
        /^\d{4}-\d{2}-\d{2}$/.test(dateValue)
    ) {

        return formatDateString(dateValue);
    }


    var date =
        new Date(dateValue);


    if (isNaN(date.getTime())) {
        return "-";
    }


    var day =
        String(
            date.getDate()
        ).padStart(2, "0");


    var month =
        String(
            date.getMonth() + 1
        ).padStart(2, "0");


    var year =
        date.getFullYear();


    return (
        day +
        "/" +
        month +
        "/" +
        year
    );
}


// =====================================================
// DATE STRING
// =====================================================

function formatDateString(value) {

    var parts =
        value.split("-");


    if (parts.length !== 3) {
        return value;
    }


    return (
        parts[2] +
        "/" +
        parts[1] +
        "/" +
        parts[0]
    );
}


// =====================================================
// FORMAT DATE TIME
// =====================================================

function formatDateTime(value) {

    if (!value) {
        return "-";
    }


    var date =
        new Date(value);


    if (isNaN(date.getTime())) {

        return value;
    }


    var datePart =
        formatDate(value);


    var hours =
        String(
            date.getHours()
        ).padStart(2, "0");


    var minutes =
        String(
            date.getMinutes()
        ).padStart(2, "0");


    return (
        datePart +
        " " +
        hours +
        ":" +
        minutes
    );
}


// =====================================================
// EDIT TASK
// =====================================================

function editTask() {

    if (!currentTaskId) {

        showMessage(
            "Task ID not found.",
            "error"
        );

        return;
    }


    window.location.href =
        contextPath +
        "/edit-task.jsp?taskId=" +
        currentTaskId;
}


// =====================================================
// DELETE TASK
// =====================================================

function deleteTask() {

    if (!currentTaskId) {

        showMessage(
            "Task ID not found.",
            "error"
        );

        return;
    }


    var confirmed =
        confirm(
            "Are you sure you want to delete Task #" +
            currentTaskId +
            "?"
        );


    if (!confirmed) {
        return;
    }


    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    fetch(
        contextPath +
        "/api/tasks/" +
        currentTaskId,
        {

            method: "DELETE",

            headers: {

                "Authorization":
                    "Bearer " + token

            }

        }
    )

    .then(function (response) {

        console.log(
            "Delete Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "Unauthorized. Please login again."
            );
        }


        if (response.status === 403) {

            throw new Error(
                "You don't have permission to delete this task."
            );
        }


        if (response.status === 404) {

            throw new Error(
                "Task not found."
            );
        }


        if (!response.ok) {

            return response.text()
                .then(function (text) {

                    throw new Error(
                        text ||
                        "Failed to delete task."
                    );

                });
        }


        return response.text();

    })

    .then(function () {

        alert(
            "Task deleted successfully!"
        );


        window.location.href =
            contextPath +
            "/tasks.jsp";

    })

    .catch(function (error) {

        console.error(
            "Delete Error:",
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

    window.location.href =
        contextPath +
        "/tasks.jsp";
}


// =====================================================
// SET TEXT
// =====================================================

function setText(elementId, value) {

    var element =
        document.getElementById(
            elementId
        );


    if (!element) {
        return;
    }


    element.innerText =
        value === null ||
        value === undefined ||
        value === ""
            ? "-"
            : value;
}


// =====================================================
// SHOW ELEMENT
// =====================================================

function showElement(elementId) {

    var element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.style.display =
            "block";
    }
}


// =====================================================
// HIDE ELEMENT
// =====================================================

function hideElement(elementId) {

    var element =
        document.getElementById(
            elementId
        );


    if (element) {

        element.style.display =
            "none";
    }
}


// =====================================================
// MESSAGE
// =====================================================

function showMessage(message, type) {

    var element =
        document.getElementById(
            "message"
        );


    if (!element) {

        alert(message);

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
