
console.log("=================================");
console.log("DevCollab Reset Password JS Loaded");
console.log("=================================");


// =====================================================
// DOM READY
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        const form =
            document.getElementById(
                "resetPasswordForm"
            );


        if (!form) {

            console.error(
                "Reset password form not found"
            );

            return;
        }


        // ---------------------------------------------
        // Load token generated on forgot-password page
        // ---------------------------------------------

        const savedToken =
            sessionStorage.getItem(
                "resetToken"
            );


        if (savedToken) {

            const tokenInput =
                document.getElementById(
                    "token"
                );


            if (tokenInput) {

                tokenInput.value =
                    savedToken;
            }
        }


        // ---------------------------------------------
        // Submit listener
        // ---------------------------------------------

        form.addEventListener(
            "submit",
            handleResetPassword
        );

    }
);


// =====================================================
// HANDLE RESET PASSWORD
// IMPORTANT: async + function MUST be together
// =====================================================

async function handleResetPassword(
    event
) {

    event.preventDefault();


    const button =
        document.getElementById(
            "resetButton"
        );


    const tokenInput =
        document.getElementById(
            "token"
        );


    const newPasswordInput =
        document.getElementById(
            "newPassword"
        );


    const confirmPasswordInput =
        document.getElementById(
            "confirmPassword"
        );


    if (!button ||
        !tokenInput ||
        !newPasswordInput ||
        !confirmPasswordInput) {

        console.error(
            "Reset password form elements missing"
        );

        return;
    }


    const token =
        tokenInput.value.trim();


    const newPassword =
        newPasswordInput.value;


    const confirmPassword =
        confirmPasswordInput.value;


    hideMessage();


    // =====================================================
    // VALIDATION
    // =====================================================

    if (!token) {

        showMessage(
            "Reset token is required.",
            "error"
        );

        return;
    }


    if (!newPassword) {

        showMessage(
            "New password is required.",
            "error"
        );

        return;
    }


    if (newPassword.length < 6) {

        showMessage(
            "Password must contain at least 6 characters.",
            "error"
        );

        return;
    }


    if (!confirmPassword) {

        showMessage(
            "Please confirm your password.",
            "error"
        );

        return;
    }


    if (
        newPassword !==
        confirmPassword
    ) {

        showMessage(
            "Passwords do not match.",
            "error"
        );

        return;
    }


    // =====================================================
    // DISABLE BUTTON
    // =====================================================

    button.disabled =
        true;

    button.innerText =
        "Resetting...";


    try {

        console.log(
            "Reset password request started"
        );


        // =================================================
        // API REQUEST
        // =================================================

        const response =
            await fetch(
                contextPath +
                "/api/auth/reset-password",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json",

                        "Accept":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            token:
                                token,

                            newPassword:
                                newPassword
                        })
                }
            );


        console.log(
            "Reset Password HTTP Status:",
            response.status
        );


        // =================================================
        // READ RESPONSE
        // =================================================

        const text =
            await response.text();


        console.log(
            "Reset Password Response:",
            text
        );


        let data =
            null;


        if (text) {

            try {

                data =
                    JSON.parse(
                        text
                    );

            } catch (jsonError) {

                data =
                    text;
            }
        }


        // =================================================
        // ERROR
        // =================================================

        if (!response.ok) {

            throw new Error(
                extractErrorMessage(
                    data
                )
            );
        }


        // =================================================
        // SUCCESS
        // =================================================

        showMessage(
            extractSuccessMessage(
                data
            ),
            "success"
        );


        // Remove used token
        sessionStorage.removeItem(
            "resetToken"
        );


        // Clear form

        document
            .getElementById(
                "resetPasswordForm"
            )
            .reset();


        // =================================================
        // REDIRECT TO LOGIN
        // =================================================

        setTimeout(
            function () {

                window.location.href =
                    contextPath +
                    "/login.jsp";

            },
            1800
        );


    } catch (error) {

        console.error(
            "Reset Password Error:",
            error
        );


        showMessage(
            error.message ||
            "Password reset failed.",
            "error"
        );


    } finally {

        button.disabled =
            false;

        button.innerText =
            "Reset Password";

    }
}


// =====================================================
// SUCCESS MESSAGE
// =====================================================

function extractSuccessMessage(
    data
) {

    if (!data) {

        return (
            "Password reset successfully."
        );
    }


    if (
        typeof data ===
        "string"
    ) {

        return data;
    }


    return (
        data.message ||
        data.successMessage ||
        data.response ||
        "Password reset successfully."
    );
}


// =====================================================
// ERROR MESSAGE
// =====================================================

function extractErrorMessage(
    data
) {

    if (!data) {

        return (
            "Password reset failed."
        );
    }


    if (
        typeof data ===
        "string"
    ) {

        return data;
    }


    if (data.message) {

        return data.message;
    }


    if (data.error) {

        return data.error;
    }


    if (data.errors) {

        if (
            Array.isArray(
                data.errors
            )
        ) {

            return data.errors.join(
                ", "
            );
        }


        return JSON.stringify(
            data.errors
        );
    }


    return (
        "Password reset failed."
    );
}


// =====================================================
// SHOW MESSAGE
// =====================================================

function showMessage(
    message,
    type
) {

    const element =
        document.getElementById(
            "message"
        );


    if (!element) {

        alert(
            message
        );

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


    element.scrollIntoView({
        behavior: "smooth",
        block: "nearest"
    });
}


// =====================================================
// HIDE MESSAGE
// =====================================================

function hideMessage() {

    const element =
        document.getElementById(
            "message"
        );


    if (!element) {
        return;
    }


    element.innerText =
        "";


    element.style.display =
        "none";
}
