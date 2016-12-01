package com.similan.portal.view.handler;

import org.primefaces.model.UploadedFile;

/**
 * Interface for communicating file uploads.
 * @author pablo
 */
public interface ImageUpload {

	String getType();

	UploadedFile getFile();

	void setImageKey(String key);

	void update() throws Exception;

	String getId();

	String currentKey();
}
