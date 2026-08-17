document
    .getElementById("loginForm")
    .addEventListener("submit", function(event) {

        event.preventDefault();

        const email =
            document.getElementById("email").value;

        const password =
            document.getElementById("password").value;

        const message =
            document.getElementById("message");


        message.innerHTML = "Logging in...";


        fetch("http://localhost:1401/api/auth/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({

                email: email,

                password: password

            })

        })

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Invalid email or password"
                );

            }

            return response.json();

        })

        .then(data => {

            console.log("LOGIN RESPONSE:");
            console.log(data);


            // JWT token save करा
            localStorage.setItem(
                "token",
                data.token
            );


            // User information save करा

            localStorage.setItem(
                "userId",
                data.userId
            );

            localStorage.setItem(
                "fullName",
                data.fullName
            );

            localStorage.setItem(
                "email",
                data.email
            );


            if (data.roles && data.roles.length > 0) {

                localStorage.setItem(
                    "role",
                    data.roles[0].roleName
                );

            }


            message.style.color = "green";

            message.innerHTML =
                "Login successful!";


            // Dashboard ला redirect

            setTimeout(function() {

                window.location.href =
                    "dashboard.jsp";

            }, 1000);

        })

        .catch(error => {

            console.error(error);

            message.style.color = "red";

            message.innerHTML =
                error.message;

        });

    });