package com.similan.portal.view.handler;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.service.api.AssetStorage;

/**
 * Common delegate class to handle file uploads
 * @author pablo
 */
@Service("imageUploadHandler")
@Slf4j
public class ImageUploadHandler {


	@Autowired
	private AssetStorage assetStorage;

	@Transactional
	public String handleImageUpload(final ImageUpload upload) {
		try {
			String current = upload.currentKey();
			
			//strip of all special characters due to browser compatibility
			String fileName = StringUtils.replace(upload.getFile().getFileName(), "\\s", "");
			log.info("Stripped file name " + fileName);
			

			log.info("Handling file upload event for file " + fileName);
			String key = this.assetStorage.storeTemporary(upload.getType(),
				upload.getId(), upload.getFile());
			key = this.assetStorage.makePersistent(key);
			upload.setImageKey(key);
			upload.update();
			log.info("Image upload handle successfully. Key is " + key);
			log.debug("Trying to delete previous image: " + current);
			
			if(key.equalsIgnoreCase(current) != true){
			  this.assetStorage.delete(current);
			}
			
			return null;
		} catch (Exception e) {
			log.error("Error while handling file upload", e);
			//TODO Should we rollback?
			return "Image upload failed";
		}
	}

	@Transactional
	public String handleImageDeletion(final ImageDeletion deletion) {
		try {
			String key = deletion.getCurrentKey();
			log.info("Handling image deletion for file " + key);
			this.assetStorage.delete(key);
			deletion.update();
			log.info("Image deletion handled successfully. Key was " + key);
			return null;
		} catch (Exception e) {
			log.error("Error while handling image deletion.", e);
			//TODO Should we rollback?
			return "Image deletion failed";
		}
	}
}
