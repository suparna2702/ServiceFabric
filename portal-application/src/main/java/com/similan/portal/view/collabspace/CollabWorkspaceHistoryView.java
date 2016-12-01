package com.similan.portal.view.collabspace;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.community.PhotoViewOption;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.portal.view.BaseView;
import com.similan.service.api.collaborationworkspace.dto.basic.CollaborationWorkspaceDto;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.wall.dto.basic.WallEntryDto;
import com.similan.service.api.wall.dto.key.WallKey;

@Scope("view")
@Component("workspaceHistoryView")
@Slf4j
public class CollabWorkspaceHistoryView extends BaseView {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private MemberInfoDto member;
	
	@Autowired
	private OrganizationDetailInfoDto orgInfo;
	
	private CollaborationWorkspaceDto workspace;
	
	private List<WallEntryDto<CollaborationWorkspaceKey>> wallEntries = null;
	
	@PostConstruct
	public void init() {
		log.info("Initializing workspace history view");
		
		String workSpaceName = this.getContextParam("wsname");
		String workSpaceOwnerName = this.getContextParam("owsname");
		log.info("Workspace name " + workSpaceName + " owner name " + workSpaceOwnerName);
		
		if (workSpaceName != null) {
			
			CollaborationWorkspaceKey workspaceKey = new CollaborationWorkspaceKey(workSpaceOwnerName, workSpaceName);
			this.workspace = this.getCollabWorkspaceService().getDetail(workspaceKey);
			
			this.fetchLatestWallEntries();
		}
	}
	
	private void fetchLatestWallEntries(){
		/* get the wall */
		WallKey<CollaborationWorkspaceKey> wallKey = new WallKey<CollaborationWorkspaceKey>(this.workspace.getKey());
		wallEntries = this.getWallService().getLatest(wallKey);
		
	}
	
	public String getWorkspaceLogo(){
		return PhotoViewOption.ShowPhoto.effectivePhoto("images/businessLogo.jpg", 
				workspace.getLogo());
	}

	public MemberInfoDto getMember() {
		return member;
	}

	public OrganizationDetailInfoDto getOrgInfo() {
		return orgInfo;
	}

	public CollaborationWorkspaceDto getWorkspace() {
		return workspace;
	}

	public List<WallEntryDto<CollaborationWorkspaceKey>> getWallEntries() {
		return wallEntries;
	}

}
