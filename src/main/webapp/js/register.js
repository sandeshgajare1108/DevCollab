
console.log("=================================");
console.log("DevCollab Registration JS Loaded");
console.log("=================================");


// =====================================================
// DOM READY
// =====================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        const form =
            document.getElementById(
                "registerForm"
            );


        if (!form) {

            console.error(
                "Registration form not found"
            );

            return;
        }


        form.addEventListener(
            "submit",
            handleRegistration
        );

    }
);


// =====================================================
// HANDLE REGISTRATION
// IMPORTANT: async + function MUST be together
// =====================================================

async function handleRegistration(event) {

    event.preventDefault();


    const registerButton =
        document.getElementById(
            "registerButton"
        );


    const fullName =
        document.getElementById(
            "fullName"
        ).value.trim();


    const email =
        document.getElementById(
            "email"
        ).value.trim();


    const mobile =
        document.getElementById(
            "mobile"
        ).value.trim();


    const password =
        document.getElementById(
            "password"
        ).value;


    const confirmPassword =
        document.getElementById(
            "confirmPassword"
        ).value;


    const terms =
        document.getElementById(
            "terms"
        ).checked;


    clearMessage();


    // =====================================================
    // VALIDATION
    // =====================================================

    if (!fullName) {

        showMessage(
            "Full name is required.",
            "error"
        );

        return;
    }


    if (fullName.length < 3) {

        showMessage(
            "Full name must contain at least 3 characters.",
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


    if (!isValidEmail(email)) {

        showMessage(
            "Please enter a valid email address.",
            "error"
        );

        return;
    }


    if (!mobile) {

        showMessage(
            "Mobile number is required.",
            "error"
        );

        return;
    }


    if (!isValidMobile(mobile)) {

        showMessage(
            "Please enter a valid mobile number.",
            "error"
        );

        return;
    }


    if (!password) {

        showMessage(
            "Password is required.",
            "error"
        );

        return;
    }


    if (password.length < 6) {

        showMessage(
            "Password must contain at least 6 characters.",
            "error"
        );

        return;
    }


    if (password !== confirmPassword) {

        showMessage(
            "Passwords do not match.",
            "error"
        );

        return;
    }


    if (!terms) {

        showMessage(
            "Please accept the terms and conditions.",
            "error"
        );

        return;
    }


    // =====================================================
    // BUTTON
    // =====================================================

    registerButton.disabled = true;

    registerButton.innerText =
        "Creating Account...";


    try {

        console.log(
            "Registration request started"
        );


        // =================================================
        // REGISTER API
        // =================================================

        const response = await fetch(
            contextPath +
            "/api/auth/register",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json",

                    "Accept":
                        "application/json"
                },

                body: JSON.stringify({

                    fullName:
                        fullName,

                    email:
                        email,

                    mobile:
                        mobile,

                    password:
                        password
                })
            }
        );


        console.log(
            "Registration HTTP Status:",
            response.status
        );


        // =================================================
        // RESPONSE
        // =================================================

        const text =
            await response.text();


        console.log(
            "Registration Response:",
            text
        );


        let data = null;


        if (text) {

            try {

                data =
                    JSON.parse(
                        text
                    );

            } catch (jsonError) {

                data = text;
            }
        }


        // =================================================
        // ERROR RESPONSE
        // =================================================

        if (!response.ok) {

            const errorMessage =
                extractErrorMessage(
                    data
                );

            throw new Error(
                errorMessage
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


        document
            .getElementById(
                "registerForm"
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
            "Registration Error:",
            error
        );


        showMessage(
            error.message ||
            "Registration failed.",
            "error"
        );


    } finally {

        registerButton.disabled =
            false;

        registerButton.innerText =
            "Create Account";

    }
}


// =====================================================
// EMAIL VALIDATION
// =====================================================

function isValidEmail(
    email
) {

    const pattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return pattern.test(
        email
    );
}


// =====================================================
// MOBILE VALIDATION
// =====================================================

function isValidMobile(
    mobile
) {

    /*
     * Allows 10 to 15 digits.
     */

    const pattern =
        /^[0-9]{10,15}$/;

    return pattern.test(
        mobile
    );
}


// =====================================================
// PASSWORD SHOW / HIDE
// =====================================================

function togglePassword(
    inputId,
    button
) {

    const input =
        document.getElementById(
            inputId
        );


    if (!input) {
        return;
    }


    if (
        input.type ===
        "password"
    ) {

        input.type =
            "text";

        button.innerText =
            "Hide";

    } else {

        input.type =
            "password";

        button.innerText =
            "Show";
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
            "Registration successful."
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
        "Registration successful."
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
            "Registration failed."
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
        "Registration failed."
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
// CLEAR MESSAGE
// =====================================================

function clearMessage() {

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
