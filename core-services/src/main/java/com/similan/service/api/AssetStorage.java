package com.similan.service.api;

import org.primefaces.model.UploadedFile;

import com.similan.framework.dto.AssetInfo;

/**
 * Interface that abstracts how assets (like photos) are stored.
 * <p>
 * Different implementation might use different mechanism, such
 * as file system or S3. 
 * @author psaavedra
 */
public interface AssetStorage {

	/**
	 * Stores an asset that is obtained from any binary stream.
	 *
	 * @param asset
	 *            The input stream that contains the data of the asset.
	 * @param contentType
	 *            The content type must be provided, as it cannot be guessed
	 *            from the stream.
	 * @return A key that can be used to retrieve the asset.
	 */
	String storeTemporary(String ownerType, String id, UploadedFile file);

	/**
	 * Makes a temporary assert persistent. A persistent asset will survive
	 * purges.
	 * @param key The key to persist.
	 * @return A new key to the definitive asset.
	 */
	String makePersistent(String key);

	/**
	 * Loads an asset from the persistent storage.
	 *
	 * @param key
	 *            The key obtained when the asset was originally stored.
	 * @param temporary
	 * 			  Whether the key represents a temporary resource.
	 * @return An URL to the location of the asset.
	 */
	AssetInfo loadAsset(String key, boolean temporary);

	/**
	 * Purges temporary assets that have been created for more than X hours.
	 * @param hours The hours an asset has to have lived to be purged. 
	 * @return The number of assets that were deleted.
	 */
	int purgeOlderThan(int hours);

	/**
	 * Deletes the asset identified by the given key.
	 * @param key The key for the asset.
	 */
	void delete(String key);
	
	/**
	 * @param ownerType
	 * @param id
	 * @param urlSource
	 * @return
	 */
	public String importResource(final String ownerType, final String id,
			final String urlSource);

}
