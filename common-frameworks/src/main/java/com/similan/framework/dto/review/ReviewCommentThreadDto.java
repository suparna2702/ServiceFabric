package com.similan.framework.dto.review;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewCommentThreadDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private List<ReviewCommentDto> commentStream = new ArrayList<ReviewCommentDto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ReviewCommentDto> getCommentStream() {
		
		if(commentStream == null){
			commentStream = new ArrayList<ReviewCommentDto>();
		}
		
		return commentStream;
	}

	public void setCommentStream(List<ReviewCommentDto> commentStream) {
		this.commentStream = commentStream;
	}
	
	
}
