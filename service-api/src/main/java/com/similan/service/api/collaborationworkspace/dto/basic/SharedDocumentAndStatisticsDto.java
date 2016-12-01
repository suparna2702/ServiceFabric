package com.similan.service.api.collaborationworkspace.dto.basic;

import com.similan.service.api.base.dto.basic.KeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.key.SharedDocumentKey;
import com.similan.service.api.document.dto.extended.DocumentStatisticsDto;

public class SharedDocumentAndStatisticsDto extends KeyHolderDto<SharedDocumentKey> {
	
	private SharedDocumentDto sharedDocument;
	
	private DocumentStatisticsDto documentStatistics;
	
	private Integer version;
	
	protected SharedDocumentAndStatisticsDto(){
		
	}
	
	public SharedDocumentAndStatisticsDto(SharedDocumentKey key, SharedDocumentDto sharedDocument,
			DocumentStatisticsDto documentStatistics, Integer version){
		super(key);
		this.sharedDocument = sharedDocument;
		this.documentStatistics = documentStatistics;
		this.version = version;
	}

	public SharedDocumentDto getSharedDocument() {
		return sharedDocument;
	}

	public DocumentStatisticsDto getDocumentStatistics() {
		return documentStatistics;
	}
	
	public Integer getVersion() {
    return version;
  }

}
