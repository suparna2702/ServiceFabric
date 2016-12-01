package com.similan.framework.dto.fileImport;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.similan.service.api.document.dto.key.DocumentCategoryKey;
import com.similan.service.api.document.dto.key.DocumentLabelKey;

@Getter
@Setter
@ToString
public abstract class ImportFileDto implements Serializable {

  private static final long serialVersionUID = -3062581833390895304L;
  private String uuid = UUID.randomUUID().toString();
  private ImportFileSource source;
  private String fileName;
  private String size;
  private String mimeType;
  private Date lastModified;
  private boolean folder;
  private boolean selected = true;
  private List<DocumentCategoryKey> categories = new ArrayList<DocumentCategoryKey>();
  private List<DocumentLabelKey> labels = new ArrayList<DocumentLabelKey>();
  private String description;
  private boolean externallyManaged = false;

  public ImportFileDto() {

  }

  public abstract InputStream getContent();

  public void setSize(String size) {
    this.size = size;
  }

  public void setSize(long bytes) {
    if (bytes == 0)
      size = "-";
    else {
      int unit = 1024;
      if (bytes < unit)
        size = bytes + " B";
      else {
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "" + ("KMGTPE").charAt(exp - 1);
        size = String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
      }
    }
  }

}
