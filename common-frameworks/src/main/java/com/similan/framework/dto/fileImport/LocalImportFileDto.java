package com.similan.framework.dto.fileImport;

import java.io.InputStream;

import org.primefaces.model.UploadedFile;

public class LocalImportFileDto extends ImportFileDto {

	private static final long serialVersionUID = -4342720657134312992L;
	private UploadedFile uploadedFile;

	public LocalImportFileDto() {

	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	@Override
	public InputStream getContent() {
		if (uploadedFile == null)
			return null;
		try {
			return uploadedFile.getInputstream();
		} catch (Exception e) {
			return null;
		}
	}
}
