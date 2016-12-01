package com.similan.portal.service.listener;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.review.ReviewCommentTagDto;

public class ReviewCommentTagEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	public ReviewCommentTagEvent(ReviewCommentTagDto source) {
		super(source);
	}
	
	@Override
	public ReviewCommentTagDto getSource(){
		return (ReviewCommentTagDto)super.getSource();
	}

}
