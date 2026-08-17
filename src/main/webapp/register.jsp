<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>DevCollab - Registration</title>

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <link rel="icon" href="data:,">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/register.css">

</head>

<body>

<div class="page-container">

    <!-- ================= LEFT PANEL ================= -->

    <div class="left-panel">

        <div class="brand">

            <h1>
                <span>Dev</span>Collab
            </h1>

            <p>
                Developer Collaboration Platform
            </p>

        </div>


        <div class="left-content">

            <h2>
                Build. Collaborate. Deliver.
            </h2>

            <p>
                Manage projects, tasks, GitHub pull requests,
                code reviews, testing and team collaboration
                from one platform.
            </p>


            <div class="feature-list">

                <div class="feature-item">
                    <span>✓</span>
                    Project Management
                </div>

                <div class="feature-item">
                    <span>✓</span>
                    Task Management
                </div>

                <div class="feature-item">
                    <span>✓</span>
                    GitHub Integration
                </div>

                <div class="feature-item">
                    <span>✓</span>
                    Team Collaboration
                </div>

            </div>

        </div>

    </div>


    <!-- ================= RIGHT PANEL ================= -->

    <div class="right-panel">

        <div class="register-card">

            <div class="register-header">

                <h2>
                    Create Account
                </h2>

                <p>
                    Join DevCollab and start collaborating.
                </p>

            </div>


            <!-- MESSAGE -->

            <div
                id="message"
                class="message"
                style="display:none;">
            </div>


            <!-- REGISTRATION FORM -->

            <form id="registerForm">


                <!-- FULL NAME -->

                <div class="form-group">

                    <label for="fullName">
                        Full Name
                    </label>

                    <input
                        type="text"
                        id="fullName"
                        name="fullName"
                        maxlength="100"
                        placeholder="Enter your full name"
                        autocomplete="name"
                        required>

                </div>


                <!-- EMAIL -->

                <div class="form-group">

                    <label for="email">
                        Email Address
                    </label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        maxlength="100"
                        placeholder="Enter your email"
                        autocomplete="email"
                        required>

                </div>


                <!-- MOBILE -->

                <div class="form-group">

                    <label for="mobile">
                        Mobile Number
                    </label>

                    <input
                        type="tel"
                        id="mobile"
                        name="mobile"
                        maxlength="15"
                        placeholder="Enter mobile number"
                        autocomplete="tel"
                        required>

                </div>


                <!-- PASSWORD -->

                <div class="form-group">

                    <label for="password">
                        Password
                    </label>

                    <div class="password-wrapper">

                        <input
                            type="password"
                            id="password"
                            name="password"
                            maxlength="255"
                            placeholder="Create a password"
                            autocomplete="new-password"
                            required>

                        <button
                            type="button"
                            class="show-password"
                            onclick="togglePassword('password', this)">

                            Show

                        </button>

                    </div>

                </div>


                <!-- CONFIRM PASSWORD -->

                <div class="form-group">

                    <label for="confirmPassword">
                        Confirm Password
                    </label>

                    <div class="password-wrapper">

                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            maxlength="255"
                            placeholder="Confirm your password"
                            autocomplete="new-password"
                            required>

                        <button
                            type="button"
                            class="show-password"
                            onclick="togglePassword('confirmPassword', this)">

                            Show

                        </button>

                    </div>

                </div>


                <!-- DEFAULT ROLE -->

                <div class="role-info">

                    <strong>Default Role:</strong>

                    <span>
                        DEVELOPER
                    </span>

                </div>


                <!-- TERMS -->

                <div class="checkbox-group">

                    <input
                        type="checkbox"
                        id="terms"
                        required>

                    <label for="terms">

                        I agree to the DevCollab
                        terms and conditions.

                    </label>

                </div>


                <!-- SUBMIT -->

                <button
                    type="submit"
                    id="registerButton"
                    class="register-btn">

                    Create Account

                </button>

            </form>


            <!-- LOGIN -->

            <div class="login-link">

                Already have an account?

                <a
                    href="${pageContext.request.contextPath}/login.jsp">

                    Login

                </a>

            </div>

        </div>

    </div>

</div>


<script>

    var contextPath =
        "${pageContext.request.contextPath}";

</script>


<script
    src="${pageContext.request.contextPath}/js/register.js">
</script>

</body>

</html>