package com.similan.service.rest.base;

import javax.ws.rs.Path;

import org.apache.cxf.jaxrs.ext.ResourceComparator;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

import com.similan.service.api.base.dto.key.EntityPaths;

@Component
public class SimilanResourceComparator implements ResourceComparator {

  @Override
  public int compare(ClassResourceInfo cri1, ClassResourceInfo cri2,
      Message message) {
    if (cri1 == cri2) {
      return -1;
    }
    Path path1 = cri1.getResourceClass().getAnnotation(Path.class);
    if (path1.value().equals(RestApplication.API_DOCS_PATH)) {
      return -1;
    }
    Path path2 = cri2.getResourceClass().getAnnotation(Path.class);
    if (path2.value().equals(RestApplication.API_DOCS_PATH)) {
      return 1;
    }
    boolean generic1 = EntityPaths.isGeneric(path1.value());
    boolean generic2 = EntityPaths.isGeneric(path2.value());
    if (generic1 && generic2 || !generic1 && !generic2) {
      return 0;
    } else if (generic1) {
      return -1;
    } else {
      return 1;
    }
  }

  @Override
  public int compare(OperationResourceInfo oper1, OperationResourceInfo oper2,
      Message message) {
    return 0;
  }

}
