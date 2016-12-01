package com.similan.portal.model;

import java.io.Serializable;
import java.util.List;

import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.comment.dto.extended.CommentAndRepliesDto;

public class TaskModel implements Serializable {

	private static final long serialVersionUID = 1L;

    private TaskDto taskData;
    
    private List<CommentAndRepliesDto<TaskKey>> comments;
    
    public TaskModel(TaskDto taskData){
    	this.taskData = taskData;
    }
    
    public String getName() {
    	return taskData.getKey().getName();
    }
    
    public String getId(){
    	return taskData.getKey().getName();
    }
    
    public int hashCode() {
		return taskData.hashCode();
	}

	public boolean equals(Object other) {
		return taskData.equals(other);
	}

	public TaskDto getTaskData() {
		return taskData;
	}

	public List<CommentAndRepliesDto<TaskKey>> getComments() {
		return comments;
	}

	public void setComments(List<CommentAndRepliesDto<TaskKey>> comments) {
		this.comments = comments;
	}
	
	public int getCommentCount() {
		if (getComments() != null)
				return getComments().size();
		return 0;
	}
	
	
}
