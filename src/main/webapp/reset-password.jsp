<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DevCollab - Reset Password</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="data:,">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/reset-password.css">
</head>
<body>
	<div class="auth-container">
		<div class="auth-card">
			<div class="logo">
				<span>Dev</span>Collab
			</div>
			<h1>Reset Password</h1>
			<p class="subtitle">Enter your reset token and create a new
				password.</p>
			<div id="message" class="message" style="display: none;"></div>
			<form id="resetPasswordForm">
				<div class="form-group">
					<label for="token"> Reset Token </label>
					<textarea id="token" placeholder="Paste reset token" required> </textarea>
				</div>
				<div class="form-group">
					<label for="newPassword"> New Password </label> <input
						type="password" id="newPassword" minlength="6"
						placeholder="Enter new password" required>
				</div>
				<div class="form-group">
					<label for="confirmPassword"> Confirm Password </label> <input
						type="password" id="confirmPassword" minlength="6"
						placeholder="Confirm new password" required>
				</div>
				<button type="submit" id="resetButton">Reset Password</button>
			</form>
			<div class="back-link">
				<a href="${pageContext.request.contextPath}/login.jsp"> ← Back
					to Login </a>
			</div>
		</div>
	</div>
	<script>
		var contextPath = "${pageContext.request.contextPath}";
	</script>
	<script src="${pageContext.request.contextPath}/js/reset-password.js">
		
	</script>
</body>
</html>