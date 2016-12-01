package com.similan.portal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.similan.framework.dto.AssetInfo;
import com.similan.service.api.AssetStorage;

@Controller
@RequestMapping(value = "/assets", method = RequestMethod.GET)
@Slf4j
public class AssetServingRequestHandler {


	@Autowired
	private AssetStorage storage;

	@Autowired
	private ResourceHttpRequestHandler defaultHandler;

	@Autowired
	private ServletContext servletContest;

	/**
	 * Handles the requests for assets.
	 */
	@RequestMapping(value = "/{type}/{id}/{filename}", method = RequestMethod.GET)
	public void handle(HttpServletRequest request,
			@RequestParam(value = "temp", defaultValue = "false") boolean temporary,
			HttpServletResponse response) throws ServletException, IOException {
		String path = (String) request
				.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String key = path.substring("/assets".length());
		log.info("Received request to handle content with key " + key
				+ " (temporary = " + temporary + ")");
		AssetInfo asset = storage.loadAsset(key, temporary);
		if (asset == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
			return;
		}
		URL location = asset.getLocation();
		if (asset.isCanRedirect()) {
			response.sendRedirect(location.toExternalForm());
			return;
		}
		response.setContentType(this.servletContest.getMimeType(asset
				.getFileName()));
		response.setContentLength((int) asset.getSize());
		ServletOutputStream out = response.getOutputStream();
		InputStream is = location.openStream();
		IOUtils.copy(is, out);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(out);
		response.flushBuffer();
		log.info("Asset with key " + key + " served successfully");
	}

	@RequestMapping(value = "/images/{image}", method = RequestMethod.GET)
	public void defaultImage(@PathVariable("image") String image,
			final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		log.info("Handling default resource for " + image);
		String path = (String) request
				.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		request.setAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE,
				path.substring("/assets/images/".length()));
		this.defaultHandler.handleRequest(request, response);
	}
}
