package com.similan.portal.view.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.acccount.BusinessAccountType;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.embed.EmbeddedUrlDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.community.dto.basic.BusinessAccountSubscriptionType;
import com.similan.service.api.community.dto.key.SocialActorKey;

@Scope("view")
@Component("businessAccountView")
@Slf4j
public class BusinessAccountView extends BaseView {

  private static final long serialVersionUID = 1L;


  @Autowired
  private MemberInfoDto memberInfo;

  @Autowired
  private OrganizationDetailInfoDto orgInfo;

  private List<EmbeddedUrlDto> embeddedUrlList = new ArrayList<EmbeddedUrlDto>();

  private Map<String, String> embeddedUrlMap;

  private List<BusinessAccountType> accountTypeList;

  private BusinessAccountType currentAccount;

  private String upgradeMessage = StringUtils.EMPTY;

  private SocialActorKey businessKey = null;

  @PostConstruct
  public void init() {

    businessKey = this.getSocialActorKey(orgInfo.getId());
    accountTypeList = this.orgService.getAllBusinessAccountTypes();
    currentAccount = this.orgService.getBusinessAccount(orgInfo.getId());
    embeddedUrlMap = this.getEmbeddedUrlMap();

    if (embeddedUrlMap != null) {
      Set<String> urlKeySet = embeddedUrlMap.keySet();
      for (String urlKey : urlKeySet) {

        EmbeddedUrlDto urlDto = new EmbeddedUrlDto();
        urlDto.setUrlTag(urlKey);
        String urlValue = embeddedUrlMap.get(urlKey);
        String fullUrl = this.getPlatformCommonSettings()
            .getPortalApplcationUrl().getValue()
            + urlValue;
        log.info("URL Key " + urlKey + " URL Value " + fullUrl);

        urlDto.setFullUrl(fullUrl);
        embeddedUrlList.add(urlDto);
      }
    }

  }

  public SocialActorKey getBusinessKey() {
    return businessKey;
  }

  public String getAccountName() {
    String accountName = currentAccount.getName();
    if (accountName
        .contentEquals(BusinessAccountSubscriptionType.BUSINESS_STANDARD)) {
      return "Business Standard";
    }

    if (accountName
        .contentEquals(BusinessAccountSubscriptionType.BUSINESS_PLUS)) {
      return "Business Plus";
    }

    if (accountName
        .contentEquals(BusinessAccountSubscriptionType.BUSINESS_ENTERPRISE)) {
      return "Business Enterprise";
    }

    return StringUtils.EMPTY;
  }

  public String getUpgradeMessage() {
    return upgradeMessage;
  }

  public void setUpgradeMessage(String upgradeMessage) {
    this.upgradeMessage = upgradeMessage;
  }

  public List<BusinessAccountType> getAccountTypeList() {
    return accountTypeList;
  }

  public void setAccountTypeList(List<BusinessAccountType> accountTypeList) {
    this.accountTypeList = accountTypeList;
  }

  public BusinessAccountType getCurrentAccount() {
    return currentAccount;
  }

  public void setCurrentAccount(BusinessAccountType currentAccount) {
    this.currentAccount = currentAccount;
  }

  public List<EmbeddedUrlDto> getEmbeddedUrlList() {
    return embeddedUrlList;
  }

  public void upgradeAccount() {
    StringBuilder message = new StringBuilder()
        .append(memberInfo.getFullName())
        .append(" has requested to upgrade the business account of ")
        .append(this.orgInfo.getBusinessName())
        .append(" from Business Standard to Business Plus ");

    if (StringUtils.isBlank(this.upgradeMessage) != true) {
      message.append(" with a message ").append(this.upgradeMessage);
    }
    this.getMemberService().addMemberFeedback(memberInfo.getId(),
        message.toString());

    FacesContext.getCurrentInstance().addMessage(
        null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Thanks",
            "Thanks for your request to upgrade your Business Account. "
                + "Some one from BusinessReach team will "
                + "get back to you soon"));

  }

}
