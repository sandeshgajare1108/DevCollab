
package com.DevCollab.cntrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DevCollab.dto.LoginRequest;
import com.DevCollab.dto.LoginResponse;
import com.DevCollab.dto.RegisterRequest;
import com.DevCollab.service.AuthService;
import com.DevCollab.dto.ForgotPasswordRequest;
import com.DevCollab.dto.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private AuthService authService;

	// =====================================================
	// LOGIN
	// =====================================================

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request) {

		return authService.login(request);
	}

	// =====================================================
	// REGISTER
	// =====================================================

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

		try {

			if (request == null) {

				return ResponseEntity.badRequest().body("Registration data is required");
			}

			if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Full name is required");
			}

			if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Email is required");
			}

			if (request.getMobile() == null || request.getMobile().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Mobile number is required");
			}

			if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {

				return ResponseEntity.badRequest().body("Password is required");
			}

			String message = authService.register(request);

			return ResponseEntity.status(HttpStatus.CREATED).body(message);

		} catch (RuntimeException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
		}
	}
	@PostMapping("/forgot-password")

	public ResponseEntity<?> forgotPassword(
	        @RequestBody ForgotPasswordRequest request) {

	    try {

	        String resetToken =
	                authService.forgotPassword(
	                        request
	                );

	        /*
	         * DEVELOPMENT TEST RESPONSE.
	         *
	         * Production मध्ये token return करू नये.
	         */

	        return ResponseEntity.ok(
	                java.util.Collections.singletonMap(
	                        "resetToken",
	                        resetToken
	                )
	        );

	    } catch (RuntimeException e) {

	        return ResponseEntity
	                .badRequest()
	                .body(
	                    e.getMessage()
	                );

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .status(
	                    HttpStatus.INTERNAL_SERVER_ERROR
	                )
	                .body(
	                    "Forgot password failed"
	                );
	    }
	}
	//Reset Password API
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
	        @RequestBody ResetPasswordRequest request) {

	    try {

	        String message =
	                authService.resetPassword(
	                        request
	                );

	        return ResponseEntity.ok(
	                java.util.Collections.singletonMap(
	                        "message",
	                        message
	                )
	        );

	    } catch (RuntimeException e) {

	        return ResponseEntity
	                .badRequest()
	                .body(
	                    e.getMessage()
	                );

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .status(
	                    HttpStatus.INTERNAL_SERVER_ERROR
	                )
	                .body(
	                    "Password reset failed"
	                );
	    }
	}
}
