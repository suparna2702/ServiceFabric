package com.similan.portal.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

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
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.similan.service.api.asset.AssetStream;
import com.similan.service.api.document.DocumentInstanceService;
import com.similan.service.api.document.dto.key.DocumentInstanceKey;

@Controller
@RequestMapping(value = "/documenticon", method = RequestMethod.GET)
@Slf4j
public class DocumentIconServingRequestHandler {


	@Autowired
	private DocumentInstanceService storage;

	@Autowired
	private ResourceHttpRequestHandler defaultHandler;

	@Autowired
	private ServletContext servletContest;

	/**
	 * Handles the requests for assets.
	 */
	@RequestMapping(value = "/{workspaceOwnerName}/{workspaceName}/{documentName}/{documentInstanceVersion}", method = RequestMethod.GET)
	public void handle(
			HttpServletRequest request,
			@PathVariable("workspaceOwnerName") String workspaceOwnerName,
			@PathVariable("workspaceName") String workspaceName,
			@PathVariable("documentName") String documentName,
			@PathVariable("documentInstanceVersion") Integer documentInstanceVersion,
			HttpServletResponse response)
			throws ServletException, IOException {

    DocumentInstanceKey documentInstanceKey = new DocumentInstanceKey(URLDecoder.decode(
        workspaceOwnerName, "ISO-8859-1"),
        URLDecoder.decode(workspaceName, "ISO-8859-1"), URLDecoder.decode(
            documentName, "ISO-8859-1"), documentInstanceVersion);
		AssetStream stream = storage.retrieveIcon(documentInstanceKey);
		if (stream == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Image not found");
			return;
		}

		response.setContentType(this.servletContest.getMimeType(stream
				.getInfo().getFilename()));
		ServletOutputStream out = response.getOutputStream();
		InputStream is = stream.getInput();
		IOUtils.copy(is, out);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(out);
		response.flushBuffer();
		log.info("Asset with key " + documentInstanceKey + " served successfully");
	}
}
