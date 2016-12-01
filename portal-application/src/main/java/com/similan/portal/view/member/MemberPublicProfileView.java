package com.similan.portal.view.member;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.lead.ContactMessageDetail;
import com.similan.domain.entity.lead.ContactMessageType;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.service.MemberService;
import com.similan.portal.service.security.SecurityUtils;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.api.wall.dto.basic.WallDto;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.basic.WallPostDto;
import com.similan.service.api.wall.dto.key.WallKey;
import com.similan.service.api.wall.dto.operation.NewWallPostDto;

@Scope("request")
@Component("memberPublicView")
@Slf4j
public class MemberPublicProfileView extends BaseView {

	private static final long serialVersionUID = 1L;

	private boolean isSameMember = false;

	private MemberInfoDto member;
	
	private MemberInfoDto loggedInMember;
	
	private ContactMessageDetail message = new ContactMessageDetail();
	
	private MapModel simpleModel;
	
    private Double mapCenterLat = 37.702387;
    
    private Double mapCenterLong = 238.406370;
    
    private WallDto<SocialActorKey> memberNetworkWall = null;
    
	private List<WallEntryDto<SocialActorKey>> wallEntries = null;
	
	private String shareComment = StringUtils.EMPTY;
	
	private String postContent = StringUtils.EMPTY;
	
	@PostConstruct
	public void init() {
		//TODO Inject the request para automatically
		String memberId = JsfUtils.getContextParam("mid");
		log.info("Member's public profile  id passed " + memberId);
		
		String loggedInMemberId = String.valueOf(SecurityUtils.getRequiredCurrentUserId());
		log.info("Getting logged in member with id " + loggedInMemberId);
		
		if (memberId == null) {
			memberId = loggedInMemberId;
			log.info("Viewer and viewing member are same (viewing member profile ) " + memberId);
		}
		
		if(memberId.equalsIgnoreCase(loggedInMemberId)){
			this.isSameMember = true;
			log.info("Viewer and viewing member are same " + memberId);
		}
		
		try {
			
			loggedInMember = memberService.getMemberById(Long.valueOf(loggedInMemberId));
			
			Long id = Long.valueOf(memberId);			
			member = memberService.viewMemberPublicProfile(id, 
					this.getMemberKey(loggedInMember));
			log.info("Got member's public profile with id " + member.getId());

			/* populate the map */
			if(member.getAddressDto() != null){
				this.generateMapModel();
			}
			
			/*
			if (this.isSameMember != true) {
				this.memberService.createClickThroughLeadForMember(loggedInMember,
						member);
			}*/
			
			/* get the wall */
			this.populatewallUpdates();
			
		} catch (Exception exp) {
			log.error("Cannot get member with id " + memberId, exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
							"Invalid member " + exp.getMessage()));
		}
	}
	
	public boolean isSameMemberLoggedIn(){
		return this.isSameMember;
	}
	
	private void populatewallUpdates(){
		log.info("Populating wall updates of member ...");
		SocialActorKey socialActorKey = this.getMemberKey(member);
		WallKey<SocialActorKey> wallKey = new WallKey<SocialActorKey>(socialActorKey);
		memberNetworkWall = this.getWallService().get(wallKey);
		wallEntries = this.getWallService().getLatest(wallKey);
	}
	
    public WallDto<SocialActorKey> getMemberNetworkWall() {
		return memberNetworkWall;
	}

	public void setMemberNetworkWall(WallDto<SocialActorKey> memberNetworkWall) {
		this.memberNetworkWall = memberNetworkWall;
	}

	public List<WallEntryDto<SocialActorKey>> getWallEntries() {
		return wallEntries;
	}

    public String getShareComment() {
		return shareComment;
	}

	public void setShareComment(String shareComment) {
		this.shareComment = shareComment;
	}
	
	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	
	public void post() {

	    try {
	      /* create a wall post */
	      NewWallPostDto newPost = new NewWallPostDto(
	          this.getMemberKey(member),
	          this.postContent);
	      this.getWallService().post(this.memberNetworkWall.getKey(), newPost);

	      /* also goes to feed */
	      wallEntries = this.getWallService()
	          .getLatest(this.memberNetworkWall.getKey());
	    } catch (Exception exp) {
	      log.error("Error", exp);
	      FacesContext.getCurrentInstance().addMessage(
	          null,
	          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
	              "Comment cannot be posted for " + exp.getMessage()));
	    }

	    this.postContent = StringUtils.EMPTY;
	  }

	public void shareCommentEveryOne(){
		
		try {
			/* create a wall post */
      NewWallPostDto newPost = new NewWallPostDto(this.getMemberKey(member),
          this.shareComment);
      WallPostDto<SocialActorKey> currentPost = this.getWallService().post(
          this.memberNetworkWall.getKey(), newPost);
      wallEntries.add(currentPost);
		}
		catch(Exception exp){
			log.error("Error", exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Comment cannot be posted for " + exp.getMessage()));
		}
		
		this.shareComment = StringUtils.EMPTY;
	}

	private void generateMapModel(){
    	
    	simpleModel = new DefaultMapModel();
		
		/* put the address */
		LatLng memberAddrCoord = new LatLng(member.getAddressDto().getLatitude(), 
				member.getAddressDto().getLongitude()); 
		
		this.mapCenterLat = member.getAddressDto().getLatitude();
		this.mapCenterLong = member.getAddressDto().getLongitude();
		
		simpleModel.addOverlay(new Marker(memberAddrCoord, 
				                   this.member.getFullName()));
		
	}
    
	public ContactMessageDetail getMessage() {
		return message;
	}

	public void setMessage(ContactMessageDetail message) {
		this.message = message;
	}

	public Double getMapCenterLat() {
		return mapCenterLat;
	}

	public void setMapCenterLat(Double mapCenterLat) {
		this.mapCenterLat = mapCenterLat;
	}

	public Double getMapCenterLong() {
		return mapCenterLong;
	}

	public void setMapCenterLong(Double mapCenterLong) {
		this.mapCenterLong = mapCenterLong;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public String getPublicPhoto() {
		return getMember().getPhotoLocation();
	}
	
	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}
	
	public void follow(){
		log.info("Follower member " + this.loggedInMember.getFirstName() + 
				                  " followed member " + this.member.getFirstName());
		if(this.loggedInMember.getId().compareTo(this.member.getId()) == 0){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
							"Cannot follow yourself"));
			return;
		}
		
		//TODO : Proceed with follow logic
	}
	
	public void share(){
		log.info("Share");
		if(this.loggedInMember.getId().compareTo(this.member.getId()) == 0){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
							"Cannot share with yourself"));
			return;
		}
		
		//TODO : Proceed with share logic
		
	}
	
	public void sendMessage(){
		
		log.info("Send message");
		if(this.loggedInMember.getId().compareTo(this.member.getId()) == 0){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
							"Cannot send message to yourself"));
			return;
		}
		
		if(StringUtils.isEmpty(this.member.getEmail())){
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Failure",
							"Cannot send message since receiver's email address is invalid"));
			return;
		}
		
		//Proceed with email
		this.message.setContactMessageType(ContactMessageType.EmailMessage);
		try {
			this.memberService.sendContactMessageToMember(message, this.loggedInMember, this.member);
	        FacesContext.getCurrentInstance().addMessage(null, 
	               new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", 
	               "Thanks. Your message has been successfully sent"));
		}
		catch(Exception exp){
			log.error("Cannot send message", exp);
			FacesContext.getCurrentInstance().addMessage(null, 
		               new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failure", 
		               "Cannot send message" + exp.getMessage()));
		}
		
	}

	private void readObject(java.io.ObjectInputStream in)
		      throws IOException, ClassNotFoundException {
			in.defaultReadObject();
			this.memberService = (MemberService) JsfUtils.findBackingBean("memberService");
	}
	
}
