package com.similan.domain.entity.community;

import org.apache.commons.lang.StringUtils;

public enum PhotoViewOption {
    HidePhoto {
		@Override
		public String effectivePhoto(String defaultPhoto, String memberPhoto) {
			return defaultPhoto;
		}
	}, ShowPhoto {
		@Override
		public String effectivePhoto(String defaultPhoto, String memberPhoto) {
			if (StringUtils.isEmpty(memberPhoto)) {
				return defaultPhoto;
			}
			return memberPhoto;
		}
	};

	/**
	 * Returns the photo that should be effectively displayed, based on the
	 * member and default photos.
	 *
	 * @param defaultPhoto
	 *            The default photo path.
	 * @param memberPhoto
	 *            The actual member photo path.
	 * @return The photo location that should be displayed according to the
	 *         actual settings.
	 */
    public abstract String effectivePhoto(String defaultPhoto, String memberPhoto);
}
