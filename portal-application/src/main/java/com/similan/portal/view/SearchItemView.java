package com.similan.portal.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.ContactMessageType;
import com.similan.domain.entity.metadata.StateType;
import com.similan.domain.repository.global.GlobalRepository.SearchTargetType;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.DomainSearchResult;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.SearchItemType;
import com.similan.framework.dto.SearchResultType;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.dto.partner.PartnerSearchSettingsConfigurationDto;
import com.similan.portal.databean.CountryBean;
import com.similan.portal.view.member.MemberView;

@Component("searchItemView")
@Scope("session")
@Slf4j
public class SearchItemView extends BaseView {

	private static final long serialVersionUID = -2346661016735490199L;
	
	private List<String> selectedSearchType;

	private Map<String, String> searchTypes;
	
	private String searchText;

	@Autowired(required = false)
	private MemberInfoDto selectedMember = null;
	
	private MemberInfoDto toMember = null;
	
	private OrganizationDetailInfoDto selectedOrgInfo = null;
	
	private DomainSearchResultSet searchResult;
	
	private Marker selectMarker;
	
	private AdvancedSearchInput advSearchInput = new AdvancedSearchInput();
	
	private ContactMessageDetail message = new ContactMessageDetail();
	
	private List<StateType> selectedStateList = null;

	private boolean embedded = false;

	private String mid;
	
	private String orgEmbeddedIdentity = StringUtils.EMPTY;
	
	private Long orgId = Long.MIN_VALUE;

	@Autowired(required = false)
	private OrganizationDetailInfoDto orgInfo;

	private boolean inCommunityPartnerVisibility = true;
	private boolean embeddedPartnerVisibility = true;

	private String searchType = "NORMAL";
	
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return
	 */
	public Long getOrgId() {
		Long retOrgId = Long.MIN_VALUE;
		
		if(this.orgId != Long.MIN_VALUE){
			retOrgId = this.orgId;
		}
		else if (getSelectedMember()
                   .getEmployer() != null){
			retOrgId = getSelectedMember()
					         .getEmployer()
					         .getId();
		}
		
		log.info("Org id fetched from request context " + retOrgId);
		return retOrgId;
	}

	public OrganizationDetailInfoDto getSelectedOrgInfo() {
		return selectedOrgInfo;
	}

	public void setSelectedOrgInfo(OrganizationDetailInfoDto selectedOrgInfo) {
		log.info("selected org " + selectedOrgInfo.getFullName() + " id " + selectedOrgInfo.getId());
		this.selectedOrgInfo = selectedOrgInfo;
	}

	public MemberInfoDto getSelectedMember() {
		if (this.selectedMember == null) {
			if (this.mid == null) {
				this.mid = getContextParam("mid");
			}
			
			if (this.mid == null) {
				log.warn("mid is null, no member can be retrieved");
				return null;
			}
			
		}
		return selectedMember;
	}
	
	public String getOrgEmbeddedIdentity() {
		return orgEmbeddedIdentity;
	}

	public void setOrgEmbeddedIdentity(String orgEmbeddedIdentity) {
		this.orgEmbeddedIdentity = orgEmbeddedIdentity;
	}

	public MemberInfoDto getToMember() {
		return toMember;
	}

	public void setToMember(MemberInfoDto toMember) {
		this.toMember = toMember;
	}

	public void setSelectedMember(MemberInfoDto selectedMember) {
		this.selectedMember = selectedMember;
	}
	
	public ContactMessageDetail getMessage() {
		return message;
	}

	public void setMessage(ContactMessageDetail message) {
		this.message = message;
	}

	public List<StateType> getSelectedStateList() {
		return selectedStateList;
	}

	public void setSelectedStateList(List<StateType> selectedStateList) {
		this.selectedStateList = selectedStateList;
	}

	public AdvancedSearchInput getAdvSearchInput() {
		return advSearchInput;
	}

	public void setAdvSearchInput(AdvancedSearchInput advSearchInput) {
		this.advSearchInput = advSearchInput;
	}

	public void setSearchTypes(Map<String, String> searchTypes) {
		this.searchTypes = searchTypes;
	}

	public Marker getSelectMarker() {
		return selectMarker;
	}

	public void setSelectMarker(Marker selectMarker) {
		this.selectMarker = selectMarker;
	}

	public DomainSearchResultSet getSearchResult() {
		return searchResult;
	}
	
	public void setSearchResult(DomainSearchResultSet searchResult) {
		this.searchResult = searchResult;
	}

	@PostConstruct
	public void init() {
		/*
		 * For now it is hard coded and in a very ugly way but later should be managed 
		 * by string resource (Fix later)
		 * */
		this.searchTypes = new HashMap<String, String>();
		
		/* pre-load the states based on default Advanced */
		CountryBean countryBean = (CountryBean)this.findBackingBean("countryBean");
		this.selectedStateList = countryBean.getStateTypeList(this.advSearchInput.getCountry());
		
				
		for (SearchTargetType searchItemType : SearchTargetType.values()) {
			if (searchItemType.equals(SearchTargetType.SOCIAL_ORGANIZATION_ANY) == true 
					|| searchItemType.equals(SearchTargetType.SOCIAL_ORGANIZATION_SUPPLIER) == true
					|| searchItemType.equals(SearchTargetType.SOCIAL_ORGANIZATION_RESELLER) == true
					|| searchItemType.equals(SearchTargetType.SOCIAL_ORGANIZATION_DISTRIBUTOR) == true) {
				
				searchTypes.put(searchItemType.getTypeName(), searchItemType.name());
			}
		}
		
		//case for embedded search
		this.orgEmbeddedIdentity = getContextParam("oeid");
		checkEmbedded();
		
		//case for in-community partner search
		String orgParamId = this.getContextParam("oid");
		if(orgParamId != null){
			this.orgId = Long.valueOf(orgParamId);
		}
		
		this.mid = getContextParam("mid");
		log.info("Member id " + this.mid + " embedded " 
		             + this.embedded + " embedded org id " 
				     + this.orgEmbeddedIdentity);
	}

	private void checkEmbedded() {
		if (isEmbedded()) {
			searchTypes.clear();
			searchTypes.put("Reseller",
					SearchTargetType.SOCIAL_ORGANIZATION_RESELLER.name());
			searchTypes.put("Distributor",
					SearchTargetType.SOCIAL_ORGANIZATION_DISTRIBUTOR.name());
			this.advSearchInput.getSelectedSearchType().add(
					SearchTargetType.SOCIAL_ORGANIZATION_RESELLER.name());
		}
	}

	public void viewProfile(DomainSearchResult result){
		log.info("Going to view profile of search item " + result.getSearchItemType() + " " 
	                          + result.getId());
		if(result.getSearchItemType().equals(SearchResultType.Person) == true){
			facesHelper.redirect("memberExternalProfile.xhtml");
		}
		else {
			facesHelper.redirect("businessExternalProfile.xhtml");
		}
	}
	
	public void sendMessage(DomainSearchResult result){
		log.info("Going to send message to search item " + result.getSearchItemType() + " " 
	                          + result.getId());
	}

	public void sendConnectMessage(DomainSearchResult result){
		log.info("Going to send connect message to search item " + result.getSearchItemType() + " " 
	                          + result.getId());
	}
	
	public String search(){
		setSearchType("NORMAL");
		return "result";
	}

	public MapModel getSimpleModel(){
		MapModel model = null;
		
		try {
			model = this.searchResult.getSimpleModel();
			
		}
		catch(Exception exp){
			log.error("Failure to render map view ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Failure to render map view " + exp.getMessage())); 
			
		}
		
		return model;
	}
	
	public void contact(){
		log.info("Contact the person");
	}
	
	public void follow(){
		log.info("Follow the person");
	}
	
	public void connect(){
		log.info("Connect to the person");
	}

	public Map<String, String> getSearchTypes() {
		return searchTypes;
	}

	public List<String> getSelectedSearchType() {
		return selectedSearchType;
	}

	public void setSelectedSearchType(List<String> selectedSearchType) {
		this.selectedSearchType = selectedSearchType;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public void onMarkerSelect(OverlaySelectEvent event) {  
        this.selectMarker = (Marker) event.getOverlay();
    }
	
	public void sendMessageToOrganizationFromMap(String orgId){
		
		Long id = Long.valueOf(orgId);
		log.info("Sending message to " + id 
				+ " message " + message.getMessage());
		
		try {
			
			OrganizationDetailInfoDto orgToSend = this.getOrgService().getOrganization(id);
			MemberView memView = (MemberView)this.findBackingBean("memberView");
			this.memberService.sendContactMessageToOrganization(this.message, memView.getMember(), orgToSend);
		}
		catch(Exception exp){
			log.error("Cannot send message to org", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
							"Error sending message " + exp.getMessage())); 
		}
		
	}

	public String businessAssociateSearch(){
	
		log.info("Performing business associate search " + this.advSearchInput.getCountry() 
			     + " state " + this.advSearchInput.getState()
			     + " radius " + this.advSearchInput.getRadius() 
			     + " embedded  search " + this.embedded);
		
		PartnerSearchSettingsConfigurationDto partnerSearchSettings = null;
		
		try {
			Long orgId = Long.MIN_VALUE;
			if(this.embedded == true){
				log.info("Org embedded identity " + this.orgEmbeddedIdentity);
				orgId = this.orgService.getOrgIdFromEmbeddedId(this.orgEmbeddedIdentity);
			}
			else {
				orgId = getOrgId();
			}
			
			/* get the org Id and partner search settings */
			partnerSearchSettings = this.getOrgService()
					.getPartnerSearchSettings(orgId);
			if(partnerSearchSettings != null){
				embeddedPartnerVisibility = partnerSearchSettings.getEmbeddedSearchConfigEnabled();	
				inCommunityPartnerVisibility = partnerSearchSettings.getInCommunitySearchConfigEnabled();
			}
			
			String searchFilterType = this.getContextParam("searchType");			
			if(searchFilterType != null){
				this.advSearchInput.getSelectedSearchType().add(searchFilterType);
			}
			setSearchType("BUSINESS");
			return "result";
		}
		catch(Exception exp){
			log.info("Error searching ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Error searching " + exp.getMessage()));
		}
		return null;
	}

	public void doSearch() {
		try {
		  if ("BUSINESS".equals(this.getSearchType())) {
				log.info("Performing business associate search "
						+ this.advSearchInput.getCountry() + " state "
						+ this.advSearchInput.getState() + " radius "
						+ this.advSearchInput.getRadius());
				Long orgId = Long.MIN_VALUE;
				if(this.embedded == true){
					orgId = this.orgService.getOrgIdFromEmbeddedId(this.orgEmbeddedIdentity);
				}
				else {
					orgId = getOrgId();
				}
				
				this.searchResult = this.orgService.businessAssociateSearch(
						orgId, this.advSearchInput);
				return;
			}
			if ("ADVANCED".equals(getSearchType())) {
				log.info("Performing advanced search " + this.advSearchInput.getCountry() 
					     + " state " + this.advSearchInput.getState()
					     + " radius " + this.advSearchInput.getRadius());
			
				this.searchResult = memberService.advancedSearch(this.selectedMember, this.advSearchInput);
				return;
			}
			
			//NORMAL, default case
			log.info(" Search string " + searchText + " search options " + selectedSearchType);

			MemberInfoDto searchInfo = this.selectedMember;
			searchInfo.setSearch(searchText);
			SearchItemType searchItem = new SearchItemType();
			searchItem.setSelectedSearchType(selectedSearchType);

			this.searchResult = memberService.memberSearch(searchInfo,
					searchItem, isEmbedded());
		} catch (Exception e) {
			log.info("Error searching ", e);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Error searching " + e.getMessage())); 
		}
	}
	
	public String advancedSearch(){
		this.searchType = "ADVANCED";
		return "result";
	}
	
	public void loadSelectedStates(){
		CountryBean countryBean = (CountryBean)this.findBackingBean("countryBean");
		this.selectedStateList = countryBean.getStateTypeList(this.advSearchInput.getCountry());
	}
	
	public void selectMember(MemberInfoDto selectedMember){
		log.info("Member id selected " + selectedMember.getId());
		this.setSelectedMember(selectedMember);
	}
	
	public void sendMessageToMember(){
		
		try {
			
			if(this.toMember == null || this.selectedMember == null){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since receiver is not valid ")); 
				return;
			}
			
			if(StringUtils.isEmpty(this.selectedMember.getEmail())){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since sender does not have a configured email ")); 
				return;
			}
			
			
			if(StringUtils.isEmpty(this.toMember.getEmail())){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since receiver does not have a configured email ")); 
				return;
			}
			
			if(this.selectedMember.getEmail().equalsIgnoreCase(this.toMember.getEmail())){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message to yourself ")); 
				return;
			}
			
			log.info("The message to be sent " + this.message.getMessage() + 
					" to member email " + this.toMember.getEmail()
					+ " from member " + this.selectedMember.getEmail());
			
			this.message.setContactMessageType(ContactMessageType.EmailMessage);
			this.memberService.sendContactMessageToMember(this.message, this.selectedMember, 
					this.toMember);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
                    "Thanks. Your message has been successfully sent")); 
		}
		catch(Exception exp){
			log.error("Error in sending message ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Error in sending message " + exp.getMessage())); 
		}
		
		//setting to null again
		this.toMember = null;
	}
	
	public void selectOrganization(OrganizationDetailInfoDto orgInfo){
		log.info("Org id selected " + orgInfo);
		this.selectedOrgInfo = orgInfo;
	}
	
	public void sendMessageToOrganization(){
		
		try {
			
			if(this.selectedOrgInfo == null){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since receiver is not valid ")); 
				return;
			}

			log.info("The message to be sent " + this.message.getMessage() 
					+ " to business " + this.selectedOrgInfo.getId());

			if(StringUtils.isEmpty(this.selectedMember.getEmail())){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since sender does not have a configured email ")); 
				return;
			}
			
			if(StringUtils.isEmpty(this.selectedOrgInfo.getEmail())){
				FacesContext.getCurrentInstance().addMessage(null, 
	                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Failure", 
	                    "Cannot send a message since receiver does not have a configured email ")); 
				return;
			}
			
			this.message.setContactMessageType(ContactMessageType.EmailMessage);
			this.memberService.sendContactMessageToOrganization(message, this.selectedMember, this.selectedOrgInfo);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
                    "Thanks. Your message has been successfully sent"));
			
		}
		catch(Exception exp){
			
			log.error("Error in sending message ", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
                    "Error in sending message " + exp.getMessage())); 
		}
		
		//make the selected org null again
		this.selectedOrgInfo = null;
	}

	/**
	 * @return the embedded
	 */
	public boolean isEmbedded() {
		return embedded;
	}

	/**
	 * @param embedded the embedded to set
	 */
	public void setEmbedded(boolean embedded) {
		log.info("Setting embedded search flag to true");
		this.embedded = embedded;
		checkEmbedded();
	}

	public void embeddedSearch() {
		setEmbedded(true);
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/**
	 * @return the embeddedPartnerVisibility
	 */
	public boolean isEmbeddedPartnerVisibility() {
		return embeddedPartnerVisibility;
	}

	/**
	 * @return the inCommunityPartnerVisibility
	 */
	public boolean isInCommunityPartnerVisibility() {
		return inCommunityPartnerVisibility;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the orgInfo
	 */
	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	/**
	 * @param orgInfo the orgInfo to set
	 */
	public void setOrgInfo(OrganizationDetailInfoDto orgInfo) {
		this.orgInfo = orgInfo;
	}

	public boolean showMap() {
		if (searchResult == null)
			return false;
		if (searchResult.getSearchResultList() == null)
			return false;

		boolean hasAnAddressInResults = false;
		for (DomainSearchResult result : searchResult.getSearchResultList()) {
			if (result == null)
				continue;
			if (result.getMemberInfo() != null)
				if (result.getMemberInfo().getAddressDto() != null)
					if (!StringUtils.isEmpty(result.getMemberInfo()
							.getAddressDto().getCity())
							&& (result.getMemberInfo().getAddressDto()
									.getLongitude() != null
							|| result.getMemberInfo().getAddressDto()
									.getLongitude() != Double.MIN_VALUE))
						hasAnAddressInResults = true;
			if (result.getOrgInfo() != null)
				if (result.getOrgInfo().getLocations() != null
						&& result.getOrgInfo().getLocations().size() > 0)
					if (!StringUtils.isEmpty(result.getOrgInfo().getLocations()
							.get(0).getCity())
							&& result.getOrgInfo().getLocations().get(0)
									.getLatitude() != Double.MIN_VALUE)
						hasAnAddressInResults = true;
		}
		return hasAnAddressInResults;

	}
}
