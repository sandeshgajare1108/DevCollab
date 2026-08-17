
console.log("=================================");
console.log("DevCollab Forgot Password JS Loaded");
console.log("=================================");


// =====================================================
// DOM READY
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        const form =
            document.getElementById(
                "forgotPasswordForm"
            );


        if (!form) {

            console.error(
                "Forgot password form not found"
            );

            return;
        }


        form.addEventListener(
            "submit",
            handleForgotPassword
        );
    }
);


// =====================================================
// HANDLE FORGOT PASSWORD
// IMPORTANT: async + function SAME LINE
// =====================================================

async function handleForgotPassword(event) {

    event.preventDefault();


    const button =
        document.getElementById(
            "forgotButton"
        );


    const email =
        document
            .getElementById("email")
            .value
            .trim();


    hideMessage();


    // =================================================
    // VALIDATION
    // =================================================

    if (!email) {

        showMessage(
            "Email is required.",
            "error"
        );

        return;
    }


    button.disabled = true;

    button.innerText =
        "Generating...";


    try {

        console.log(
            "Forgot Password request started"
        );


        const response =
            await fetch(
                contextPath +
                "/api/auth/forgot-password",
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
                            email: email
                        })
                }
            );


        console.log(
            "Forgot Password HTTP Status:",
            response.status
        );


        const text =
            await response.text();


        console.log(
            "Forgot Password Response:",
            text
        );


        let data = null;


        if (text) {

            try {

                data =
                    JSON.parse(text);

            } catch (error) {

                data = text;
            }
        }


        // =================================================
        // ERROR
        // =================================================

        if (!response.ok) {

            throw new Error(
                extractErrorMessage(data)
            );
        }


        // =================================================
        // TOKEN
        // =================================================

        const resetToken =
            typeof data === "object"
                ? data.resetToken
                : null;


        if (!resetToken) {

            throw new Error(
                "Reset token was not returned by the server."
            );
        }


        // =================================================
        // SHOW TOKEN
        // =================================================

        const tokenBox =
            document.getElementById(
                "tokenBox"
            );


        const tokenField =
            document.getElementById(
                "resetToken"
            );


        if (tokenField) {

            tokenField.value =
                resetToken;
        }


        if (tokenBox) {

            tokenBox.style.display =
                "block";
        }


        // =================================================
        // SAVE TOKEN
        // =================================================

        sessionStorage.setItem(
            "resetToken",
            resetToken
        );


        showMessage(
            "Reset token generated successfully.",
            "success"
        );


    } catch (error) {

        console.error(
            "Forgot Password Error:",
            error
        );


        showMessage(
            error.message ||
            "Forgot password failed.",
            "error"
        );


    } finally {

        button.disabled = false;

        button.innerText =
            "Generate Reset Token";
    }
}


// =====================================================
// GO TO RESET PASSWORD
// =====================================================

function goToResetPassword() {

    window.location.href =
        contextPath +
        "/reset-password.jsp";
}


// =====================================================
// ERROR MESSAGE
// =====================================================

function extractErrorMessage(
    data
) {

    if (!data) {

        return "Forgot password failed.";
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


    return (
        "Forgot password failed."
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

        alert(message);

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
