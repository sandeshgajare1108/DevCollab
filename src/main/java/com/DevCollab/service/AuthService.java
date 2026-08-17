package com.DevCollab.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.sql.Timestamp;
import java.util.UUID;
import com.DevCollab.dto.ForgotPasswordRequest;
import com.DevCollab.dto.ResetPasswordRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevCollab.Repository.RoleRepository;
import com.DevCollab.Repository.UserRepository;
import com.DevCollab.dto.LoginRequest;
import com.DevCollab.dto.LoginResponse;
import com.DevCollab.dto.RegisterRequest;
import com.DevCollab.entity.RoleEntity;
import com.DevCollab.entity.UserEntity;
import com.DevCollab.security.JwtService;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JwtService jwtService;

	// =====================================================
	// LOGIN
	// =====================================================

	public LoginResponse login(LoginRequest request) {

		// Find user by email

		List<UserEntity> users = userRepository.findByEmail(request.getEmail());

		// User not found

		if (users == null || users.isEmpty()) {

			throw new RuntimeException("Invalid email or password");
		}

		UserEntity user = users.get(0);

		// Password check

		if (!user.getPassword().equals(request.getPassword())) {

			throw new RuntimeException("Invalid email or password");
		}

		// Get user roles

		Set<RoleEntity> roles = user.getRoles();

		// Convert RoleEntity to String

		List<String> roleNames = new ArrayList<String>();

		if (roles != null) {

			for (RoleEntity role : roles) {

				roleNames.add(role.getRoleName());
			}
		}

		// Generate JWT

		String token = jwtService.generateToken(user.getUserId(), user.getEmail(), roleNames);

		// Return response

		return new LoginResponse(token, user.getUserId(), user.getFullName(), user.getEmail(), user.getRoles());
	}

	// =====================================================
	// REGISTER
	// =====================================================

	public String register(RegisterRequest request) {

		String email = request.getEmail().trim().toLowerCase();

		String fullName = request.getFullName().trim();

		String mobile = request.getMobile().trim();

		String password = request.getPassword();

		// =================================================
		// CHECK EMAIL
		// =================================================

		List<UserEntity> existingUsers = userRepository.findByEmail(email);

		if (existingUsers != null && !existingUsers.isEmpty()) {

			throw new RuntimeException("Email already registered");
		}

		// =================================================
		// CREATE USER
		// =================================================

		UserEntity user = new UserEntity();

		user.setFullName(fullName);

		user.setEmail(email);

		user.setMobile(mobile);

		user.setPassword(password);

		// =================================================
		// DEFAULT STATUS
		// =================================================

		user.setStatus("ACTIVE");

		// =================================================
		// DEFAULT ROLE = DEVELOPER
		// =================================================

		Optional<RoleEntity> developerRole = roleRepository.findByRoleName("DEVELOPER");

		if (!developerRole.isPresent()) {

			throw new RuntimeException("DEVELOPER role not found");
		}

		Set<RoleEntity> roles = new HashSet<RoleEntity>();

		roles.add(developerRole.get());

		user.setRoles(roles);

		// =================================================
		// SAVE USER
		// =================================================

		userRepository.save(user);

		return "Registration successful";
	}

	public String forgotPassword(ForgotPasswordRequest request) {

		if (request == null || request.getEmail() == null || request.getEmail().trim().isEmpty()) {

			throw new RuntimeException("Email is required");
		}

		String email = request.getEmail().trim().toLowerCase();

		List<UserEntity> users = userRepository.findByEmail(email);

		if (users == null || users.isEmpty()) {

			throw new RuntimeException("No account found with this email");
		}

		UserEntity user = users.get(0);

		String resetToken = UUID.randomUUID().toString();

		long expiryMillis = System.currentTimeMillis() + (15 * 60 * 1000);

		Timestamp expiry = new Timestamp(expiryMillis);

		user.setResetToken(resetToken);

		user.setResetTokenExpiry(expiry);

		userRepository.save(user);

		/*
		 * DEVELOPMENT / TESTING ONLY
		 *
		 * Production मध्ये हा token email द्वारे पाठवायचा.
		 */

		return resetToken;
	}

//	Reset Password

	public String resetPassword(ResetPasswordRequest request) {

		if (request == null || request.getToken() == null || request.getToken().trim().isEmpty()) {

			throw new RuntimeException("Reset token is required");
		}

		if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {

			throw new RuntimeException("New password is required");
		}

		if (request.getNewPassword().length() < 6) {

			throw new RuntimeException("Password must contain at least 6 characters");
		}

		Optional<UserEntity> optionalUser = userRepository.findByResetToken(request.getToken().trim());

		if (!optionalUser.isPresent()) {

			throw new RuntimeException("Invalid reset token");
		}

		UserEntity user = optionalUser.get();

		Timestamp expiry = user.getResetTokenExpiry();

		if (expiry == null) {

			throw new RuntimeException("Reset token expiry is missing");
		}

		if (expiry.getTime() < System.currentTimeMillis()) {

			throw new RuntimeException("Reset token has expired");
		}

		user.setPassword(request.getNewPassword());

		/*
		 * Token can be used only once.
		 */

		user.setResetToken(null);

		user.setResetTokenExpiry(null);

		userRepository.save(user);

		return "Password reset successfully";
	}
}