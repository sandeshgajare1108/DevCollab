var allBugs = [];


document.addEventListener(
    "DOMContentLoaded",
    function () {

        console.log("Bugs JS Loaded");

        loadUserData();

        loadBugs();


        document
            .getElementById("searchInput")
            .addEventListener(
                "input",
                filterBugs
            );


        document
            .getElementById("statusFilter")
            .addEventListener(
                "change",
                filterBugs
            );
    }
);


// =====================================================
// USER
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

    var avatar =
        document.getElementById("userAvatar");


    if (fullName && userName) {
        userName.innerText = fullName;
    }

    if (role && userRole) {
        userRole.innerText =
            role.toUpperCase();
    }

    if (fullName && avatar) {
        avatar.innerText =
            fullName
                .charAt(0)
                .toUpperCase();
    }
}


// =====================================================
// LOAD BUGS
// =====================================================

function loadBugs() {

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
        contextPath + "/api/bugs",
        {
            method: "GET",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )
    .then(async response => {

        console.log(
            "Bug API Status:",
            response.status
        );

        if (!response.ok) {

            throw new Error(
                await response.text()
            );
        }

        return response.json();
    })
    .then(bugs => {

        allBugs = bugs || [];

        hideLoading();

        displayBugs(allBugs);
    })
    .catch(error => {

        hideLoading();

        console.error(
            "Bug Load Error:",
            error
        );

        showMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// DISPLAY
// =====================================================

function displayBugs(bugs) {

    var container =
        document.getElementById(
            "bugContainer"
        );

    var empty =
        document.getElementById(
            "emptyMessage"
        );


    container.innerHTML = "";


    if (!bugs ||
        bugs.length === 0) {

        empty.style.display =
            "block";

        return;
    }


    empty.style.display =
        "none";


    bugs.forEach(
        function (bug) {

            var card =
                document.createElement(
                    "div"
                );

            card.className =
                "bug-card";


            card.innerHTML =

                '<div class="bug-top">' +

                    '<span class="bug-id">' +
                        "#" +
                        bug.bugId +
                    '</span>' +

                    '<span class="badge ' +
                        severityClass(
                            bug.severity
                        ) +
                    '">' +
                        escapeHtml(
                            bug.severity
                        ) +
                    '</span>' +

                '</div>' +

                '<h3>' +
                    escapeHtml(
                        bug.title
                    ) +
                '</h3>' +

                '<p>' +
                    escapeHtml(
                        bug.description ||
                        "No description"
                    ) +
                '</p>' +

                '<div class="bug-info">' +

                    '<span>' +
                        '📁 Project: ' +
                        (bug.projectId || "-") +
                    '</span>' +

                    '<span>' +
                        '✅ Task: ' +
                        (bug.taskId || "-") +
                    '</span>' +

                    '<span>' +
                        '🔀 PR: ' +
                        (bug.pullRequestId || "-") +
                    '</span>' +

                    '<span>' +
                        '👤 Reported By: ' +
                        (bug.reportedBy || "-") +
                    '</span>' +

                    '<span>' +
                        '👨‍💻 Assigned To: ' +
                        (bug.assignedTo || "Not assigned") +
                    '</span>' +

                '</div>' +

                '<div class="bug-top">' +

                    '<span class="badge ' +
                        statusClass(
                            bug.status
                        ) +
                    '">' +
                        formatStatus(
                            bug.status
                        ) +
                    '</span>' +

                    '<span>' +
                        'Priority: ' +
                        escapeHtml(
                            bug.priority
                        ) +
                    '</span>' +

                '</div>' +

                '<div class="bug-actions">' +

                    '<select onchange="' +
                        'updateBugStatus(' +
                        bug.bugId +
                        ', this.value)">' +

                        createStatusOptions(
                            bug.status
                        ) +

                    '</select>' +

                    '<input ' +
                        'type="number" ' +
                        'min="1" ' +
                        'placeholder="Assign user ID" ' +
                        'id="assign-' +
                        bug.bugId +
                        '">' +

                    '<button ' +
                        'onclick="assignBug(' +
                        bug.bugId +
                        ')">' +

                        'Assign Developer' +

                    '</button>' +

                '</div>';

            container.appendChild(card);
        }
    );
}


// =====================================================
// CREATE BUG
// =====================================================

function createBug() {

    var token =
        localStorage.getItem("token");


    var projectId =
        numberValue("projectId");

    var taskId =
        numberValue("taskId");

    var pullRequestId =
        numberValue("pullRequestId");

    var title =
        document.getElementById(
            "bugTitle"
        ).value.trim();

    var description =
        document.getElementById(
            "bugDescription"
        ).value.trim();

    var severity =
        document.getElementById(
            "bugSeverity"
        ).value;

    var priority =
        document.getElementById(
            "bugPriority"
        ).value;


    if (!projectId) {

        showMessage(
            "Project ID is required.",
            "error"
        );

        return;
    }


    if (!title) {

        showMessage(
            "Bug title is required.",
            "error"
        );

        return;
    }


    fetch(
        contextPath + "/api/bugs",
        {

            method: "POST",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"
            },

            body:
                JSON.stringify({

                    projectId:
                        projectId,

                    taskId:
                        taskId,

                    pullRequestId:
                        pullRequestId,

                    title:
                        title,

                    description:
                        description,

                    severity:
                        severity,

                    priority:
                        priority
                })
        }
    )
    .then(async response => {

        var text =
            await response.text();

        if (!response.ok) {

            throw new Error(
                text
            );
        }

        return JSON.parse(text);
    })
    .then(bug => {

        console.log(
            "Bug Created:",
            bug
        );

        showMessage(
            "Bug created successfully.",
            "success"
        );

        closeBugModal();

        loadBugs();
    })
    .catch(error => {

        console.error(
            "Create Bug Error:",
            error
        );

        showMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// ASSIGN BUG
// =====================================================

function assignBug(
    bugId
) {

    var token =
        localStorage.getItem("token");

    var input =
        document.getElementById(
            "assign-" + bugId
        );

    var assignedTo =
        Number(
            input.value
        );


    if (!assignedTo) {

        showMessage(
            "Enter developer user ID.",
            "error"
        );

        return;
    }


    fetch(
        contextPath +
        "/api/bugs/" +
        bugId +
        "/assign?assignedTo=" +
        assignedTo,
        {

            method: "PUT",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )
    .then(async response => {

        var text =
            await response.text();

        if (!response.ok) {

            throw new Error(
                text
            );
        }

        return JSON.parse(text);
    })
    .then(bug => {

        showMessage(
            "Bug assigned successfully.",
            "success"
        );

        loadBugs();
    })
    .catch(error => {

        showMessage(
            error.message,
            "error"
        );
    });
}


// =====================================================
// UPDATE STATUS
// =====================================================

function updateBugStatus(
    bugId,
    status
) {

    var token =
        localStorage.getItem("token");


    var resolution = "";


    if (
        status === "FIXED" ||
        status === "CLOSED"
    ) {

        resolution =
            prompt(
                "Enter resolution:"
            ) || "";
    }


    fetch(
        contextPath +
        "/api/bugs/" +
        bugId +
        "/status?status=" +
        encodeURIComponent(
            status
        ) +
        "&resolution=" +
        encodeURIComponent(
            resolution
        ),
        {

            method: "PUT",

            headers: {
                "Authorization":
                    "Bearer " + token
            }
        }
    )
    .then(async response => {

        var text =
            await response.text();

        if (!response.ok) {

            throw new Error(
                text
            );
        }

        return JSON.parse(text);
    })
    .then(bug => {

        showMessage(
            "Bug status updated successfully.",
            "success"
        );

        loadBugs();
    })
    .catch(error => {

        showMessage(
            error.message,
            "error"
        );

        loadBugs();
    });
}


// =====================================================
// FILTER
// =====================================================

function filterBugs() {

    var search =
        document.getElementById(
            "searchInput"
        ).value
         .toLowerCase()
         .trim();


    var status =
        document.getElementById(
            "statusFilter"
        ).value;


    var filtered =
        allBugs.filter(
            function(bug) {

                var matchesSearch =
                    (
                        bug.title || ""
                    )
                    .toLowerCase()
                    .includes(search)
                    ||
                    (
                        bug.description || ""
                    )
                    .toLowerCase()
                    .includes(search);


                var matchesStatus =
                    status === "ALL" ||
                    bug.status === status;


                return (
                    matchesSearch &&
                    matchesStatus
                );
            }
        );


    displayBugs(
        filtered
    );
}


// =====================================================
// STATUS OPTIONS
// =====================================================

function createStatusOptions(
    current
) {

    var statuses = [

        "OPEN",
        "ASSIGNED",
        "IN_PROGRESS",
        "FIXED",
        "RETESTING",
        "CLOSED",
        "REOPENED"

    ];


    var html = "";


    statuses.forEach(
        function(status) {

            html +=
                '<option value="' +
                status +
                '" ' +
                (
                    status === current
                        ? "selected"
                        : ""
                ) +
                '>' +
                formatStatus(status) +
                '</option>';
        }
    );


    return html;
}


// =====================================================
// OPEN MODAL
// =====================================================

function openBugModal() {

    document.getElementById(
        "bugModal"
    ).style.display =
        "flex";
}


// =====================================================
// CLOSE MODAL
// =====================================================

function closeBugModal() {

    document.getElementById(
        "bugModal"
    ).style.display =
        "none";
}


// =====================================================
// UTILITY
// =====================================================

function numberValue(id) {

    var value =
        document.getElementById(id).value;

    if (!value) {
        return null;
    }

    var number =
        Number(value);

    return isNaN(number)
        ? null
        : number;
}


function severityClass(
    severity
) {

    return "severity-" +
        String(
            severity || "medium"
        )
        .toLowerCase();
}


function statusClass(
    status
) {

    return "status-" +
        String(
            status || "open"
        )
        .toLowerCase()
        .replace(
            "_",
            "-"
        );
}


function formatStatus(
    status
) {

    return String(
        status || "-"
    )
    .replace(
        /_/g,
        " "
    );
}


function escapeHtml(
    value
) {

    return String(
        value || ""
    )
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


function hideLoading() {

    var loading =
        document.getElementById(
            "loading"
        );

    if (loading) {

        loading.style.display =
            "none";
    }
}


function showMessage(
    message,
    type
) {

    var element =
        document.getElementById(
            "message"
        );

    if (!element) {
        return;
    }

    element.innerText =
        message;

    element.className =
        "message " +
        (
            type || "success"
        );

    element.style.display =
        "block";


    setTimeout(
        function() {

            element.style.display =
                "none";

        },
        4000
    );
}


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