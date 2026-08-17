// =====================================================
// DEV COLLAB - TASKS FINAL JS
// =====================================================

var allTasks = [];


// =====================================================
// PAGE LOAD
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        console.log("Tasks JS Loaded");

        loadUserData();

        loadTasks();


        var taskForm =
            document.getElementById("taskForm");


        if (taskForm) {

            taskForm.addEventListener(
                "submit",
                function (event) {

                    event.preventDefault();

                    createTask();

                }
            );
        }


        var searchInput =
            document.getElementById("searchInput");


        if (searchInput) {

            searchInput.addEventListener(
                "input",
                function () {

                    filterTasks();

                }
            );
        }


        var statusFilter =
            document.getElementById("statusFilter");


        if (statusFilter) {

            statusFilter.addEventListener(
                "change",
                function () {

                    filterTasks();

                }
            );
        }

    }
);


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
// LOAD TASKS
// =====================================================

function loadTasks() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    var loading =
        document.getElementById("loading");


    if (loading) {

        loading.style.display =
            "block";
    }


    fetch(
        contextPath + "/api/tasks",
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

    .then(
        function (response) {

            console.log(
                "Task API Status:",
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
                        "You don't have permission to view tasks."
                    );
                }


                throw new Error(
                    "Failed to load tasks. HTTP " +
                    response.status
                );
            }


            return response.json();

        }
    )


    .then(
        function (tasks) {

            allTasks =
                tasks || [];


            var loading =
                document.getElementById("loading");


            if (loading) {

                loading.style.display =
                    "none";
            }


            displayTasks(allTasks);

        }
    )


    .catch(
        function (error) {

            console.error(
                "Task Error:",
                error
            );


            var loading =
                document.getElementById("loading");


            if (loading) {

                loading.style.display =
                    "none";
            }


            showMessage(
                error.message,
                "error"
            );

        }
    );
}


// =====================================================
// DISPLAY TASKS
// =====================================================

function displayTasks(tasks) {

    var container =
        document.getElementById(
            "taskContainer"
        );


    var empty =
        document.getElementById(
            "emptyMessage"
        );


    if (!container) {

        return;
    }


    container.innerHTML = "";


    if (!tasks ||
        tasks.length === 0) {

        if (empty) {

            empty.style.display =
                "block";
        }

        return;
    }


    if (empty) {

        empty.style.display =
            "none";
    }


    tasks.forEach(
        function (task) {

            var taskId =
                task.taskId;


            var card =
                document.createElement("div");


            card.className =
                "task-card";


            var currentStatus =
                task.status ||
                "TODO";


            card.innerHTML =

                '<div class="task-top">' +

                    '<span class="task-id">' +
                        "#" +
                        taskId +
                    '</span>' +

                    '<span class="status ' +
                        getStatusClass(
                            currentStatus
                        ) +
                    '">' +

                        formatStatus(
                            currentStatus
                        ) +

                    '</span>' +

                '</div>' +


                '<h3>' +

                    escapeHtml(
                        task.taskName ||
                        "Untitled Task"
                    ) +

                '</h3>' +


                '<p>' +

                    escapeHtml(
                        task.description ||
                        "No description"
                    ) +

                '</p>' +


                '<div class="task-info">' +

                    '<span>' +
                        '📁 Project: ' +
                        (task.projectId || "-") +
                    '</span>' +

                    '<span>' +
                        '👤 Assigned: ' +
                        (task.assignedTo || "-") +
                    '</span>' +

                    '<span>' +
                        '⚡ ' +
                        formatPriority(
                            task.priority
                        ) +
                    '</span>' +

                    '<span>' +
                        '📅 Start: ' +
                        formatDate(
                            task.startDate
                        ) +
                    '</span>' +

                    '<span>' +
                        '📅 Due: ' +
                        formatDate(
                            task.dueDate
                        ) +
                    '</span>' +

                '</div>' +


                '<div class="status-update-section">' +

                    '<label>' +
                        'Task Status' +
                    '</label>' +

                    createStatusDropdown(
                        task
                    ) +

                '</div>' +


                '<div class="task-actions">' +

                    '<button ' +
                        'type="button" ' +
                        'onclick="viewTask(' +
                        taskId +
                        ')">' +

                        'View' +

                    '</button>' +

                    '<button ' +
                        'type="button" ' +
                        'class="edit-action" ' +
                        'onclick="editTask(' +
                        taskId +
                        ')">' +

                        'Edit' +

                    '</button>' +

                    '<button ' +
                        'type="button" ' +
                        'class="delete-action" ' +
                        'onclick="deleteTask(' +
                        taskId +
                        ')">' +

                        'Delete' +

                    '</button>' +

                '</div>';


            container.appendChild(card);

        }
    );
}


// =====================================================
// CREATE STATUS DROPDOWN
// =====================================================

function createStatusDropdown(task) {

    var statuses = [

        "TODO",

        "IN_PROGRESS",

        "CODE_REVIEW",

        "TESTING",

        "BUG_FOUND",

        "FIXING",

        "RETESTING",

        "APPROVED",

        "COMPLETED"

    ];


    var html =

        '<select ' +

            'class="task-status-select" ' +

            'data-task-id="' +
                task.taskId +
            '" ' +

            'onchange="changeTaskStatus(this)">' ;


    statuses.forEach(
        function (status) {

            var selected =
                status === task.status
                    ? "selected"
                    : "";


            html +=

                '<option value="' +
                    status +
                    '" ' +
                    selected +
                '>' +

                    formatStatus(status) +

                '</option>';

        }
    );


    html += '</select>';


    return html;
}


// =====================================================
// CHANGE TASK STATUS
// =====================================================

function changeTaskStatus(
    selectElement
) {

    if (!selectElement) {

        return;
    }


    var taskId =
        selectElement.getAttribute(
            "data-task-id"
        );


    var newStatus =
        selectElement.value;


    var token =
        localStorage.getItem("token");


    if (!taskId) {

        showMessage(
            "Task ID not found.",
            "error"
        );

        return;
    }


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    console.log(
        "================================"
    );


    console.log(
        "TASK STATUS UPDATE"
    );


    console.log(
        "Task ID:",
        taskId
    );


    console.log(
        "New Status:",
        newStatus
    );


    fetch(
        contextPath +
        "/api/tasks/" +
        taskId +
        "/status?status=" +
        encodeURIComponent(
            newStatus
        ),
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        async function (response) {

            console.log(
                "Status Update HTTP:",
                response.status
            );


            var text =
                await response.text();


            console.log(
                "Status Update Response:",
                text
            );


            if (!response.ok) {

                throw new Error(
                    text ||
                    "Failed to update task status"
                );
            }


            return JSON.parse(text);

        }
    )


    .then(
        function (data) {

            console.log(
                "Task status updated:",
                data
            );


            showMessage(
                "Task status updated successfully.",
                "success"
            );


            loadTasks();

        }
    )


    .catch(
        function (error) {

            console.error(
                "Task Status Update Error:",
                error
            );


            showMessage(
                error.message,
                "error"
            );


            loadTasks();

        }
    );
}


// =====================================================
// SEARCH + FILTER
// =====================================================

function filterTasks() {

    var searchElement =
        document.getElementById(
            "searchInput"
        );


    var statusElement =
        document.getElementById(
            "statusFilter"
        );


    var search =
        searchElement
            ? searchElement.value
                .toLowerCase()
                .trim()
            : "";


    var status =
        statusElement
            ? statusElement.value
            : "ALL";


    var filtered =
        allTasks.filter(
            function (task) {

                var name =
                    String(
                        task.taskName ||
                        ""
                    )
                    .toLowerCase();


                var description =
                    String(
                        task.description ||
                        ""
                    )
                    .toLowerCase();


                var matchesSearch =
                    name.includes(search) ||
                    description.includes(search);


                var matchesStatus =
                    status === "ALL" ||
                    task.status === status;


                return (
                    matchesSearch &&
                    matchesStatus
                );

            }
        );


    displayTasks(filtered);
}


// =====================================================
// VIEW TASK
// =====================================================

function viewTask(taskId) {

    if (!taskId) {

        return;
    }


    window.location.href =
        contextPath +
        "/task-details.jsp?taskId=" +
        taskId;
}


// =====================================================
// EDIT TASK
// =====================================================

function editTask(taskId) {

    if (!taskId) {

        return;
    }


    window.location.href =
        contextPath +
        "/edit-task.jsp?taskId=" +
        taskId;
}


// =====================================================
// DELETE TASK
// =====================================================

function deleteTask(taskId) {

    if (!taskId) {

        showMessage(
            "Task ID not found.",
            "error"
        );

        return;
    }


    var confirmed =
        confirm(
            "Are you sure you want to delete Task #" +
            taskId +
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

        window.location.href =
            contextPath +
            "/login.jsp";

        return;
    }


    fetch(
        contextPath +
        "/api/tasks/" +
        taskId,
        {

            method: "DELETE",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        async function (response) {

            console.log(
                "Delete Task Status:",
                response.status
            );


            var message =
                await response.text();


            if (response.status === 401) {

                throw new Error(
                    "Unauthorized. Please login again."
                );
            }


            if (response.status === 403) {

                throw new Error(
                    message ||
                    "You don't have permission to delete this task."
                );
            }


            if (response.status === 404) {

                throw new Error(
                    "Task not found."
                );
            }


            if (!response.ok) {

                throw new Error(
                    message ||
                    "Failed to delete task."
                );
            }


            return message;

        }
    )


    .then(
        function (message) {

            console.log(
                "Delete Response:",
                message
            );


            showMessage(
                message ||
                "Task deleted successfully.",
                "success"
            );


            loadTasks();

        }
    )


    .catch(
        function (error) {

            console.error(
                "Delete Task Error:",
                error
            );


            showMessage(
                error.message,
                "error"
            );

        }
    );
}


// =====================================================
// OPEN CREATE TASK MODAL
// =====================================================

function openCreateTask() {

    var modal =
        document.getElementById(
            "taskModal"
        );


    var form =
        document.getElementById(
            "taskForm"
        );


    if (form) {

        form.reset();
    }


    /*
     * Default create values.
     */

    var status =
        document.getElementById(
            "taskStatus"
        );


    if (status) {

        status.value =
            "TODO";
    }


    var priority =
        document.getElementById(
            "priority"
        );


    if (priority) {

        priority.value =
            "MEDIUM";
    }


    if (modal) {

        modal.style.display =
            "flex";
    }
}


// =====================================================
// CLOSE CREATE TASK MODAL
// =====================================================

function closeTaskModal() {

    var modal =
        document.getElementById(
            "taskModal"
        );


    if (modal) {

        modal.style.display =
            "none";
    }

}


// =====================================================
// CREATE TASK
// =====================================================

function createTask() {

    var token =
        localStorage.getItem("token");


    var userId =
        localStorage.getItem("userId");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return;
    }


    if (!userId) {

        showMessage(
            "User ID not found. Please login again.",
            "error"
        );

        return;
    }


    var taskName =
        document
            .getElementById("taskTitle")
            .value
            .trim();


    var description =
        document
            .getElementById("taskDescription")
            .value
            .trim();


    var projectId =
        document
            .getElementById("projectId")
            .value;


    var assignedTo =
        getNumberValue("assignedTo");


    var status =
        document
            .getElementById("taskStatus")
            .value;


    var priority =
        document
            .getElementById("priority")
            .value;


    var startDate =
        document
            .getElementById("startDate")
            .value ||
        null;


    var dueDate =
        document
            .getElementById("dueDate")
            .value ||
        null;


    if (!taskName) {

        showMessage(
            "Task name is required.",
            "error"
        );

        return;
    }


    if (!projectId) {

        showMessage(
            "Project ID is required.",
            "error"
        );

        return;
    }


    var task = {

        projectId:
            Number(projectId),

        taskName:
            taskName,

        description:
            description || null,

        assignedTo:
            assignedTo,

        /*
         * Backend now uses JWT user.
         * This value is ignored/overridden by backend.
         */
        createdBy:
            Number(userId),

        status:
            status || "TODO",

        priority:
            priority || "MEDIUM",

        startDate:
            startDate,

        dueDate:
            dueDate
    };


    console.log(
        "================================"
    );


    console.log(
        "CREATE TASK REQUEST"
    );


    console.log(
        task
    );


    console.log(
        "================================"
    );


    var saveButton =
        document.getElementById(
            "saveTaskButton"
        );


    if (saveButton) {

        saveButton.disabled =
            true;

        saveButton.innerText =
            "Saving...";

    }


    fetch(
        contextPath +
        "/api/tasks",
        {

            method: "POST",

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


    .then(
        async function (response) {

            console.log(
                "Create Task HTTP Status:",
                response.status
            );


            var text =
                await response.text();


            if (!response.ok) {

                throw new Error(
                    text ||
                    "Failed to create task. HTTP " +
                    response.status
                );
            }


            return text;

        }
    )


    .then(
        function (text) {

            console.log(
                "Create Task Response:",
                text
            );


            showMessage(
                "Task created successfully!",
                "success"
            );


            closeTaskModal();


            loadTasks();

        }
    )


    .catch(
        function (error) {

            console.error(
                "CREATE TASK ERROR:",
                error
            );


            showMessage(
                error.message,
                "error"
            );

        }
    )


    .finally(
        function () {

            if (saveButton) {

                saveButton.disabled =
                    false;

                saveButton.innerText =
                    "Save Task";

            }

        }
    );
}


// =====================================================
// NUMBER VALUE
// =====================================================

function getNumberValue(id) {

    var element =
        document.getElementById(id);


    if (!element) {

        return null;
    }


    var value =
        element.value;


    if (!value) {

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
// FORMAT STATUS
// =====================================================

function formatStatus(status) {

    if (!status) {

        return "-";
    }


    return String(status)
        .replace(/_/g, " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            function (letter) {

                return letter.toUpperCase();

            }
        );
}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

    if (!status) {

        return "todo";
    }


    switch (
        String(status).toUpperCase()
    ) {

        case "IN_PROGRESS":
            return "in-progress";

        case "CODE_REVIEW":
            return "code-review";

        case "TESTING":
            return "testing";

        case "BUG_FOUND":
            return "bug-found";

        case "FIXING":
            return "fixing";

        case "RETESTING":
            return "retesting";

        case "APPROVED":
            return "approved";

        case "COMPLETED":
            return "completed";

        case "TODO":
        default:
            return "todo";

    }
}


// =====================================================
// FORMAT PRIORITY
// =====================================================

function formatPriority(priority) {

    if (!priority) {

        return "-";
    }


    return String(priority)
        .replace(/_/g, " ");
}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(value) {

    if (!value) {

        return "-";
    }


    var date =
        new Date(value);


    if (isNaN(date.getTime())) {

        return value;
    }


    return date.toLocaleDateString();

}


// =====================================================
// ESCAPE HTML
// =====================================================

function escapeHtml(value) {

    return String(value)

        .replace(
            /&/g,
            "&amp;"
        )

        .replace(
            /</g,
            "&lt;"
        )

        .replace(
            />/g,
            "&gt;"
        )

        .replace(
            /"/g,
            "&quot;"
        )

        .replace(
            /'/g,
            "&#039;"
        );
}


// =====================================================
// MESSAGE
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

        console.error(message);

        return;
    }


    element.innerText =
        message;


    element.className =
        "message " +
        (
            type ||
            "success"
        );


    element.style.display =
        "block";


    setTimeout(
        function () {

            element.style.display =
                "none";

            element.innerText =
                "";

        },
        4000
    );
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