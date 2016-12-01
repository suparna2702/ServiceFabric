package com.similan.service.impl.wall.entrymarshaller.collaborationworkspace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.wall.IWallEntrySubject;
import com.similan.domain.entity.wall.collaborationworkspace.TaskCreatedWallEntry;
import com.similan.service.api.base.dto.basic.IKeyHolderDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.community.dto.basic.ActorDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.collaborationworkspace.TaskCreatedWallEntryDto;
import com.similan.service.api.wall.dto.key.IWallEntrySubjectKey;
import com.similan.service.api.wall.dto.key.WallEntryKey;
import com.similan.service.impl.collaborationworkspace.CollaborationWorkspaceMarshaller;
import com.similan.service.impl.common.GenericReferenceMarshaller;
import com.similan.service.impl.wall.WallEntryMarshaller;

@Component
public class TaskCreatedWallEntryMarshaller extends
    WallEntryMarshaller<TaskCreatedWallEntry, CollaborationWorkspaceKey> {
  @Autowired
  private GenericReferenceMarshaller genericReferenceMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;

  @Override
  public WallEntryDto<CollaborationWorkspaceKey> marshall(
      WallEntryKey<CollaborationWorkspaceKey> entryKey,
      ActorDto initiatorDto, Date date, TaskCreatedWallEntry entry) {
	  
	GenericReference<IWallEntrySubject> subject = entry.getSubject();
	IKeyHolderDto<TaskKey> taskKey = genericReferenceMarshaller
	                   .marshall(subject, IWallEntrySubjectKey.class);
	
    Task task = collaborationWorkspaceMarshaller
    		.unmarshallTaskKey(taskKey.getKey(), true);
    
    TaskDto taskDto = collaborationWorkspaceMarshaller
                    .marshallTask(task);
    
    TaskCreatedWallEntryDto entryDto = new TaskCreatedWallEntryDto(entryKey,
        initiatorDto, date, taskDto);
    return entryDto;
  }
}
