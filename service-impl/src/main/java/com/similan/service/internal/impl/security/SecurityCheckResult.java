package com.similan.service.internal.impl.security;


public class SecurityCheckResult {
	private boolean allowed;
	private String message;

	public SecurityCheckResult(boolean allowed, String message) {
		this.allowed = allowed;
		this.message = message;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public String getMessage() {
		return message;
	}
}