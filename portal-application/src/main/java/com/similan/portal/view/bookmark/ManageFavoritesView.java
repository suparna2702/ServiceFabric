package com.similan.portal.view.bookmark;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;

@Scope("view")
@Component("manageFavoritesView")
@Slf4j
public class ManageFavoritesView extends BaseView  {

  private static final long serialVersionUID = 1L;
  
  @Autowired(required = true)
  private MemberInfoDto member = null;
  
  @Autowired
  private OrganizationDetailInfoDto orgInfo;
  
  @PostConstruct
  public void init() {
    log.info("Post init ManageFavoritesView view ");
  }

}
