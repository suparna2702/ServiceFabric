package com.similan.framework.dto;

import java.net.URL;

/**
 * Data transfer object that contains the information on
 * where a resource is located.
 * @author pablo
 */
public class AssetInfo {

	private final URL location;

	private boolean canRedirect = false;

	private String contentType;

	private long size;

	private String fileName;

	public AssetInfo(final URL location) {
		this.location = location;
	}

	/**
	 * @return the canRedirect
	 */
	public boolean isCanRedirect() {
		return canRedirect;
	}

	/**
	 * @param canRedirect the canRedirect to set
	 */
	public void setCanRedirect(boolean canRedirect) {
		this.canRedirect = canRedirect;
	}

	/**
	 * @return the location
	 */
	public URL getLocation() {
		return location;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param l the size to set
	 */
	public void setSize(long l) {
		this.size = l;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

}
