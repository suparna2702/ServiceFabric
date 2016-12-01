package com.similan.portal.view;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.util.NonmemberContactMessageType;
import com.similan.framework.dto.NonmemberContactMessageInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.util.EmailValidator;
import com.similan.service.exception.CoreServiceException;

@Scope("request")
@Component("homeView")
@Slf4j
public class HomeView extends BaseView {

	private static final long serialVersionUID = -5340310986321575551L;

	@Autowired(required = false)
	private MemberInfoDto memberInfo = new MemberInfoDto();

	private NonmemberContactMessageInfoDto contactMessage = new NonmemberContactMessageInfoDto();

	private String errorMsg = StringUtils.EMPTY;

	private String firstName = StringUtils.EMPTY;

	private String lastName = StringUtils.EMPTY;

	private String email = StringUtils.EMPTY;

	private String business = StringUtils.EMPTY;

	@PostConstruct
	public void init() {

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public NonmemberContactMessageInfoDto getContactMessage() {
		return contactMessage;
	}

	public void setContactMessage(NonmemberContactMessageInfoDto contactMessage) {
		this.contactMessage = contactMessage;
	}

	public MemberInfoDto getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfoDto memberInfo) {
		this.memberInfo = memberInfo;
	}

	/**
	 * Home page navigation
	 */
	public void homeUrl() {

		String retUrl = "home.xhtml";
		if (memberInfo != null) {
			retUrl = "/member/memberHome.xhtml";
		}
		log.info("Home view url " + retUrl);
		facesHelper.redirect(retUrl);
	}

	/**
	 * Initiates the workflow for member sign up
	 * 
	 * @return
	 */
	public String join() {
		try {

			log.debug("Joining member name " + this.memberInfo.getFirstName());
			if (this.memberInfo.getEmail() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Failure", "Invalid email. Cannot continue signup process"));
				return "result";
			} else {
				if (EmailValidator.validate(this.memberInfo.getEmail()) != true) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Failure", "Invalid email. Cannot continue signup process"));
					return "result";
				}
			}

			this.memberService.memberSignupInitiate(this.memberInfo);
			errorMsg = "Success";

		} catch (CoreServiceException exp) {
			log.error("Membership creation failed ", exp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
					"Membership creation failed due to " + exp.getMessage()));
			this.errorMsg = "Failure";

		} catch (Exception exp) {
			log.error("Membership creation failed ", exp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
					"Membership creation failed due to " + exp.getMessage()));
			this.errorMsg = "Failure";
		}

		return "result";

	}

	public void requestToJoin() {
		try {

		} catch (Exception exp) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
					"Error sending message due to " + exp.getMessage()));
		}
		
		this.requestMoreInfo();
	}

	public void requestMoreInfo() {

		try {

			StringBuilder moreInfoMessageBuilder = new StringBuilder().append("First Name : ").append(this.firstName)
					.append(" Last Name : ").append(this.lastName).append(" Email : ").append(this.email)
					.append(" Business Name : ").append(this.business).append(" requested for more info");

			log.debug("More info message " + moreInfoMessageBuilder.toString());

			NonmemberContactMessageInfoDto moreInfoMessage = new NonmemberContactMessageInfoDto();
			moreInfoMessage.setFirstName(this.firstName);
			moreInfoMessage.setLastName(this.lastName);
			moreInfoMessage.setEmail(this.email);
			moreInfoMessage.setBusiness(this.business);
			moreInfoMessage.setMessageType(NonmemberContactMessageType.MoreInfoRequest);

			this.memberService.contactUs(moreInfoMessage);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",
					"Thanks for your interest. We will get back to you soon"));

		} catch (Exception exp) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
					"Error sending message due to " + exp.getMessage()));
		}

		this.firstName = StringUtils.EMPTY;
		this.lastName = StringUtils.EMPTY;
		this.email = StringUtils.EMPTY;
		this.business = StringUtils.EMPTY;
	}

	/**
	 * @return
	 */
	public void contactUs() {

		try {

			log.debug("Contact message " + this.contactMessage.getComment());

			this.contactMessage.setMessageType(NonmemberContactMessageType.ContactRequest);
			this.memberService.contactUs(this.contactMessage);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", "Thanks. Somebody will soon contact you"));
		} catch (Exception exp) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure",
					"Error sending message due to " + exp.getMessage()));
		}

	}

	public void deneme(String x) {
		System.out.println("x:" + x);
	}

}
