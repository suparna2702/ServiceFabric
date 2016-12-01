package com.similan.portal.view.member;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("memberInviteInputView")
@Slf4j
public class MemberInviteInputView extends BaseView {

	private static final long serialVersionUID = 1L;


	private String pid = null;

	private String memberId = null;
	private MemberInfoDto member = null;

	@PostConstruct
	public void init() {
		memberId = this.getContextParam("mid");
		pid = getContextParam("pid");
		try {
			this.member = this.memberService.getMemberById(Long
					.valueOf(memberId));
		} catch (Exception e) {
		}
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public void setMember(MemberInfoDto member) {
		this.member = member;
	}

	public void memberInviteSubmit() {

		try {
			this.memberService.memberInviteSignupInput(pid, member);
			facesHelper.redirect("/member/memberInviteSignupComplete.xhtml");

		} catch (Exception exp) {
			log.info("Error signing up user ", exp);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"Error completing signup" + exp.getMessage()));
		}
	}
}
