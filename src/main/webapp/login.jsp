<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>DevCollab - Login</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: Arial, sans-serif;
}

body {
	min-height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
	background: linear-gradient(135deg, #667eea, #764ba2);
}

.forgot-link {
	display: block;
	text-align: right;
	margin-top: 8px;
	color: #4f46e5;
	text-decoration: none;
	font-size: 13px;
	font-weight: bold;
}

.login-container {
	width: 400px;
	background: white;
	padding: 40px;
	border-radius: 15px;
	box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}

.logo {
	text-align: center;
	font-size: 30px;
	font-weight: bold;
	margin-bottom: 10px;
}

.logo span {
	color: #667eea;
}

.subtitle {
	text-align: center;
	color: #777;
	margin-bottom: 30px;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: block;
	margin-bottom: 8px;
	font-weight: bold;
}

.form-group input {
	width: 100%;
	padding: 13px;
	border: 1px solid #ddd;
	border-radius: 8px;
	font-size: 15px;
}

.form-group input:focus {
	outline: none;
	border-color: #667eea;
}

.login-btn {
	width: 100%;
	padding: 14px;
	border: none;
	border-radius: 8px;
	background: #667eea;
	color: white;
	font-size: 16px;
	font-weight: bold;
	cursor: pointer;
}

.login-btn:hover {
	background: #5568d9;
}

.message {
	margin-top: 15px;
	text-align: center;
	font-weight: bold;
}

.register {
	text-align: center;
	margin-top: 20px;
}

.register a {
	color: #667eea;
	text-decoration: none;
	font-weight: bold;
}
</style>
<link rel="icon" href="data:,">
</head>

<body>

	<div class="login-container">

		<div class="logo">
			<span>Dev</span>Collab
		</div>

		<p class="subtitle">Login to your account</p>

		<form id="loginForm">

			<div class="form-group">

				<label>Email</label> <input type="email" id="email"
					placeholder="Enter your email" required>

			</div>


			<div class="form-group">

				<label>Password</label> <input type="password" id="password"
					placeholder="Enter your password" required>

			</div>


			<button type="submit" class="login-btn">Login</button>

		</form>


		<div id="message" class="message"></div>


		<div class="register">

			Don't have an account? <a href="register.jsp"> Register </a> <a
				href="${pageContext.request.contextPath}/forgot-password.jsp"
				class="forgot-link"> Forgot Password? </a>


		</div>

	</div>


	<script src="${pageContext.request.contextPath}/js/login.js"></script>

</body>
</html>