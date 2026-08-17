 id="k2t9qa"
console.log("=================================");
console.log("DevCollab Notifications JS Loaded");
console.log("=================================");


// =====================================================
// DOM READY
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadUserInfo();

        loadNotifications();

    }
);


// =====================================================
// USER INFO
// =====================================================

function loadUserInfo() {

    var fullName =
        localStorage.getItem("fullName");


    var role =
        localStorage.getItem("role");


    if (!fullName) {

        fullName =
            "User";
    }


    if (!role) {

        role =
            "USER";
    }


    setText(
        "sidebarName",
        fullName
    );


    setText(
        "sidebarRole",
        role
    );


    var firstLetter =
        fullName
            .charAt(0)
            .toUpperCase();


    setText(
        "sidebarAvatar",
        firstLetter
    );
}


// =====================================================
// GET TOKEN
// =====================================================

function getToken() {

    var token =
        localStorage.getItem("token");


    if (!token) {

        showMessage(
            "JWT token not found. Please login again.",
            "error"
        );

        return null;
    }


    return token;
}


// =====================================================
// LOAD NOTIFICATIONS
// =====================================================

function loadNotifications() {

    var token =
        getToken();


    if (!token) {

        return;
    }


    console.log(
        "Loading notifications..."
    );


    fetch(
        contextPath +
        "/api/notifications",
        {
            method: "GET",

            headers: {

                "Authorization":
                    "Bearer " +
                    token,

                "Content-Type":
                    "application/json"
            }
        }
    )


    .then(
        function (response) {

            console.log(
                "Notifications HTTP Status:",
                response.status
            );


            if (response.status === 401) {

                throw new Error(
                    "401 Unauthorized. Please login again."
                );
            }


            if (response.status === 403) {

                throw new Error(
                    "403 Forbidden. Access denied."
                );
            }


            if (!response.ok) {

                throw new Error(
                    "Notification API Error: " +
                    response.status
                );
            }


            return response.json();
        }
    )


    .then(
        function (data) {

            console.log(
                "Notifications:",
                data
            );


            displayNotifications(
                data
            );


            updateCounts(
                data
            );
        }
    )


    .catch(
        function (error) {

            console.error(
                "Notification Error:",
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
// DISPLAY
// =====================================================

function displayNotifications(
    notifications
) {

    var list =
        document.getElementById(
            "notificationList"
        );


    if (!list) {

        return;
    }


    list.innerHTML =
        "";


    if (!notifications ||
        notifications.length === 0) {

        list.innerHTML = `

            <div class="empty">

                <div class="empty-icon">
                    🔔
                </div>

                <h3>
                    No notifications
                </h3>

                <p>
                    You're all caught up!
                </p>

            </div>

        `;


        return;
    }


    notifications.forEach(
        function (notification) {

            var item =
                document.createElement(
                    "div"
                );


            item.className =
                "notification-item " +
                (
                    notification.read
                        ? ""
                        : "unread"
                );


            var icon =
                getNotificationIcon(
                    notification.type
                );


            item.innerHTML = `

                <div class="notification-icon">
                    ${icon}
                </div>


                <div class="notification-content">

                    <h3>
                        ${escapeHtml(
                            notification.title
                        )}
                    </h3>


                    <p>
                        ${escapeHtml(
                            notification.message
                        )}
                    </p>


                    <div class="notification-time">

                        ${formatDate(
                            notification.createdAt
                        )}

                    </div>

                </div>


                <div class="notification-actions">

                    ${
                        notification.read
                            ? ""
                            : `
                                <button
                                    title="Mark as read"
                                    onclick="markAsRead(${notification.notificationId})">

                                    ✓

                                </button>
                            `
                    }


                    <button
                        class="delete-notification"
                        title="Delete"
                        onclick="deleteNotification(${notification.notificationId})">

                        🗑

                    </button>

                </div>

            `;


            list.appendChild(
                item
            );

        }
    );
}


// =====================================================
// COUNTS
// =====================================================

function updateCounts(
    notifications
) {

    var total =
        notifications
            ? notifications.length
            : 0;


    var unread =
        notifications
            ? notifications.filter(
                function (notification) {

                    return !notification.read;

                }
            ).length
            : 0;


    setText(
        "totalCount",
        total
    );


    setText(
        "unreadCount",
        unread
    );


    setText(
        "menuBadge",
        unread
    );


    var badge =
        document.getElementById(
            "menuBadge"
        );


    if (badge) {

        badge.style.display =
            unread > 0
                ? "inline-block"
                : "none";
    }
}


// =====================================================
// MARK AS READ
// =====================================================

function markAsRead(
    notificationId
) {

    var token =
        getToken();


    if (!token) {

        return;
    }


    fetch(
        contextPath +
        "/api/notifications/" +
        notificationId +
        "/read",
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " +
                    token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        function (response) {

            if (response.status === 401) {

                throw new Error(
                    "401 Unauthorized. Please login again."
                );
            }


            if (response.status === 403) {

                throw new Error(
                    "403 Forbidden. Access denied."
                );
            }


            if (!response.ok) {

                throw new Error(
                    "Unable to mark notification as read."
                );
            }


            return response.text();
        }
    )


    .then(
        function () {

            showMessage(
                "Notification marked as read.",
                "success"
            );


            loadNotifications();

        }
    )


    .catch(
        function (error) {

            console.error(
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
// MARK ALL AS READ
// =====================================================

function markAllAsRead() {

    var token =
        getToken();


    if (!token) {

        return;
    }


    fetch(
        contextPath +
        "/api/notifications/read-all",
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " +
                    token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        async function (response) {

            var text =
                await response.text();


            console.log(
                "Mark All Read Status:",
                response.status
            );


            console.log(
                "Mark All Read Response:",
                text
            );


            if (!response.ok) {

                throw new Error(
                    text ||
                    "Unable to mark all notifications as read."
                );
            }


            return text;
        }
    )


    .then(
        function () {

            showMessage(
                "All notifications marked as read.",
                "success"
            );


            loadNotifications();

        }
    )


    .catch(
        function (error) {

            console.error(
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
// DELETE SINGLE
// =====================================================

function deleteNotification(
    notificationId
) {

    var token =
        getToken();


    if (!token) {

        return;
    }


    if (!confirm(
        "Delete this notification?"
    )) {

        return;
    }


    fetch(
        contextPath +
        "/api/notifications/" +
        notificationId,
        {

            method: "DELETE",

            headers: {

                "Authorization":
                    "Bearer " +
                    token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        async function (response) {

            var text =
                await response.text();


            console.log(
                "Delete Notification Status:",
                response.status
            );


            if (!response.ok) {

                throw new Error(
                    text ||
                    "Unable to delete notification."
                );
            }


            return text;
        }
    )


    .then(
        function () {

            showMessage(
                "Notification deleted.",
                "success"
            );


            loadNotifications();

        }
    )


    .catch(
        function (error) {

            console.error(
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
// DELETE ALL
// =====================================================

function deleteAllNotifications() {

    var token =
        getToken();


    if (!token) {

        return;
    }


    if (!confirm(
        "Delete all notifications?"
    )) {

        return;
    }


    fetch(
        contextPath +
        "/api/notifications",
        {

            method: "DELETE",

            headers: {

                "Authorization":
                    "Bearer " +
                    token,

                "Content-Type":
                    "application/json"
            }

        }
    )


    .then(
        async function (response) {

            var text =
                await response.text();


            console.log(
                "Delete All Status:",
                response.status
            );


            console.log(
                "Delete All Response:",
                text
            );


            if (!response.ok) {

                throw new Error(
                    text ||
                    "Unable to delete notifications."
                );
            }


            return text;
        }
    )


    .then(
        function () {

            showMessage(
                "All notifications deleted.",
                "success"
            );


            loadNotifications();

        }
    )


    .catch(
        function (error) {

            console.error(
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
// ICON
// =====================================================

function getNotificationIcon(
    type
) {

    switch (
        type
    ) {

        case "TASK_ASSIGNED":

            return "✅";


        case "TASK_UPDATED":

            return "🔄";


        case "TASK_STATUS":

            return "🔄";


        case "PROJECT_INVITATION":

            return "📁";


        case "PROJECT_UPDATED":

            return "📊";


        case "TEAM_ADDED":

            return "👥";


        case "BUG_ASSIGNED":

            return "🐞";


        case "PR_APPROVED":

            return "✅";


        case "CODE_REVIEW":

            return "🔍";


        case "SYSTEM":

            return "⚙️";


        default:

            return "🔔";
    }
}


// =====================================================
// DATE
// =====================================================

function formatDate(
    value
) {

    if (!value) {

        return "-";
    }


    var date =
        new Date(value);


    if (
        isNaN(
            date.getTime()
        )
    ) {

        return value;
    }


    return date.toLocaleString();
}


// =====================================================
// SAFE HTML
// =====================================================

function escapeHtml(
    value
) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";
    }


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
// SET TEXT
// =====================================================

function setText(
    id,
    value
) {

    var element =
        document.getElementById(
            id
        );


    if (!element) {

        return;
    }


    element.innerText =
        value === null ||
        value === undefined
            ? ""
            : value;
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

        return;
    }


    element.innerText =
        message;


    element.className =
        "message " +
        (
            type ||
            "error"
        );


    element.style.display =
        "block";


    setTimeout(
        function () {

            element.innerText =
                "";

            element.className =
                "message";

            element.style.display =
                "none";

        },
        4000
    );
}


// =====================================================
// LOGOUT
// =====================================================

function logout() {

    localStorage.removeItem(
        "token"
    );

    localStorage.removeItem(
        "userId"
    );

    localStorage.removeItem(
        "fullName"
    );

    localStorage.removeItem(
        "email"
    );

    localStorage.removeItem(
        "role"
    );


    window.location.href =
        contextPath +
        "/login.jsp";
}
