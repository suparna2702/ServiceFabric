package com.similan.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;

import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.AssetInfo;
import com.similan.service.api.AssetStorage;

/**
 * {@link AssetStorage} implementation that uses the file system
 * to store assets,
 * @author pablo
 */
@Slf4j
public class FileSystemAssetStorage implements AssetStorage {

	private static final String TEMP_PREFIX = "/temp";

	/**
	 * Logger.
	 */

	/**
	 * Configuration.
	 */
	private PlatformCommonSettings platformCommonSettings;

	public String storeTemporary(final String ownerType, final String id,
			final UploadedFile file) {
		log.info("Storing temporary asset for " + ownerType + " with id " + id
				+ " (" + file.getFileName() + ")"); 
		StringBuilder basedir = new StringBuilder(platformCommonSettings
				.getRootImageDirectory().getValue()).append(TEMP_PREFIX);
		log.debug("Temporary storage path is " + basedir);
		StringBuilder key = new StringBuilder("/").append(ownerType)
				.append("/").append(id).append("/");
		File dest = new File(basedir.append(key).toString());
		log.debug("Destination path is " + dest);
		//TODO Handle problems when making the directory.
		if (!dest.mkdirs()) {
			log.warn("Could not create directory structure for " + dest);
		}
		File outputFile = new File(dest, file.getFileName());
		FileOutputStream out = null;
		InputStream asset = null;
		try {
			log.debug("Copying files from source to destination (" + outputFile + ")");
			asset = file.getInputstream();
			out = new FileOutputStream(outputFile);
			int bytes = IOUtils.copy(asset, out);
			log.info("Successfully stored " + bytes + " bytes of data");
		} catch (IOException e) {
			log.error("Error storing uploaded file " + file.getFileName()
					+ " to final destinatio " + dest, e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(asset);
			IOUtils.closeQuietly(out);
		}
		key.append(file.getFileName());
		log.info("Successfully stored asset with key " + key);
		return key.toString();
	}
	
	public String importResource(final String ownerType, final String id,
			final String urlSource) {
		FileOutputStream fos = null;
		ReadableByteChannel rbc = null;
		try {

			log.info("Importing temporary asset for " + ownerType + " with id "
					+ id + " (" + urlSource + ")");
			StringBuilder basedir = new StringBuilder(platformCommonSettings
					.getRootImageDirectory().getValue()).append(TEMP_PREFIX);
			log.debug("Temporary storage path is " + basedir);
			StringBuilder key = new StringBuilder("/").append(ownerType)
					.append("/").append(id).append("/");
			File dest = new File(basedir.append(key).toString());
			log.debug("Destination path is " + dest);
			// TODO Handle problems when making the directory.
			if (!dest.mkdirs()) {
				log.warn("Could not create directory structure for " + dest);
			}

			URL website = new URL(urlSource);
			rbc = Channels.newChannel(website.openStream());

			File outputFile = new File(dest, FilenameUtils.getBaseName(website.toString()));

			fos = new FileOutputStream(outputFile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

			key.append(outputFile.getName());
			log.info("Successfully stored asset with key " + key);
			return key.toString();
		} catch (Exception e) {
			log.error("Error loading URL for file " + urlSource, e);
			return null;
		} finally {
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(rbc);
			
		}
	}

	public String makePersistent(final String key) {
		log.info("Storing asset with key " + key + " definitively");
		StringBuilder basedir = getBaseDir();
		StringBuilder tempPath = new StringBuilder(basedir).append(TEMP_PREFIX)
				.append(key);
		basedir.append(key);
		File temp = new File(tempPath.toString());
		File dest = new File(basedir.toString());
		dest.getParentFile().mkdirs();
		log.debug("Copying file from " + temp + " to " + dest);
		temp.renameTo(dest);
		log.info("Successfully stored " + key);
		return key;
	}

	public AssetInfo loadAsset(String key, boolean temporary) {
		log.info("Loading asset " + key + "(temporary=" + temporary + ")");
		StringBuilder basedir = getBaseDir();
		if (temporary) {
			basedir.append(TEMP_PREFIX);
		}
		basedir.append(key);
		File asset = new File(basedir.toString());
		log.debug("Loading resource from " + asset);
		if (!asset.exists()) {
			log.error("File " + asset + " does not exist in the filesystem");
		}
		try {
			AssetInfo assetInfo = new AssetInfo(asset.toURI().toURL());
			assetInfo.setSize(asset.length());
			assetInfo.setFileName(asset.getName());
			return assetInfo;
		} catch (MalformedURLException e) {
			log.error("Error loading URL for file " + asset, e);
			return null;
		}
	}

	/**
	 * @return
	 */
	private StringBuilder getBaseDir() {
		StringBuilder basedir = new StringBuilder(platformCommonSettings
				.getRootImageDirectory().getValue());
		return basedir;
	}

	/* (non-Javadoc)
	 * @see com.similan.portal.service.storage.AssetStorage#purgeOlderThan(int)
	 */
	public int purgeOlderThan(int hours) {
		log.info("Purging temporary assets older than " + hours + " hours");
		//Obtain N hours before now. Anything previous to that will be deleted.
		StringBuilder basedir = new StringBuilder(platformCommonSettings
				.getRootImageDirectory().getValue()).append(TEMP_PREFIX);
		File base = new File(basedir.toString());
		if (!base.isDirectory()) {
			log.error("File " + base + " is not a directory, aborting...");
			return 0;
		}
		long maxAge = System.currentTimeMillis() - (hours * 3600 * 1000);
		log.debug("Obtaining files which timestamp is older than " + maxAge
				+ " from " + base + " recursively");
		IOFileFilter filter = new OlderThanFileFilter(maxAge);
		Collection<File> files = FileUtils.listFiles(base, filter, TrueFileFilter.TRUE);
		log.info("Found " + files.size() + " files to delete");
		int count = 0;
		for (File file : files) {
			if (file.delete()) {
				count++;
			} else {
				log.error("Unable to delete file " + file);
			}
		}
		log.info("Deleted " + count + " files");
		return count;
	}

	/**
	 * A file filter implementation that only obtains the files that
	 * are older than a given amount of time.
	 * <p>
	 * The comparison is done using timestamps, not dates.
	 * @author pablo
	 */
	private static class OlderThanFileFilter implements IOFileFilter {

		private long maxAge;

		public OlderThanFileFilter(long maxAge) {
			this.maxAge = maxAge;
		}

		public boolean accept(File file) {
			return file.lastModified() < this.maxAge;
		}

		public boolean accept(File dir, String name) {
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see com.similan.portal.service.storage.AssetStorage#delete(java.lang.String)
	 */
	public void delete(final String key) {
		if (StringUtils.isBlank(key)) {
			log.warn("Attempt to delete a blank key");
			return;
		}
		String actual = getBaseDir().append(key).toString();
		log.info("Trying to delete the asset stored at " + actual);
		File file = new File(actual);
		if (!file.exists()) {
			log.warn("The given file " + file + " does not exist");
			return;
		}
		if (file.isDirectory()) {
			log.warn("The given file " + file + " is a directory");
			return;
		}
		if (!file.delete()) {
			log.warn("Could ot delete file " + file);
			return;
		}
		log.info("Successfully deleted file " + file);
	}

	public PlatformCommonSettings getPlatformCommonSettings() {
		return platformCommonSettings;
	}

	public void setPlatformCommonSettings(
			PlatformCommonSettings platformCommonSettings) {
		this.platformCommonSettings = platformCommonSettings;
	}

}
