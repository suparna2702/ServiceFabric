package com.similan.portal.view;

import java.io.InputStream;
import java.util.Collections;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

import com.similan.framework.dto.fileImport.ImportFileDto;

public class AttachmentUtils {

  public static Attachment create(String id, String contentType,
      String filename, InputStream stream) {
    MultivaluedMap<String, String> headers = new MultivaluedHashMap<String, String>(
        3);
    headers.put("Content-ID", Collections.singletonList(id));
    headers.put("Content-Type", Collections.singletonList(contentType));
    headers.put(
        "Content-Disposition",
        Collections.singletonList("attachment; filename=\""
            + filename.replace("\"", "\\\"") + "\""));
    return new Attachment(stream, headers);
  }

  public static Attachment create(String id, ImportFileDto file) {
    return create(id, file.getMimeType(), file.getFileName(), file.getContent());
  }

  public static Attachment create(ImportFileDto file) {
    return create("no-id", file);
  }
  
}
