package com.similan.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.similan.domain.entity.lead.LeadActivityType;
import com.similan.domain.entity.util.AddressDto;
import com.similan.framework.dto.lead.CrmLeadActivityDto;
import com.similan.framework.dto.lead.CrmLeadDto;
import com.similan.framework.dto.lead.CrmLeadNoteDto;
import com.similan.framework.dto.lead.LeadDto;
import com.similan.service.api.CoreServiceErrorCode;
import com.similan.service.api.ZohoFacade;
import com.similan.service.exception.CoreServiceException;

@Slf4j
public class ZohoFacadeImpl implements ZohoFacade {
	private static final String ZOHO_API_ROW_NUMBER_ATTRIBUTE = "no";
	private static final String ZOHO_API_ROW_TAG = "row";
	private static final String ZOHO_API_ID_VAL = "Id";
	private static final String ZOHO_API_LEADS_TAG = "Leads";
	private static final String ZOHO_API_FIELD_ATTRIBUTE_NAME = "val";
	private static final String ZOHO_API_XPATH_FIELD_SET = "//FL";
	private static final String ZOHO_API_INSER_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Leads/insertRecords?authtoken={authtoken}&scope={scope}";
	private static final String ZOHO_API_UPDATE_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Leads/updateRecords?authtoken={authtoken}&scope={scope}&id={id}";
	private static final String ZOHO_API_SCOPE_CRM_API = "crmapi";
	private static final String ZOHO_API_PARAMETER_SCOPE = "scope";
	private static final String ZOHO_API_PARAMETER_AUTH_TOKEN = "authtoken";
	private static final String ZOHO_API_GET_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Leads/getRecords?authtoken={authtoken}&scope={scope}&fromIndex={fromIndex}&toIndex={toIndex}&selectColumns={selectColumns}";
	private static final String ZOHO_API_XPATH_RESULTS_ROW = "//row";
	private static final String ZOHO_API_XML_FIELD_TAG = "FL";
	private static final String ZOHO_API_NOTE_TAG = "Notes";
	private static final String ZOHO_API_CALL_TAG ="Calls";
	private static final String ZOHO_API_EVENT_TAG = "Event";

	private static final String ZOHO_API_PARAMETER_FROM_INDEX = "fromIndex";
	private static final String ZOHO_API_PARAMETER_TO_INDEX = "toIndex";
	private static final String ZOHO_API_PARAMETER_SELECT_COLUMNS = "selectColumns";

	private static final int ZOHO_MAXIMUM_PAGE_SIZE = 200;
	private static final String ZOHO_API_XPATH_ERROR = "/response/error/message";
	private static final String ZOHO_API_PARAMETER_ID = "id";
	private static final String ZOHO_API_GET_RECORD_BY_ID_URL = "https://crm.zoho.com/crm/private/xml/Leads/getRecordById?authtoken={authtoken}&scope={scope}&id={id}&selectColumns={selectColumns}";
	private static final String ZOHO_API_MODIFIED_TIME_VAL = "Modified Time";
	private static final String ZOHO_API_GET_RELATED_NOTES_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Notes/getRelatedRecords?authtoken={authtoken}&selectColumns={selectColumns}&scope={scope}&parentModule=Leads&id={id}";
	private static final String ZOHO_API_INSERT_NOTE_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Notes/insertRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_DELETE_NOTE_RECORD_URL = "https://crm.zoho.com/crm/private/xml/Notes/deleteRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_UPDATE_NOTE_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Notes/updateRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_INSERT_CALL_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Calls/insertRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_DELETE_CALL_RECORD_URL = "https://crm.zoho.com/crm/private/xml/Calls/deleteRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_UPDATE_CALL_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Calls/updateRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_GET_RELATED_CALLS_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Calls/getRelatedRecords?authtoken={authtoken}&selectColumns={selectColumns}&scope={scope}&parentModule=Leads&id={id}";
	private static final String ZOHO_API_INSERT_EVENT_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Events/insertRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_DELETE_EVENT_RECORD_URL = "https://crm.zoho.com/crm/private/xml/Events/deleteRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_UPDATE_EVENT_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Events/updateRecords?authtoken={authtoken}&scope=crmapi&id={id}";
	private static final String ZOHO_API_GET_RELATED_EVENT_RECORDS_URL = "https://crm.zoho.com/crm/private/xml/Events/getRelatedRecords?authtoken={authtoken}&selectColumns={selectColumns}&scope={scope}&parentModule=Leads&id={id}";

	private RestTemplate restTemplate = null;

	public List<CrmLeadDto> getZohoLeads(String authToken)
			throws CoreServiceException {
		boolean done = false;
		int startIndex = 1;
		int endIndex = ZOHO_MAXIMUM_PAGE_SIZE;

		List<CrmLeadDto> result = new ArrayList<CrmLeadDto>();
		while (done == false) {
			List<CrmLeadDto> page = getZohoLeads(authToken, startIndex,
					endIndex);
			result.addAll(page);

			if (page.size() < endIndex - startIndex) {
				done = true;
			}
			startIndex += ZOHO_MAXIMUM_PAGE_SIZE;
			endIndex += ZOHO_MAXIMUM_PAGE_SIZE;
		}
		return result;
	}

	public List<CrmLeadDto> getZohoLeads(String authToken, long start, long end)
			throws CoreServiceException {
		try {
			if (end - start > ZOHO_MAXIMUM_PAGE_SIZE)
				throw new IllegalArgumentException(
						"Zoho api only allows batch sizes no greater than "
								+ ZOHO_MAXIMUM_PAGE_SIZE);
			String result = callZohoGetRecordsForLeads(authToken, start, end);
			log.info("Response to getRecords call from Zoho " + result);
			checkZohoResultForError(result);

			List<CrmLeadDto> remoteLeadDtos = parseGetRecordsResponse(result);

			for (CrmLeadDto remoteLeadDto : remoteLeadDtos) {
				remoteLeadDto.getNoteList().addAll(
						getZohoNotesForLead(authToken, remoteLeadDto));
				remoteLeadDto.getActivityList().addAll(
						getZohoCallsForLead(authToken, remoteLeadDto));
				remoteLeadDto.getActivityList().addAll(
						getZohoEventsForLead(authToken, remoteLeadDto));
			}
			return remoteLeadDtos;
		} catch (Exception exception) {
			log.error("Failed to get zoho leads ", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	public List<CrmLeadActivityDto> getZohoEventsForLead(String authToken,
			CrmLeadDto remoteLeadDto) throws CoreServiceException {
		try {
			String result = callZohoGetRelatedRecordsForEvents(authToken,
					remoteLeadDto.getCrmId());
			log.info("Response to getRecords for activities call from Zoho "
					+ result);
			checkZohoResultForError(result);

			return parseGetRecordsForEventsResponse(result);

		} catch (Exception exception) {
			log.error("Failed to get zoho activities ", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	public List<CrmLeadActivityDto> getZohoCallsForLead(String authToken,
			CrmLeadDto remoteLeadDto) throws CoreServiceException {
		try {
			String result = callZohoGetRelatedRecordsForCalls(authToken,
					remoteLeadDto.getCrmId());
			log.info("Response to getRecords for activities call from Zoho "
					+ result);
			checkZohoResultForError(result);

			return parseGetRecordsForCallsResponse(result);

		} catch (Exception exception) {
			log.error("Failed to get zoho activities ", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	public List<CrmLeadNoteDto> getZohoNotesForLead(String authToken,
			CrmLeadDto remoteLeadDto) throws CoreServiceException {
		try {
			String result = callZohoGetRelatedRecordsForNotes(authToken,
					remoteLeadDto.getCrmId());
			log.info("Response to getRecords for notes call from Zoho "
					+ result);
			checkZohoResultForError(result);

			return parseGetRecordsForNotesResponse(result);

		} catch (Exception exception) {
			log.error("Failed to get zoho notes ", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	private void checkZohoResultForError(String result) throws Exception {
		NodeList errorData;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder
				.parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(ZOHO_API_XPATH_ERROR);

		errorData = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		if (errorData != null && errorData.getLength() > 0) {
			Element error = (Element) errorData.item(0);
			throw new Exception(error.getTextContent());
		}
	}

	private List<CrmLeadActivityDto> parseGetRecordsForEventsResponse(
			String result) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException {
		NodeList rowData;
		rowData = getRowDataFromGetRecordsResponse(result);

		List<CrmLeadActivityDto> activityList = new ArrayList<CrmLeadActivityDto>();

		for (int i = 0; i < rowData.getLength(); i++) {
			CrmLeadActivityDto activity = new CrmLeadActivityDto();
			activity.setLeadActivityType(LeadActivityType.Meeting);
			Element rowElement = (Element) rowData.item(i);
			getEventDataFromGetRecordsResponseRow(rowData, rowElement, activity);
			activityList.add(activity);

		}
		return activityList;
	}

	private List<CrmLeadActivityDto> parseGetRecordsForCallsResponse(
			String result) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException {
		NodeList rowData;
		rowData = getRowDataFromGetRecordsResponse(result);

		List<CrmLeadActivityDto> activityList = new ArrayList<CrmLeadActivityDto>();

		for (int i = 0; i < rowData.getLength(); i++) {
			CrmLeadActivityDto activity = new CrmLeadActivityDto();
			activity.setLeadActivityType(LeadActivityType.Phone);
			Element rowElement = (Element) rowData.item(i);
			getCallDataFromGetRecordsResponseRow(rowData, rowElement, activity);
			activityList.add(activity);

		}
		return activityList;
	}

	private List<CrmLeadNoteDto> parseGetRecordsForNotesResponse(String result)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException, JAXBException {
		NodeList rowData;
		rowData = getRowDataFromGetRecordsResponse(result);

		List<CrmLeadNoteDto> noteList = new ArrayList<CrmLeadNoteDto>();

		for (int i = 0; i < rowData.getLength(); i++) {
			CrmLeadNoteDto note = new CrmLeadNoteDto();

			Element rowElement = (Element) rowData.item(i);
			getNoteDataFromGetRecordsResponseRow(rowData, rowElement, note);
			noteList.add(note);

		}
		return noteList;
	}

	private List<CrmLeadDto> parseGetRecordsResponse(String result)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException, JAXBException {
		NodeList rowData;
		rowData = getRowDataFromGetRecordsResponse(result);

		List<CrmLeadDto> leadList = new ArrayList<CrmLeadDto>();

		for (int i = 0; i < rowData.getLength(); i++) {
			CrmLeadDto lead = new CrmLeadDto();
			AddressDto location = new AddressDto();
			lead.setLocation(location);

			Element rowElement = (Element) rowData.item(i);
			getLeadDataFromGetRecordsResponseRow(rowData, rowElement, lead);
			leadList.add(lead);

		}
		return leadList;
	}

	private void getNoteDataFromGetRecordsResponseRow(NodeList rowData,
			Element rowElement, CrmLeadNoteDto lead) throws JAXBException {
		NodeList noteData = rowElement
				.getElementsByTagName(ZOHO_API_XML_FIELD_TAG);

		for (int j = 0; j < noteData.getLength(); j++) {

			Element flElement = (Element) noteData.item(j);
			String attributeName = flElement
					.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			String attributeValue = flElement.getTextContent();

			if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("id",
					attributeName))
				lead.setCrmId(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Title", attributeName))
				lead.getLeadNote().setSubject(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Note Content", attributeName))
				lead.getLeadNote().setMessage(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Modified Time", attributeName))
				lead.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(attributeValue));
		}
	}

	private void getEventDataFromGetRecordsResponseRow(NodeList rowData,
			Element rowElement, CrmLeadActivityDto activity)
			throws JAXBException {
		NodeList activityData = rowElement
				.getElementsByTagName(ZOHO_API_XML_FIELD_TAG);

		for (int j = 0; j < activityData.getLength(); j++) {

			Element flElement = (Element) activityData.item(j);
			String attributeName = flElement
					.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			String attributeValue = flElement.getTextContent();
			log.info("Handling tag " + attributeName + " with value " + attributeValue);
			if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("ACTIVITYID",
					attributeName))
				activity.setCrmId(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Subject", attributeName))
				activity.setSubjectActivity(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Start DateTime", attributeName))
				activity.setStartDate(getDateFromZohoFormatedString(attributeValue));

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"End DateTime", attributeName))
				activity.setEndDate(getDateFromZohoFormatedString(attributeValue));

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Venue", attributeName))
				activity.setVenue(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Modified Time", attributeName))
				activity.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(attributeValue));
			
			log.info("Found remote activity with data " + activity);
		}
	}

	private void getCallDataFromGetRecordsResponseRow(NodeList rowData,
			Element rowElement, CrmLeadActivityDto activity)
			throws JAXBException {
		NodeList activityData = rowElement
				.getElementsByTagName(ZOHO_API_XML_FIELD_TAG);

		for (int j = 0; j < activityData.getLength(); j++) {

			Element flElement = (Element) activityData.item(j);
			String attributeName = flElement
					.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			String attributeValue = flElement.getTextContent();

			if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("ACTIVITYID",
					attributeName))
				activity.setCrmId(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Subject", attributeName))
				activity.setSubjectActivity(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Note Content", attributeName))
				activity.setActivityDescription(attributeValue);

			else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
					"Modified Time", attributeName))
				activity.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(attributeValue));
		}
	}

	private void getLeadDataFromGetRecordsResponseRow(NodeList rowData,
			Element rowElement, CrmLeadDto lead) throws JAXBException {
		NodeList leadData = rowElement
				.getElementsByTagName(ZOHO_API_XML_FIELD_TAG);
		for (int j = 0; j < leadData.getLength(); j++) {

			Element flElement = (Element) leadData.item(j);
			String attributeName = flElement
					.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			String attributeValue = flElement.getTextContent();

			updateLeadFromXml(lead, attributeName, attributeValue);
		}
	}

	private NodeList getRowDataFromGetRecordsResponse(String result)
			throws ParserConfigurationException, XPathExpressionException,
			SAXException, IOException {
		NodeList rowData;
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder
				.parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(ZOHO_API_XPATH_RESULTS_ROW);

		rowData = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		return rowData;
	}

	private String callZohoGetRecordsForLeads(String authToken, long start,
			long end) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
		parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
		parameters.put(ZOHO_API_PARAMETER_FROM_INDEX, Long.toString(start));
		parameters.put(ZOHO_API_PARAMETER_TO_INDEX, Long.toString(end));
		parameters.put(ZOHO_API_PARAMETER_SELECT_COLUMNS, "All");

		log.info("Calling get records with parameters " + parameters);
		String result = restTemplate.getForObject(ZOHO_API_GET_RECORDS_URL,
				String.class, parameters);

		return result;
	}

	private String callZohoGetRelatedRecordsForNotes(String authToken,
			String leadId) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
		parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
		parameters.put(ZOHO_API_PARAMETER_SELECT_COLUMNS, "All");
		parameters.put(ZOHO_API_PARAMETER_ID, leadId);

		log.info("Calling get notes with parameters " + parameters);
		String result = restTemplate.getForObject(
				ZOHO_API_GET_RELATED_NOTES_RECORDS_URL, String.class,
				parameters);

		return result;
	}

	private String callZohoGetRelatedRecordsForEvents(String authToken,
			String leadId) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
		parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
		parameters.put(ZOHO_API_PARAMETER_SELECT_COLUMNS, "All");
		parameters.put(ZOHO_API_PARAMETER_ID, leadId);

		log.info("Calling get events with parameters " + parameters);
		String result = restTemplate.getForObject(
				ZOHO_API_GET_RELATED_EVENT_RECORDS_URL, String.class,
				parameters);

		return result;
	}

	private String callZohoGetRelatedRecordsForCalls(String authToken,
			String leadId) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
		parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
		parameters.put(ZOHO_API_PARAMETER_SELECT_COLUMNS, "All");
		parameters.put(ZOHO_API_PARAMETER_ID, leadId);

		log.info("Calling get calls with parameters " + parameters);
		String result = restTemplate.getForObject(
				ZOHO_API_GET_RELATED_CALLS_RECORDS_URL, String.class,
				parameters);

		return result;
	}

	private Date getDateFromZohoFormatedString(String dateString) {

		try {
			return DateUtils.parseDate(dateString,
					new String[] { "yyyy-MM-dd HH:mm:ss" });
		} catch (ParseException e) {
			log.warn("Cannot parse date:" + dateString, e);
			return null;
		}

	}

	private String getZohoFormatedDateStringFromDate(Date date) {

		// Note, zoho expects dates to always be in PST
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss",
				TimeZone.getTimeZone("America/Los_Angeles"));
	}

	private void updateLeadFromXml(CrmLeadDto lead, String attributeName,
			String attributeValue) throws JAXBException {

		if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("LEADID",
				attributeName))
			lead.setCrmId(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"FIRST NAME", attributeName))
			lead.setFirstName(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"LAST NAME", attributeName))
			lead.setLastName(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("TITLE",
				attributeName))
			lead.setName(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("PHONE",
				attributeName))
			lead.setContactPhone(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("EMAIL",
				attributeName))
			lead.setContactEmail(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("CITY",
				attributeName))
			lead.getLocation().setCity(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("STREET",
				attributeName))
			lead.getLocation().setStreet(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase("STATE",
				attributeName))
			lead.getLocation().setState(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"COUNTRY", attributeName))
			lead.getLocation().setCountry(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"ZIP CODE", attributeName))
			lead.getLocation().setZipCode(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"COMPANY", attributeName))
			lead.setCompany(attributeValue);

		else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(
				"MODIFIED TIME", attributeName))
			lead.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(attributeValue));

	}

	public void updateLead(String authToken, CrmLeadDto lead)
			throws CoreServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			parameters.put("id", lead.getCrmId());
			log.info("Calling Zoho updateLead with parameters" + parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData", convertLeadSourceToZohoXml(lead));
			log.info("Calling Zoho updateLead with parameters" + map);

			String result = restTemplate.postForObject(
					ZOHO_API_UPDATE_RECORDS_URL, map, String.class, parameters);
			log.info("Response to updateLead call from Zoho " + result);
			checkZohoResultForError(result);
			updateLeadFromZohoResponse(result, lead);

		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	public void createLead(String authToken, CrmLeadDto lead)
			throws CoreServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			log.info("Calling Zoho createLead with parameters" + parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData", convertLeadSourceToZohoXml(lead));
			log.info("Calling Zoho createLead with parameters" + map);

			String result = restTemplate.postForObject(
					ZOHO_API_INSER_RECORDS_URL, map, String.class, parameters);
			log.info("Response to createLead call from Zoho " + result);
			checkZohoResultForError(result);
			updateLeadFromZohoResponse(result, lead);
		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	private void updateLeadFromZohoResponse(String result, CrmLeadDto lead)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder
				.parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(ZOHO_API_XPATH_FIELD_SET);

		NodeList rowData = (NodeList) expr
				.evaluate(doc, XPathConstants.NODESET);
		if (rowData.getLength() == 0)
			return;

		for (int i = 0; i < rowData.getLength(); i++) {
			Element element = (Element) rowData.item(i);
			String val = element.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_ID_VAL)) {
				lead.setCrmId(element.getTextContent());

			}
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_MODIFIED_TIME_VAL)) {
				lead.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(element
						.getTextContent()));
			}
		}
	}

	private void updateNoteFromZohoResponse(String result, CrmLeadNoteDto note)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder
				.parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(ZOHO_API_XPATH_FIELD_SET);

		NodeList rowData = (NodeList) expr
				.evaluate(doc, XPathConstants.NODESET);
		if (rowData.getLength() == 0)
			return;

		for (int i = 0; i < rowData.getLength(); i++) {
			Element element = (Element) rowData.item(i);
			String val = element.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_ID_VAL)) {
				note.setCrmId(element.getTextContent());

			}
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_MODIFIED_TIME_VAL)) {
				note.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(element
						.getTextContent()));
			}
		}
	}

	private String convertLeadSourceToZohoXml(LeadDto lead)
			throws JAXBException, ParserConfigurationException,
			TransformerException, UnsupportedEncodingException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement(ZOHO_API_LEADS_TAG);
		document.appendChild(rootElement);
		Element rowElement = document.createElement(ZOHO_API_ROW_TAG);
		rowElement.setAttribute(ZOHO_API_ROW_NUMBER_ATTRIBUTE, "1");
		rootElement.appendChild(rowElement);

		Element data = null;
		boolean hasData = false;

		if (lead.getFirstName() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "First Name");
			data.setTextContent(lead.getFirstName());
			rowElement.appendChild(data);
			hasData = true;
		}
		if (lead.getLastName() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Last Name");
			data.setTextContent(lead.getLastName());
			rowElement.appendChild(data);
			hasData = true;
		} else
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA);

		if (lead.getName() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Title");
			data.setTextContent(lead.getName());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getContactPhone() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Phone");
			data.setTextContent(lead.getContactPhone());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getContactEmail() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Email");
			data.setTextContent(lead.getContactEmail());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getLocation().getCity() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "City");
			data.setTextContent(lead.getLocation().getCity());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getLocation().getStreet() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Street");
			data.setTextContent(lead.getLocation().getStreet());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getLocation().getState() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "State");
			data.setTextContent(lead.getLocation().getState());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getLocation().getCountry() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Country");
			data.setTextContent(lead.getLocation().getCountry());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getLocation().getZipCode() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Zip Code");
			data.setTextContent(lead.getLocation().getZipCode());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (lead.getCompany() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Company");
			data.setTextContent(lead.getCompany());
			rowElement.appendChild(data);
			hasData = true;
		} else {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Company");
			data.setTextContent("Unknown");
			rowElement.appendChild(data);
		}

		if (hasData == false)
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(baos);
		transformer.transform(source, result);

		return new String(baos.toByteArray(), "UTF-8");

	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public CrmLeadDto getLead(String authToken, String crmId)
			throws CoreServiceException {
		try {
			CrmLeadDto lead = null;
			String result = callZohoGetRecordById(authToken, crmId);
			log.info("Response to getRecordsById call from Zoho " + result);
			checkZohoResultForError(result);
			List<CrmLeadDto> leadSourceList = parseGetRecordsResponse(result);
			if (leadSourceList != null && leadSourceList.size() > 0)
				lead = leadSourceList.get(0);

			return lead;
		} catch (Exception exception) {
			log.error("Failed to get zoho leads ", exception);
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	private String callZohoGetRecordById(String authToken, String crmId) {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, authToken);
		parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
		parameters.put(ZOHO_API_PARAMETER_ID, crmId);
		parameters.put(ZOHO_API_PARAMETER_SELECT_COLUMNS, "All");

		log.info("Calling get records by ID with parameters " + parameters);
		String result = restTemplate.getForObject(
				ZOHO_API_GET_RECORD_BY_ID_URL, String.class, parameters);

		return result;
	}

	public void createLeadNote(String zohoAuthToken, CrmLeadDto remoteLead,
			CrmLeadNoteDto remoteLeadNoteDto) throws CoreServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			log.info("Calling Zoho createLead with parameters" + parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData",
					convertLeadNoteToZohoXml(remoteLead, remoteLeadNoteDto));
			log.info("Calling Zoho createLead with parameters" + map);

			String result = restTemplate.postForObject(
					ZOHO_API_INSERT_NOTE_RECORDS_URL, map, String.class,
					parameters);
			log.info("Response to createNote call from Zoho " + result);
			checkZohoResultForError(result);
			updateNoteFromZohoResponse(result, remoteLeadNoteDto);
		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}

	}

	public void updateLeadNote(String zohoAuthToken, CrmLeadDto remoteLead,
			CrmLeadNoteDto remoteLeadNoteDto) throws CoreServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			parameters.put("id", remoteLeadNoteDto.getCrmId());
			log.info("Calling Zoho updateNote with parameters" + parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData",
					convertLeadNoteToZohoXml(remoteLead, remoteLeadNoteDto));
			log.info("Calling Zoho updateNote with parameters" + map);

			String result = restTemplate.postForObject(
					ZOHO_API_UPDATE_NOTE_RECORDS_URL, map, String.class,
					parameters);
			log.info("Response to updateNote call from Zoho " + result);
			checkZohoResultForError(result);
			updateNoteFromZohoResponse(result, remoteLeadNoteDto);

		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}

	}

	private String convertLeadNoteToZohoXml(CrmLeadDto remoteLead,
			CrmLeadNoteDto note) throws JAXBException,
			ParserConfigurationException, TransformerException,
			UnsupportedEncodingException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement(ZOHO_API_NOTE_TAG);
		document.appendChild(rootElement);
		Element rowElement = document.createElement(ZOHO_API_ROW_TAG);
		rowElement.setAttribute(ZOHO_API_ROW_NUMBER_ATTRIBUTE, "1");
		rootElement.appendChild(rowElement);

		Element data = null;
		boolean hasData = false;

		if (note.getLeadNote().getSubject() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Title");
			data.setTextContent(note.getLeadNote().getSubject());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (note.getLeadNote().getMessage() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Note Content");
			data.setTextContent(note.getLeadNote().getSubject());
			rowElement.appendChild(data);
			hasData = true;
		}

		data = document.createElement(ZOHO_API_XML_FIELD_TAG);
		data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "SEID");
		data.setTextContent(remoteLead.getCrmId());
		rowElement.appendChild(data);

		data = document.createElement(ZOHO_API_XML_FIELD_TAG);
		data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "SEMODULE");
		data.setTextContent("Leads");
		rowElement.appendChild(data);

		if (hasData == false)
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(baos);
		transformer.transform(source, result);

		return new String(baos.toByteArray(), "UTF-8");

	}

	public void deleteLeadNote(String zohoAuthToken, CrmLeadNoteDto note) {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			parameters.put("id", note.getCrmId());
			log.info("Calling Zoho updateNote with parameters" + parameters);

			String result = restTemplate.getForObject(
					ZOHO_API_DELETE_NOTE_RECORD_URL, String.class, parameters);
			log.info("Response to updateNote call from Zoho " + result);
			checkZohoResultForError(result);

		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	public void createLeadActivity(String zohoAuthToken, CrmLeadDto remoteLead,
			CrmLeadActivityDto remoteLeadActivityDto)
			throws CoreServiceException {

		try {

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			log.info("Calling Zoho createLead with parameters" + parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData",
					convertLeadActivityToZohoXml(remoteLead,
							remoteLeadActivityDto));
			log.info("Calling Zoho createLead with parameters" + map);

			String result = null;
			if (remoteLeadActivityDto.getLeadActivityType() == LeadActivityType.Phone) {

				result = restTemplate.postForObject(
						ZOHO_API_INSERT_CALL_RECORDS_URL, map, String.class,
						parameters);
			} else {
				result = restTemplate.postForObject(
						ZOHO_API_INSERT_EVENT_RECORDS_URL, map, String.class,
						parameters);
			}
			log.info("Response to createActivity call from Zoho " + result);
			checkZohoResultForError(result);
			updateActivityFromZohoResponse(result, remoteLeadActivityDto);
		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}

	}

	public void updateLeadActivity(String zohoAuthToken, CrmLeadDto remoteLead,
			CrmLeadActivityDto remoteLeadActivityDto)
			throws CoreServiceException {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			parameters.put("id", remoteLeadActivityDto.getCrmId());
			log.info("Calling Zoho updateActivity with parameters"
					+ parameters);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("xmlData",
					convertLeadActivityToZohoXml(remoteLead,
							remoteLeadActivityDto));
			log.info("Calling Zoho updateActivity with parameters" + map);

			String result = null;
			if (remoteLeadActivityDto.getLeadActivityType() == LeadActivityType.Phone) {
				result = restTemplate.postForObject(
						ZOHO_API_UPDATE_CALL_RECORDS_URL, map, String.class,
						parameters);
			} else {
				result = restTemplate.postForObject(
						ZOHO_API_UPDATE_EVENT_RECORDS_URL, map, String.class,
						parameters);
			}
			log.info("Response to updateActivity call from Zoho " + result);
			checkZohoResultForError(result);
			updateActivityFromZohoResponse(result, remoteLeadActivityDto);

		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}

	}

	private String convertLeadActivityToZohoXml(CrmLeadDto remoteLead,
			CrmLeadActivityDto activity) throws JAXBException,
			ParserConfigurationException, TransformerException,
			UnsupportedEncodingException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = null;
		if (activity.getLeadActivityType() == LeadActivityType.Phone) {
			rootElement = document.createElement(ZOHO_API_CALL_TAG);
		} else {
			rootElement = document.createElement(ZOHO_API_EVENT_TAG);
		}
		document.appendChild(rootElement);
		Element rowElement = document.createElement(ZOHO_API_ROW_TAG);
		rowElement.setAttribute(ZOHO_API_ROW_NUMBER_ATTRIBUTE, "1");
		rootElement.appendChild(rowElement);

		Element data = null;
		boolean hasData = false;

		if (activity.getSubjectActivity() != null) {
			data = document.createElement(ZOHO_API_XML_FIELD_TAG);
			data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Subject");
			data.setTextContent(activity.getSubjectActivity());
			rowElement.appendChild(data);
			hasData = true;
		}

		if (activity.getLeadActivityType() == LeadActivityType.Phone) {
			if (activity.getActivityDescription() != null) {
				data = document.createElement(ZOHO_API_XML_FIELD_TAG);
				data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Description");
				data.setTextContent(activity.getActivityDescription());
				rowElement.appendChild(data);
				hasData = true;
			}
		}

		if (activity.getLeadActivityType() == LeadActivityType.Meeting) {

			if (activity.getStartDate() != null) {
				data = document.createElement(ZOHO_API_XML_FIELD_TAG);
				data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME,
						"Start DateTime");
				data.setTextContent(getZohoFormatedDateStringFromDate(activity
						.getStartDate()));
				rowElement.appendChild(data);
				hasData = true;
			}

			if (activity.getEndDate() != null) {
				data = document.createElement(ZOHO_API_XML_FIELD_TAG);
				data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "End DateTime");
				data.setTextContent(getZohoFormatedDateStringFromDate(activity
						.getEndDate()));
				rowElement.appendChild(data);
				hasData = true;
			}

			if (activity.getVenue() != null) {
				data = document.createElement(ZOHO_API_XML_FIELD_TAG);
				data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "Venue");
				data.setTextContent(activity.getVenue());
				rowElement.appendChild(data);
				hasData = true;
			}
		}
		data = document.createElement(ZOHO_API_XML_FIELD_TAG);
		data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "SEID");
		data.setTextContent(remoteLead.getCrmId());
		rowElement.appendChild(data);

		data = document.createElement(ZOHO_API_XML_FIELD_TAG);
		data.setAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME, "SEMODULE");
		data.setTextContent("Leads");
		rowElement.appendChild(data);

		if (hasData == false)
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR_INVALID_DATA);

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(baos);
		transformer.transform(source, result);

		return new String(baos.toByteArray(), "UTF-8");

	}

	public void deleteLeadActivity(String zohoAuthToken,
			CrmLeadActivityDto activity) {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ZOHO_API_PARAMETER_AUTH_TOKEN, zohoAuthToken);
			parameters.put(ZOHO_API_PARAMETER_SCOPE, ZOHO_API_SCOPE_CRM_API);
			parameters.put("id", activity.getCrmId());
			log.info("Calling Zoho updateActivity with parameters"
					+ parameters);

			String result = null;
			if (activity.getLeadActivityType() == LeadActivityType.Phone) {
				result = restTemplate.getForObject(
						ZOHO_API_DELETE_CALL_RECORD_URL, String.class,
						parameters);
				log.info("Response to deleteActivity phone call from Zoho "
						+ result);
			} else if (activity.getLeadActivityType() == LeadActivityType.Meeting) {
				result = restTemplate.getForObject(
						ZOHO_API_DELETE_EVENT_RECORD_URL, String.class,
						parameters);
				log.info("Response to deleteActivity event call from Zoho "
						+ result);
			}
			checkZohoResultForError(result);

		} catch (Exception exception) {
			throw new CoreServiceException(
					CoreServiceErrorCode.CRM_ZOHO_API_ERROR, exception);
		}
	}

	private void updateActivityFromZohoResponse(String result,
			CrmLeadActivityDto note) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder
				.parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();
		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(ZOHO_API_XPATH_FIELD_SET);

		NodeList rowData = (NodeList) expr
				.evaluate(doc, XPathConstants.NODESET);
		if (rowData.getLength() == 0)
			return;

		for (int i = 0; i < rowData.getLength(); i++) {
			Element element = (Element) rowData.item(i);
			String val = element.getAttribute(ZOHO_API_FIELD_ATTRIBUTE_NAME);
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_ID_VAL)) {
				note.setCrmId(element.getTextContent());

			}
			if (StringUtils.equalsIgnoreCase(val, ZOHO_API_MODIFIED_TIME_VAL)) {
				note.setCrmLastUpdateTimestamp(getDateFromZohoFormatedString(element
						.getTextContent()));
			}
		}
	}

}
