
// =====================================================
// DEV COLLAB - EDIT TASK
// =====================================================

var currentTaskId = null;
var currentTask = null;


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("Edit Task JS Loaded");
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

    var form = document.getElementById("taskForm");

    if (form) {

        form.addEventListener("submit", function (event) {

            event.preventDefault();

            updateTask();

        });

    } else {

        console.error("taskForm element not found.");

    }

});


// =====================================================
// GET TASK ID FROM URL
// =====================================================

function getTaskIdFromUrl() {

    var params =
        new URLSearchParams(window.location.search);

    return params.get("taskId");

}


// =====================================================
// LOAD USER DATA
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


    if (userName && fullName) {

        userName.innerText = fullName;

    }


    if (userAvatar && fullName) {

        userAvatar.innerText =
            fullName
                .charAt(0)
                .toUpperCase();

    }


    if (userRole && role) {

        userRole.innerText = role;

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


    console.log(
        "Loading task:",
        currentTaskId
    );


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
            "Task GET Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "Unauthorized. Please login again."
            );

        }


        if (response.status === 403) {

            throw new Error(
                "You don't have permission to edit this task."
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
            "Task Details:",
            task
        );


        if (!task) {

            throw new Error(
                "Task data not found."
            );

        }


        currentTask = task;


        fillTaskForm(task);


        hideElement("loading");

        showElement("formContainer");


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
// FILL TASK FORM
// =====================================================

function fillTaskForm(task) {

    console.log(
        "Filling task form:",
        task
    );


    setValue(
        "taskId",
        task.taskId
    );


    setValue(
        "projectId",
        task.projectId
    );


    setValue(
        "taskName",
        task.taskName
    );


    setValue(
        "description",
        task.description
    );


    setValue(
        "assignedTo",
        task.assignedTo
    );


    setValue(
        "createdBy",
        task.createdBy
    );


    setValue(
        "status",
        task.status || "TODO"
    );


    setValue(
        "priority",
        task.priority || "MEDIUM"
    );


    setValue(
        "startDate",
        formatDateForInput(task.startDate)
    );


    setValue(
        "dueDate",
        formatDateForInput(task.dueDate)
    );


    console.log(
        "Task form filled successfully."
    );

}


// =====================================================
// UPDATE TASK
// =====================================================

function updateTask() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    if (!currentTaskId) {

        showMessage(
            "Task ID not found.",
            "error"
        );

        return;
    }


    // =================================================
    // VALIDATE TASK NAME
    // =================================================

    var taskName =
        getValue("taskName").trim();


    if (!taskName) {

        showMessage(
            "Task name is required.",
            "error"
        );

        return;
    }


    // =================================================
    // CREATE REQUEST BODY
    // =================================================

    var task = {

    	    projectId:
    	        getNumberValue("projectId"),

    	    taskName:
    	        taskName,

    	    description:
    	        getValue("description").trim(),

    	    assignedTo:
    	        getNumberValue("assignedTo"),

    	    status:
    	        getValue("status") || "TODO",

    	    priority:
    	        getValue("priority") || "MEDIUM",

    	    startDate:
    	        getValue("startDate") || null,

    	    dueDate:
    	        getValue("dueDate") || null
    	};


    console.log(
        "================================="
    );

    console.log(
        "Updating Task ID:",
        currentTaskId
    );

    console.log(
        "Request Body:",
        task
    );

    console.log(
        "================================="
    );


    var updateButton =
        document.getElementById("updateButton");


    if (updateButton) {

        updateButton.disabled = true;

        updateButton.innerText =
            "Updating...";

    }


    showMessage(
        "Updating task...",
        "info"
    );


    // =================================================
    // PUT REQUEST
    // =================================================

    fetch(
        contextPath +
        "/api/tasks/" +
        currentTaskId,
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"

            },

            body:
                JSON.stringify(task)

        }
    )

    .then(function (response) {

        console.log(
            "PUT Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "Unauthorized. Please login again."
            );

        }


        if (response.status === 403) {

            throw new Error(
                "You don't have permission to update this task."
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

                    console.error(
                        "Server Error:",
                        text
                    );


                    throw new Error(
                        text ||
                        "Failed to update task."
                    );

                });

        }


        return response.json();

    })


    .then(function (updatedTask) {

        console.log(
            "Updated Task:",
            updatedTask
        );


        showMessage(
            "Task updated successfully!",
            "success"
        );


        // =================================================
        // GO TO TASK DETAILS
        // =================================================

        setTimeout(function () {

            window.location.href =
                contextPath +
                "/task-details.jsp?taskId=" +
                currentTaskId;

        }, 1000);

    })


    .catch(function (error) {

        console.error(
            "Update Task Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );


        if (updateButton) {

            updateButton.disabled =
                false;

            updateButton.innerText =
                "💾 Update Task";

        }

    });

}


// =====================================================
// GET VALUE
// =====================================================

function getValue(elementId) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Element not found:",
            elementId
        );

        return "";

    }


    return element.value;

}


// =====================================================
// GET NUMBER VALUE
// =====================================================

function getNumberValue(elementId) {

    var value =
        getValue(elementId);


    if (
        value === null ||
        value === undefined ||
        value === ""
    ) {

        return null;

    }


    var number =
        Number(value);


    if (isNaN(number)) {

        return null;

    }


    return number;

}


// =====================================================
// SET VALUE
// =====================================================

function setValue(elementId, value) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Element not found:",
            elementId
        );

        return;

    }


    if (
        value === null ||
        value === undefined
    ) {

        element.value = "";

    } else {

        element.value = value;

    }

}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDateForInput(dateValue) {

    if (!dateValue) {

        return "";

    }


    // Already YYYY-MM-DD

    if (
        typeof dateValue === "string" &&
        /^\d{4}-\d{2}-\d{2}$/.test(dateValue)
    ) {

        return dateValue;

    }


    try {

        var date =
            new Date(dateValue);


        if (isNaN(date.getTime())) {

            return "";

        }


        var year =
            date.getFullYear();


        var month =
            String(
                date.getMonth() + 1
            ).padStart(2, "0");


        var day =
            String(
                date.getDate()
            ).padStart(2, "0");


        return (
            year +
            "-" +
            month +
            "-" +
            day
        );

    }

    catch (error) {

        console.error(
            "Date formatting error:",
            error
        );

        return "";

    }

}


// =====================================================
// SHOW ELEMENT
// =====================================================

function showElement(elementId) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Cannot show element. Not found:",
            elementId
        );

        return;

    }


    element.style.display =
        "block";

}


// =====================================================
// HIDE ELEMENT
// =====================================================

function hideElement(elementId) {

    var element =
        document.getElementById(elementId);


    if (!element) {

        console.warn(
            "Cannot hide element. Not found:",
            elementId
        );

        return;

    }


    element.style.display =
        "none";

}


// =====================================================
// MESSAGE
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


    if (type === "success") {

        setTimeout(function () {

            if (element) {

                element.style.display =
                    "none";

            }

        }, 3000);

    }

}


// =====================================================
// BACK TO TASK DETAILS
// =====================================================

function goBack() {

    if (currentTaskId) {

        window.location.href =
            contextPath +
            "/task-details.jsp?taskId=" +
            currentTaskId;

    }

    else {

        window.location.href =
            contextPath +
            "/tasks.jsp";

    }

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
