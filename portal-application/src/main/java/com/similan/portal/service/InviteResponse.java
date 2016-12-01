package com.similan.portal.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Response class for the invite methods in the {@link MemberService}.
 * @author pablo
 */
public class InviteResponse {

	private List<String> successful = new ArrayList<String>();

	private List<String> failed = new ArrayList<String>();

	/**
	 * @return the successful
	 */
	public List<String> getSuccessful() {
		return successful;
	}

	/**
	 * @param successful the successful to set
	 */
	public void setSuccessful(List<String> successful) {
		this.successful = successful;
	}

	/**
	 * @return the failed
	 */
	public List<String> getFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(List<String> failed) {
		this.failed = failed;
	}

}
