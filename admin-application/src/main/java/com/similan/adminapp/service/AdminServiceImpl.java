package com.similan.adminapp.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.admin.AdminAccount;
import com.similan.domain.entity.community.MemberState;
import com.similan.domain.repository.admin.AdminAccountRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.partner.PartnerProgramDefinitionRepository;
import com.similan.domain.repository.partner.PartnershipRepository;
import com.similan.framework.configuration.PlatformCommonSettings;
import com.similan.framework.dto.admin.AdminAccountDto;

@Service("adminService")
@Slf4j
public class AdminServiceImpl implements AdminService, Serializable {
	private static final long serialVersionUID = 1L;
			
	@Autowired
	private AdminAccountRepository adminAccountRepository;
	
	@Autowired
	private PlatformCommonSettings platformCommonSettings;
	
	@Autowired
	private SocialPersonRepository memberRepository;
	
	@Autowired
	private SocialOrganizationRepository businessRepository;
	
	@Autowired
	private PartnerProgramDefinitionRepository partnerProgramRepository;
	
	@Autowired
	private PartnershipRepository partnershipRepository;

	public PlatformCommonSettings getPlatformCommonSettings() {
		return platformCommonSettings;
	}

	public void setPlatformCommonSettings(
			PlatformCommonSettings platformCommonSettings) {
		this.platformCommonSettings = platformCommonSettings;
	}

	
	public void setTotalPartnershipCount(Long cnt){
		
	}
	
	@Transactional
	public Long getTotalPartnershipCount(){
		return this.partnershipRepository.findPartnershipCount();
	}
	
	public void setTotalPartnerProgramCount(Long progCnt){
		
	}
	
	@Transactional
	public Long getTotalPartnerProgramCount(){
		return this.partnerProgramRepository.findPartnerProgramCount();
	}
	
	@Transactional
	public Long getTotalMemberCount(){
		return this.memberRepository.getTotalMemberCount();
	}
	
	@Transactional
	public Long getTotalPendingMemberCount(){
		return this.memberRepository.getTotalMemberCountByStatus(MemberState.Pending);
	}
	
	@Transactional
	public Long getTotalSuspendedMemberCount(){
		return this.memberRepository.getTotalMemberCountByStatus(MemberState.Suspended);
	}
	
	@Transactional
	public Long getTotalActiveMemberCount(){
		return this.memberRepository.getTotalMemberCountByStatus(MemberState.Active);
	}
	
	@Transactional
	public Long getTotalBusinessCount(){
		return this.businessRepository.getTotalBusinessCount();
	}
	
	@Transactional
	public Map<String, Long> getMemberLoginCountOverTimeperiod(int startPeriod,
			int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public Map<String, Long> getWallEventCountOverTimeperiod(int startPeriod,
			int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public Map<String, Long> getCommunitySearchEventCountOverTimeperiod(
			int startPeriod, int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public Map<String, Long> getPartnerSearchEventCountOverTimeperiod(
			int startPeriod, int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public AdminAccountDto login(String email, String password){
		
		AdminAccountDto account = null;
		
		/* first check super admin login */
		if(this.platformCommonSettings.getSuperAdminLoginForAdminApp() != null){
			
			String superAdminEmail = this.platformCommonSettings.getSuperAdminLoginForAdminApp().getValue();
			String supaerAdminPassword = this.platformCommonSettings.getSuperAdminPasswordForAdminApp().getValue();
			
			if(superAdminEmail.contentEquals(email) && supaerAdminPassword.contentEquals(password)){
				AdminAccount superAdminAccount = this.adminAccountRepository.findByEmail(email);
				
				if(superAdminAccount != null){
					
					account = new AdminAccountDto();
					account.setEmail(superAdminAccount.getEmail());
					account.setFirstName(superAdminAccount.getFirstName());
					account.setLastName(superAdminAccount.getLastName());
					account.setPassword(superAdminAccount.getPassword());
					account.setLogged(true);
				}
				else {
					/* create super admin account */
					account = new AdminAccountDto();
					account.setEmail(email);
					account.setFirstName("Supaer Admin");
					account.setLastName("Supaer Admin");
					account.setPassword(password);
					account.setLogged(true);
					this.createAdminAccount(account);
				}
			}
		}
		
		AdminAccount adminAccount = this.adminAccountRepository.findByEmail(email);
		
		if(adminAccount != null){
			if(adminAccount.getPassword().contentEquals(password) == true){
				account = new AdminAccountDto();
				account.setEmail(adminAccount.getEmail());
				account.setFirstName(adminAccount.getFirstName());
				account.setLastName(adminAccount.getLastName());
				account.setPassword(adminAccount.getPassword());
				account.setLogged(true);
			}
		}
		
		return account;
	}
	
	@Transactional
	public void createAdminAccount(AdminAccountDto account){
		
		AdminAccount adminAccount = this.adminAccountRepository.create();
		adminAccount.setEmail(account.getEmail());
		adminAccount.setFirstName(account.getFirstName());
		adminAccount.setLastName(account.getLastName());
		adminAccount.setPassword(account.getPassword());
		
		this.adminAccountRepository.save(adminAccount);
		log.info("Account created " + account.getFirstName());
		account.setId(adminAccount.getId());
		
	}

	@Transactional
	public boolean isEmailExists(String email) {
		AdminAccount adminAccount = this.adminAccountRepository.findByEmail(email);
		
		if(adminAccount != null){
			return true;
		}
		
		return false;
	}
	
	@Transactional
	public List<AdminAccountDto> getAllAdminAccount(){
		List<AdminAccount> accountList = this.adminAccountRepository.findAll();
		List<AdminAccountDto> retAccountList = new ArrayList<AdminAccountDto>();
		
		for(AdminAccount account : accountList){
			
			AdminAccountDto accountDto = new AdminAccountDto();
			accountDto.setId(account.getId());
			accountDto.setEmail(account.getEmail());
			accountDto.setFirstName(account.getFirstName());
			accountDto.setLastName(account.getLastName());
			accountDto.setPassword(account.getPassword());
			
			retAccountList.add(accountDto);
		}
		
		return retAccountList;
	}
	
	@Transactional
	public Map<String, Long> getLeadImportCountOverTimeperiod(int startPeriod,
			int frequency){
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
        Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public Map<String, Long> getCrmSyncCountOverTimeperiod(int startPeriod,
			int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
	
	@Transactional
	public Map<String, Long> getLeadTransferCountOverTimeperiod(
			int startPeriod, int frequency){
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}

	@Transactional
	public Map<String, Long> getPollSubmissionCountOverTimeperiod(
			int startPeriod, int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}

	@Transactional
	public Map<String, Long> getPollEventCountOverTimeperiod(int startPeriod,
			int frequency) {
		//CustomerEngagementAnalyticsServiceImpl customerEngagementAnalyticsService = (CustomerEngagementAnalyticsServiceImpl)SpringUtil.getSpringBean("customerEngaementAnalyticService");
		Map<String, Long> retMap = new HashMap<String, Long>();
		return retMap;
	}
}
