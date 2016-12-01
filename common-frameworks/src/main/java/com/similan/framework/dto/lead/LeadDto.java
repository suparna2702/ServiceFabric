package com.similan.framework.dto.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.LeadStatusType;
import com.similan.domain.entity.lead.LeadType;
import com.similan.domain.entity.lead.TransferState;
import com.similan.domain.entity.util.AddressDto;
import com.similan.framework.dto.leadcapture.LeadCaptureResponseDto;

public class LeadDto implements Serializable {

	private static final long serialVersionUID = 3469581707927215378L;

	private Long id;
	
	private String firstName;
	
	private String name;
	
	private String description = StringUtils.EMPTY;
	 
	private String contactPhone;
	
	private String contactEmail;
	
	private String company;
	
	private AddressDto location;
	
	private String lastName;
	
	private String keyword;
	
	private String leadPhotoLocation;
	
	private Boolean viewed = Boolean.FALSE;
	
	private LeadType leadType;
	
	private Long forSocialActorId;
	
	private Date timeStamp;
	
	private LeadStatusType leadStatus = LeadStatusType.Unassigned;
	
	private String industry;
	
	private String sicCode;
    
	private ContactMessageDetail contact;
    
    private Boolean locationVerified;
    
    private Boolean phoneVerified;
    
    private Boolean nameVerified;
    
    private Boolean intentVerified;
    
    private List<LeadNoteDto> noteList = new ArrayList<LeadNoteDto>();
    
    private List<LeadMessageDto> messageList = new ArrayList<LeadMessageDto>();
    
    private List<LeadActivityDto> activityList = new ArrayList<LeadActivityDto>();
    
    private float purchasePrice = 1.00f;
    
    private Long fromSocialActorId;
    
    private TransferState transferState = TransferState.None;
    
    private String leadSource = StringUtils.EMPTY;
    
    private LeadCaptureResponseDto response;
    
    private String title = StringUtils.EMPTY;
    
    private List<LeadTransferStatusDto> LeadTransferStatusList = new ArrayList<LeadTransferStatusDto>();

	private Long createdById;

	private Date dateCreated;

	private Date dateLastViewed;
	
    public LeadDto() {
    	this.location = new AddressDto();
    	transferState = TransferState.None;
    	locationVerified = false;
    	phoneVerified = false;
    	intentVerified = false;
    }
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LeadCaptureResponseDto getResponse() {
		return response;
	}

	public void setResponse(LeadCaptureResponseDto response) {
		this.response = response;
	}

	public TransferState getTransferState() {
		return transferState;
	}

	public void setTransferState(TransferState transferState) {
		this.transferState = transferState;
	}

	public Long getFromSocialActorId() {
		return fromSocialActorId;
	}

	public void setFromSocialActorId(Long fromSocialActorId) {
		this.fromSocialActorId = fromSocialActorId;
	}

	public float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public AddressDto getLocation() {
		return location;
	}

	public void setLocation(AddressDto location) {
		this.location = location;
	}

	public Boolean getViewed() {
		return viewed;
	}

	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}

	public LeadStatusType getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(LeadStatusType leadStatus) {
		this.leadStatus = leadStatus;
	}

	public List<LeadActivityDto> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<LeadActivityDto> activityList) {
		this.activityList = activityList;
	}

	public List<LeadNoteDto> getNoteList() {
		return noteList;
	}

	public void setNoteList(List<LeadNoteDto> noteList) {
		this.noteList = noteList;
	}

    public void setLeadTypeStr(String leadTypeStr){
    	//do nothing for now
    }
    
    public String getLeadPhoto() {
    	return PhotoViewOption.ShowPhoto.effectivePhoto("/images/memberDefaultPhoto.png", 
    			this.leadPhotoLocation);
	}

	public void setLeadPhoto(String leadPhoto) {
		this.leadPhotoLocation = leadPhoto;
	}
    
   	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeadType getLeadType() {
		return leadType;
	}

	public void setLeadType(LeadType leadType) {
		this.leadType = leadType;
	}

	public ContactMessageDetail getContact() {
		return contact;
	}

	public void setContact(ContactMessageDetail contact) {
		this.contact = contact;
	}
	
	public List<LeadMessageDto> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<LeadMessageDto> messageList) {
		this.messageList = messageList;
	}
	
	public Boolean getLocationVerified() {
		return locationVerified;
	}

	public void setLocationVerified(Boolean locationVerified) {
		this.locationVerified = locationVerified;
	}

	public Boolean getPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(Boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public Boolean getNameVerified() {
		return nameVerified;
	}

	public void setNameVerified(Boolean nameVerified) {
		this.nameVerified = nameVerified;
	}

	public Boolean getIntentVerified() {
		return intentVerified;
	}

	public void setIntentVerified(Boolean intentVerified) {
		this.intentVerified = intentVerified;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLeadPhotoLocation() {
		return leadPhotoLocation;
	}

	public void setLeadPhotoLocation(String leadPhotoLocation) {
		this.leadPhotoLocation = leadPhotoLocation;
	}
	
	public Long getForSocialActorId() {
		return forSocialActorId;
	}

	public void setForSocialActorId(Long forSocialActorId) {
		this.forSocialActorId = forSocialActorId;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSicCode() {
		return sicCode;
	}

	public void setSicCode(String sicCode) {
		this.sicCode = sicCode;
	}
	
	public String getLeadSource(){
		return this.leadSource;
	}
	
	public void setLeadSource(String leadSource){
		this.leadSource = leadSource;
	}
	
	public String getLeadTypeStr(){
		if(this.leadType != null){
			return this.leadType.toString();
		}
		
		return LeadType.Unknown.toString();
	}

	@Override
	public String toString() {
		return "LeadDto [id=" + id + ", firstName=" + firstName + ", name="
				+ name + ", contactPhone=" + contactPhone + ", contactEmail="
				+ contactEmail + ", company=" + company + ", location="
				+ location + ", lastName=" + lastName + ", keyword=" + keyword
				+ ", leadPhotoLocation=" + leadPhotoLocation + ", viewed="
				+ viewed + ", leadType=" + leadType + ", forSocialActorId="
				+ forSocialActorId + ", timeStamp=" + timeStamp
				+ ", leadStatus=" + leadStatus + ", industry=" + industry
				+ ", sicCode=" + sicCode + ", contact=" + contact
				+ ", locationVerified=" + locationVerified + ", phoneVerified="
				+ phoneVerified + ", nameVerified=" + nameVerified
				+ ", intentVerified=" + intentVerified + ", noteList="
				+ noteList + ", messageList=" + messageList + ", activityList="
				+ activityList + ", purchasePrice=" + purchasePrice
				+ ", fromSocialActorId=" + fromSocialActorId
				+ ", transferState=" + transferState + ", leadSource="
				+ leadSource + "]";
	}

	public List<LeadTransferStatusDto> getLeadTransferStatusList() {
		return LeadTransferStatusList;
	}

	public void setLeadTransferStatusList(List<LeadTransferStatusDto> leadTransferStatusList) {
		LeadTransferStatusList = leadTransferStatusList;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastViewed() {
		return dateLastViewed;
	}

	public void setDateLastViewed(Date dateLastViewed) {
		this.dateLastViewed = dateLastViewed;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}
	
	

	
}
