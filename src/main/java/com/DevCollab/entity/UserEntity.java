package com.DevCollab.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "status")
	private String status;

	@Column(name = "created_at")
	private Timestamp createdAt;

	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Column(name = "reset_token")
	private String resetToken;
	@Column(name = "reset_token_expiry")
	private Timestamp resetTokenExpiry;

	// Many Users can have Many Roles
	@ManyToMany
	@JoinTable(name = "user_roles",

			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),

			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Set<RoleEntity> roles = new HashSet<>();

	// Default Constructor
	public UserEntity() {
		super();
	}

	// Getters and Setters

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	// toString
	@Override
	public String toString() {

		return "UserEntity [userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", password=" + password
				+ ", mobile=" + mobile + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", roles=" + roles + "]";
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Timestamp getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public void setResetTokenExpiry(Timestamp resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}
}