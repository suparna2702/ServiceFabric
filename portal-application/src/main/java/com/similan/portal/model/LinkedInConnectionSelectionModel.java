package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.model.SelectableDataModel;

import com.similan.framework.dto.network.LinkedInConnection;

@Slf4j
public class LinkedInConnectionSelectionModel extends
    ListDataModel<LinkedInConnection> implements
    SelectableDataModel<LinkedInConnection>, Serializable {
  
  
  public LinkedInConnectionSelectionModel(){
    
  }
  
  public LinkedInConnectionSelectionModel(List<LinkedInConnection> connections){
    super(connections);
    log.info("Selecting data model");
  }

  private static final long serialVersionUID = 1L;

  @Override
  public LinkedInConnection getRowData(String rowKey) {
    @SuppressWarnings("unchecked")
    List<LinkedInConnection> listData = (List<LinkedInConnection>) getWrappedData();
    for (LinkedInConnection connection : listData) {
      if (connection.getLinkedInId().equalsIgnoreCase(rowKey)) {
        return connection;
      }
    }
    return null;
  }

  @Override
  public Object getRowKey(LinkedInConnection connection) {
    return connection.getLinkedInId();
  }

}
