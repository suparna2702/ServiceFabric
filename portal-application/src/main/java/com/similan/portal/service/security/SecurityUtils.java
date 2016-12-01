package com.similan.portal.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.similan.portal.service.security.MemberUserDetailsService.PersistentUser;

/**
 * Utility class for accessing security context information.
 * 
 * @author psaavedra
 */
public class SecurityUtils {

	private SecurityUtils() {
	}

	/**
	 * Returns the currently logged user id, if any.
	 * 
	 * @return The current user id, or null if no user is currently logged in.
	 */
	public static Long getCurrentUserId() {
		PersistentUser user = getCurrentUser();
		if (user == null) {
			return null;
		}
		return user.getId();
	}

	/**
	 * Returns the currently logged user id, or throws an
	 * {@link IllegalArgumentException} if no user is logged in.
	 *
	 * @return The current user id, never null.
	 */
	public static Long getRequiredCurrentUserId() {
		Long userId = getCurrentUserId();
		if (userId == null) {
			throw new IllegalArgumentException(
					"No currently logged in user available");
		}
		return userId;
	}

	/**
	 * Returns the {@link PersistentUser} associated with the given
	 * authentication object, if any.
	 *
	 * @param auth
	 *            The authentication object.
	 * @return The associated persistent user, or null.
	 */
	public static PersistentUser fromAuthentication(Authentication auth) {
		if (auth == null) {
			return null;
		}
		Object credentials = auth.getPrincipal();
		if (!(credentials instanceof PersistentUser)) {
			return null;
		}
		return (PersistentUser) credentials;
	}

	/**
	 * Returns the current logged in user, based on the currently available
	 * {@link Authentication} object in the security context.
	 *
	 * @return The currently logged in user, or null if no user has logged in.
	 */
	public static PersistentUser getCurrentUser() {
		return fromAuthentication(SecurityContextHolder.getContext()
				.getAuthentication());
	}
}
