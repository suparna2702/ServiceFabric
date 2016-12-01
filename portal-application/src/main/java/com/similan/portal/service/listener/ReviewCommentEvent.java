package com.similan.portal.service.listener;

import org.springframework.context.ApplicationEvent;

import com.similan.framework.dto.review.ReviewCommentDto;

public class ReviewCommentEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public ReviewCommentEvent(ReviewCommentDto source) {
		super(source);
	}
	
	@Override
	public ReviewCommentDto getSource(){
		return (ReviewCommentDto)super.getSource();
	}

}
