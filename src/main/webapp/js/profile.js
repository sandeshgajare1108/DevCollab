document.addEventListener("DOMContentLoaded", function () {

    console.log("Profile JS Loaded");

    loadProfile();

});


/*
=========================================================
GET USER ID
=========================================================
*/

function getUserId() {

    const userId =
        localStorage.getItem("userId");

    if (!userId) {

        console.error("User ID not found");

        showMessage(
            "User ID not found. Please login again.",
            "error"
        );

        return null;
    }

    return userId;
}


/*
=========================================================
GET JWT TOKEN
=========================================================
*/

function getToken() {

    const token =
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
=========================================================
GET ROLE FROM RESPONSE
=========================================================
*/

function getRole(data) {

    if (
        data.roles &&
        Array.isArray(data.roles) &&
        data.roles.length > 0
    ) {

        return data.roles
            .map(function (role) {

                return role.roleName;

            })
            .join(", ");
    }

    return "USER";
}


/*
=========================================================
LOAD PROFILE
=========================================================
*/

function loadProfile() {

    const userId =
        getUserId();

    if (!userId) {
        return;
    }


    const token =
        getToken();

    if (!token) {
        return;
    }


    console.log(
        "Loading profile for user:",
        userId
    );


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

        console.log(
            "Profile HTTP Status:",
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


        if (response.status === 404) {

            throw new Error(
                "User profile not found."
            );
        }


        if (!response.ok) {

            throw new Error(
                "Profile API Error: " +
                response.status
            );
        }


        return response.json();

    })


    .then(function (data) {

        console.log(
            "Profile Data:",
            data
        );


        setProfileData(data);

    })


    .catch(function (error) {

        console.error(
            "Profile Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });

}


/*
=========================================================
SET PROFILE DATA
=========================================================
*/

function setProfileData(data) {

    const role =
        getRole(data);


    /*
    USER ID
    */

    setValue(
        "userId",
        data.userId
    );


    /*
    FULL NAME
    */

    setValue(
        "fullName",
        data.fullName
    );


    /*
    EMAIL
    */

    setValue(
        "email",
        data.email
    );


    /*
    MOBILE
    */

    setValue(
        "mobile",
        data.mobile
    );


    /*
    STATUS
    */

    setValue(
        "status",
        data.status
    );


    /*
    ROLE
    */

    setValue(
        "role",
        role
    );


    /*
    CREATED
    */

    setValue(
        "createdAt",
        formatDate(data.createdAt)
    );


    /*
    UPDATED
    */

    setValue(
        "updatedAt",
        formatDate(data.updatedAt)
    );


    /*
    HEADER
    */

    setText(
        "displayName",
        data.fullName
    );


    setText(
        "displayRole",
        role
    );


    /*
    SIDEBAR
    */

    setText(
        "sidebarName",
        data.fullName
    );


    setText(
        "sidebarRole",
        role
    );


    /*
    AVATAR
    */

    const firstLetter =
        data.fullName
            ? data.fullName
                .charAt(0)
                .toUpperCase()
            : "U";


    setText(
        "profileAvatar",
        firstLetter
    );


    setText(
        "sidebarAvatar",
        firstLetter
    );

}


/*
=========================================================
UPDATE PROFILE FORM
=========================================================
*/

document
    .getElementById("profileForm")
    .addEventListener(
        "submit",
        function (event) {

            event.preventDefault();

            updateProfile();

        }
    );


/*
=========================================================
UPDATE PROFILE
=========================================================
*/

function updateProfile() {

    const userId =
        getUserId();

    if (!userId) {
        return;
    }


    const token =
        getToken();

    if (!token) {
        return;
    }


    const fullName =
        document
            .getElementById("fullName")
            .value
            .trim();


    const email =
        document
            .getElementById("email")
            .value
            .trim();


    const mobile =
        document
            .getElementById("mobile")
            .value
            .trim();


    /*
    VALIDATION
    */

    if (!fullName) {

        showMessage(
            "Full name is required.",
            "error"
        );

        return;
    }


    if (!email) {

        showMessage(
            "Email is required.",
            "error"
        );

        return;
    }


    /*
    REQUEST BODY
    */

    const requestData = {

        fullName: fullName,

        email: email,

        mobile: mobile

    };


    console.log(
        "Updating profile:",
        requestData
    );


    fetch(
        contextPath +
        "/api/users/" +
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
                JSON.stringify(requestData)

        }
    )


    .then(function (response) {

        console.log(
            "Update HTTP Status:",
            response.status
        );


        if (response.status === 401) {

            throw new Error(
                "401 Unauthorized."
            );
        }


        if (response.status === 403) {

            throw new Error(
                "403 Forbidden. You don't have permission."
            );
        }


        if (response.status === 404) {

            throw new Error(
                "User not found."
            );
        }


        if (!response.ok) {

            throw new Error(
                "Profile update failed. HTTP " +
                response.status
            );
        }


        return response.json();

    })


    .then(function (data) {

        console.log(
            "Updated Profile:",
            data
        );


        /*
        UPDATE LOCAL STORAGE
        */

        if (data.fullName) {

            localStorage.setItem(
                "fullName",
                data.fullName
            );
        }


        if (data.email) {

            localStorage.setItem(
                "email",
                data.email
            );
        }


        /*
        UPDATE UI
        */

        setProfileData(data);


        showMessage(
            "Profile updated successfully!",
            "success"
        );

    })


    .catch(function (error) {

        console.error(
            "Update Profile Error:",
            error
        );


        showMessage(
            error.message,
            "error"
        );

    });

}


/*
=========================================================
SET INPUT VALUE
=========================================================
*/

function setValue(
    elementId,
    value
) {

    const element =
        document.getElementById(
            elementId
        );


    if (!element) {
        return;
    }


    element.value =
        value === null ||
        value === undefined
            ? ""
            : value;

}


/*
=========================================================
SET TEXT
=========================================================
*/

function setText(
    elementId,
    value
) {

    const element =
        document.getElementById(
            elementId
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


/*
=========================================================
FORMAT DATE
=========================================================
*/

function formatDate(value) {

    if (!value) {
        return "-";
    }


    const date =
        new Date(value);


    if (isNaN(date.getTime())) {

        return value;
    }


    return date.toLocaleString();

}


/*
=========================================================
MESSAGE
=========================================================
*/

function showMessage(
    message,
    type
) {

    const element =
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

            element.innerText = "";

            element.className =
                "message";

        },
        4000
    );

}


/*
=========================================================
LOGOUT
=========================================================
*/

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