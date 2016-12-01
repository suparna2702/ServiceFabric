package com.similan.portal.view.handler;

/**
 * Represents an image deletion.
 * @author psaavedra
 */
public interface ImageDeletion {

	/**
	 * Returns the current image key.
	 *
	 * @return The current image key, never null.
	 */
	String getCurrentKey();

	/**
	 * Updates the underlying entity.
	 * @throws Exception 
	 */
	void update() throws Exception;
}
