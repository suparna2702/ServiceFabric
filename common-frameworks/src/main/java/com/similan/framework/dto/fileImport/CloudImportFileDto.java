package com.similan.framework.dto.fileImport;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.scribe.model.Token;

@Getter
@Setter
@ToString
public class CloudImportFileDto extends ImportFileDto {

  private static final long serialVersionUID = -5595030914857788736L;
  private String id;
  private Token token;
  private File downloadedFile;
  private String downloadUrl;
  private String thumbNailUrl;
  private String fileAccessUrl;
 
  public CloudImportFileDto() {

  }

  @Override
  public InputStream getContent() {
    if (downloadedFile == null)
      return null;
    try {
      return new FileInputStream(downloadedFile);
    } catch (Exception e) {
      return null;
    }

  }

}
