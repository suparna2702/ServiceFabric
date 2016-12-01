package com.similan.service.api;

import com.similan.domain.repository.global.GlobalRepository.LocationFilter;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.SearchItemType;
import com.similan.framework.dto.member.MemberInfoDto;

public interface DomainSearchService {
	
	/**
	 * 
	 * @param searchInfo
	 * @param advSearchInput
	 * @return
	 * @throws Exception 
	 */
	public DomainSearchResultSet advancedSearch(MemberInfoDto searchInfo,
			AdvancedSearchInput advSearchInput) throws Exception;
	
	/**
	 * 
	 * @param searchString
	 * @param searcherId
	 * @param searchItem
	 * @param locationFilter
	 * @return
	 * @throws Exception
	 */
	public DomainSearchResultSet memberSearch(String searchString, long searcherId,
			SearchItemType searchItem, LocationFilter locationFilter) throws Exception;
	
	/**
	 * 
	 * @param searchString
	 * @param searcherId
	 * @param searchItem
	 * @param locationFilter
	 * @return
	 * @throws Exception
	 */
	public DomainSearchResultSet memberSearchEmbedded(String searchString, MemberInfoDto memberInfo,
			SearchItemType searchItem, LocationFilter locationFilter) throws Exception;
	
	public DomainSearchResultSet businessAssociateSearch(Long orgId,
			AdvancedSearchInput advSearchInput) throws Exception;

}
