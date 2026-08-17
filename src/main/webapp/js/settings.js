document.addEventListener(
    "DOMContentLoaded",
    function () {

        console.log(
            "Settings JS Loaded"
        );

        loadUserInformation();

        loadSettings();

    }
);


/*
 * =====================================================
 * GET USER ID
 * =====================================================
 */

function getUserId() {

    var userId =
        localStorage.getItem("userId");


    if (!userId) {

        showMessage(
            "User ID not found. Please login again.",
            "error"
        );

        return null;
    }


    return userId;
}


/*
 * =====================================================
 * GET TOKEN
 * =====================================================
 */

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


/*
 * =====================================================
 * LOAD USER INFORMATION
 * =====================================================
 */

function loadUserInformation() {

    var userId =
        getUserId();

    var token =
        getToken();


    if (!userId || !token) {

        return;
    }


    fetch(
        contextPath +
        "/api/users/" +
        userId,
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

        if (!response.ok) {

            throw new Error(
                "Unable to load account information."
            );
        }

        return response.json();

    })

    .then(function (data) {

        console.log(
            "User Information:",
            data
        );


        setText(
            "accountName",
            data.fullName
        );


        setText(
            "accountEmail",
            data.email
        );


        setText(
            "accountStatus",
            data.status
        );


        var role = "USER";


        if (
            data.roles &&
            data.roles.length > 0
        ) {

            role =
                data.roles[0].roleName;
        }


        setText(
            "accountRole",
            role
        );


        setText(
            "sidebarName",
            data.fullName
        );


        setText(
            "sidebarRole",
            role
        );


        var firstLetter =
            data.fullName
                ? data.fullName
                    .charAt(0)
                    .toUpperCase()
                : "U";


        setText(
            "sidebarAvatar",
            firstLetter
        );

    })

    .catch(function (error) {

        console.error(
            "User Information Error:",
            error
        );

    });
}


/*
 * =====================================================
 * LOAD SETTINGS
 * =====================================================
 */

function loadSettings() {

    var userId =
        getUserId();

    var token =
        getToken();


    if (!userId || !token) {

        return;
    }


    console.log(
        "Loading settings for user:",
        userId
    );


    fetch(
        contextPath +
        "/api/settings/" +
        userId,
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
            "Settings HTTP Status:",
            response.status
        );


        if (!response.ok) {

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


            throw new Error(
                "Settings API Error: " +
                response.status
            );
        }


        return response.json();

    })

    .then(function (data) {

        console.log(
            "Settings Data:",
            data
        );


        document
            .getElementById(
                "emailNotifications"
            )
            .checked =
                data.emailNotifications === true;


        document
            .getElementById(
                "taskNotifications"
            )
            .checked =
                data.taskNotifications === true;


        document
            .getElementById(
                "projectNotifications"
            )
            .checked =
                data.projectNotifications === true;


        document
            .getElementById(
                "darkMode"
            )
            .checked =
                data.darkMode === true;


        applyDarkMode(
            data.darkMode === true
        );

    })

    .catch(function (error) {

        console.error(
            "Settings Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });
}


/*
 * =====================================================
 * SAVE SETTINGS
 * =====================================================
 */

function saveSettings() {

    var userId =
        getUserId();

    var token =
        getToken();


    if (!userId || !token) {

        return;
    }


    var requestData = {

        emailNotifications:
            document
                .getElementById(
                    "emailNotifications"
                )
                .checked,


        taskNotifications:
            document
                .getElementById(
                    "taskNotifications"
                )
                .checked,


        projectNotifications:
            document
                .getElementById(
                    "projectNotifications"
                )
                .checked,


        darkMode:
            document
                .getElementById(
                    "darkMode"
                )
                .checked

    };


    console.log(
        "Saving settings:",
        requestData
    );


    fetch(
        contextPath +
        "/api/settings/" +
        userId,
        {

            method: "PUT",

            headers: {

                "Authorization":
                    "Bearer " + token,

                "Content-Type":
                    "application/json"

            },

            body:
                JSON.stringify(
                    requestData
                )

        }
    )

    .then(function (response) {

        console.log(
            "Save Settings Status:",
            response.status
        );


        if (!response.ok) {

            if (response.status === 401) {

                throw new Error(
                    "401 Unauthorized."
                );
            }


            if (response.status === 403) {

                throw new Error(
                    "403 Forbidden."
                );
            }


            return response
                .json()
                .then(function (errorData) {

                    throw new Error(
                        errorData.message ||
                        "Settings update failed."
                    );

                });

        }


        return response.json();

    })

    .then(function (data) {

        console.log(
            "Updated Settings:",
            data
        );


        localStorage.setItem(
            "darkMode",
            data.darkMode
        );


        applyDarkMode(
            data.darkMode === true
        );


        showMessage(
            "Settings saved successfully!",
            "success"
        );

    })

    .catch(function (error) {

        console.error(
            "Save Settings Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });
}


/*
 * =====================================================
 * DARK MODE
 * =====================================================
 */

function applyDarkMode(enabled) {

    if (enabled) {

        document.body.classList.add(
            "dark-mode"
        );

    } else {

        document.body.classList.remove(
            "dark-mode"
        );
    }
}


/*
 * =====================================================
 * TEXT HELPER
 * =====================================================
 */

function setText(
    elementId,
    value
) {

    var element =
        document.getElementById(
            elementId
        );


    if (!element) {

        return;
    }


    element.innerText =
        value === null ||
        value === undefined
            ? "-"
            : value;
}


/*
 * =====================================================
 * MESSAGE
 * =====================================================
 */

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
        "message " + type;


    setTimeout(
        function () {

            element.innerText =
                "";

            element.className =
                "message";

        },
        4000
    );
}


/*
 * =====================================================
 * LOGOUT
 * =====================================================
 */

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

    localStorage.removeItem(
        "darkMode"
    );


    window.location.href =
        contextPath +
        "/login.jsp";
}