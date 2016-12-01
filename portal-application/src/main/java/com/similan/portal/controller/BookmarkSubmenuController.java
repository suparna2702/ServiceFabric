package com.similan.portal.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.databean.BookmarkEnitityUrlMap;
import com.similan.service.api.bookmark.BookmarkService;
import com.similan.service.api.community.SocialActorService;

@Controller
@RequestMapping(value = "/bookmark", method = RequestMethod.GET)
public class BookmarkSubmenuController {
	@Autowired
	private ResourceHttpRequestHandler defaultHandler;

	@Autowired
	private ServletContext servletContest;
	
	@Autowired
	private BookmarkEnitityUrlMap bookmarkEnitityUrlMap;
	
	@Autowired
	private BookmarkService bookmarkService;
	
	@Autowired
	private MemberInfoDto memberInfo;
	
	@Autowired
	private SocialActorService socialActorService;
	
	@RequestMapping(value = "/all/", method = RequestMethod.GET)
	public void getBookmarks(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (memberInfo == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot fetch bookmark");
			return;
		}
		
	}

}
