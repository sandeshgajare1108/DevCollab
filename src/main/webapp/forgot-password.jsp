<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DevCollab - Forgot Password</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/forgot-password.css">
</head>
<body>
	<div class="auth-container">
		<div class="auth-card">
			<div class="logo">
				<span>Dev</span>Collab
			</div>
			<h1>Forgot Password?</h1>
			<p class="subtitle">Enter your registered email address to reset
				your password.</p>
			<div id="message" class="message" style="display: none;"></div>
			<form id="forgotPasswordForm">
				<div class="form-group">
					<label for="email"> Email Address </label> <input type="email"
						id="email" placeholder="Enter registered email" required>
				</div>
				<button type="submit" id="forgotButton">Generate Reset
					Token</button>
			</form>
			<div id="tokenBox" class="token-box" style="display: none;">
				<p>Development reset token:</p>
				<textarea id="resetToken" readonly> </textarea>
				<button type="button" onclick="goToResetPassword()">
					Continue to Reset Password</button>
			</div>
			<div class="back-link">
				<a href="${pageContext.request.contextPath}/login.jsp"> ← Back
					to Login </a>
			</div>
		</div>
	</div>
	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>
	<script src="${pageContext.request.contextPath}/js/forgot-password.js">
		
	</script>
</body>
</html>