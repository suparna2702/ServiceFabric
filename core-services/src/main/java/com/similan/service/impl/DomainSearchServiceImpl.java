package com.similan.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.opengis.geometry.coordinate.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.community.OrganizationType;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialContact;
import com.similan.domain.entity.community.SocialEmployee;
import com.similan.domain.entity.community.SocialOrganization;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.community.SocialContactRepository;
import com.similan.domain.repository.community.SocialOrganizationRepository;
import com.similan.domain.repository.community.SocialPersonRepository;
import com.similan.domain.repository.global.GlobalRepository;
import com.similan.domain.repository.global.GlobalRepository.LocationFilter;
import com.similan.domain.repository.global.GlobalRepository.SearchTargetType;
import com.similan.domain.util.GeometryUtils;
import com.similan.framework.dto.AdvancedSearchInput;
import com.similan.framework.dto.CommunityEventDto;
import com.similan.framework.dto.CommunityEventType;
import com.similan.framework.dto.DomainSearchResult;
import com.similan.framework.dto.DomainSearchResultSet;
import com.similan.framework.dto.OrganizationAddressDto;
import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.dto.OrganizationDetailInfoDto;
import com.similan.framework.dto.SearchItemType;
import com.similan.framework.dto.SearchResultDto;
import com.similan.framework.dto.SearchResultItemDto;
import com.similan.framework.dto.SearchResultType;
import com.similan.framework.dto.member.MemberInfoDto;
import com.similan.framework.geo.GeoHaversineDistance;
import com.similan.framework.geo.Geocoder;
import com.similan.framework.util.BeanPropertyUpdator;
import com.similan.service.api.DomainSearchService;
import com.similan.service.api.connection.dto.basic.ContactType;

@Slf4j
public class DomainSearchServiceImpl implements DomainSearchService {
	@Autowired
	private GlobalRepository globalRepository;
	@Autowired
	private SocialOrganizationRepository organizationRepository;
	@Autowired
	private SearchAnalyticServiceImpl searchAnalyticService;
	@Autowired
	private SocialPersonRepository memberRepository;
  @Autowired
  private SocialContactRepository socialContactRepository;
  @Autowired
  private Geocoder geocoder;
  
  @Getter @Setter
  private BeanPropertyUpdator memberAttributeUpdater;
  @Getter @Setter
  private BeanPropertyUpdator orgAttributeUpdater;
  @Getter @Setter
  private Map<OrganizationType, String> markerColorCode;
	


	/**
	 * The main embedded search service
	 * 1. get all the associates of this organization then filter out
	 */
	public DomainSearchResultSet memberSearchEmbedded(String searchString, MemberInfoDto memberInfo,
			SearchItemType searchItem, LocationFilter locationFilter) throws Exception {
		
		DomainSearchResultSet resultSet = new DomainSearchResultSet();
		return resultSet;
		
	}

	/**
	 * The main search service
	 */
	public DomainSearchResultSet memberSearch(String searchString, long searcherId,
			SearchItemType searchItem, LocationFilter locationFilter) throws Exception {
		
		/* get the search string */
		log.info("Search string " + searchString 
				                        + "member id " + searcherId);
		SocialPerson searcher = this.memberRepository.findOne(searcherId);
		Long orgId = Long.MIN_VALUE;
		
		if(searcher.getEmployer() != null){
			SocialOrganization emp = (SocialOrganization)searcher.getEmployer().getContactFrom();
			orgId = emp.getId();
			log.info("Searcher's organization id " + orgId);
		}
		
		CommunityEventDto searchEventDto = new CommunityEventDto();
		searchEventDto.setEventGeneratorId(searcherId);
		searchEventDto.setEventType(CommunityEventType.SearchEvent);
		SearchResultDto searchResultDto = new SearchResultDto();
		searchResultDto.setFromSocialActorId(searcherId);
		searchEventDto.setSearchResult(searchResultDto);
		
		long elapsedTime = System.currentTimeMillis();
		
		DomainSearchResultSet resultSet = new DomainSearchResultSet();
		resultSet.setSearchString(searchString);
		resultSet.getSearchSummery().setSearchString(searchString);
		resultSet.setSercherId(searcherId);
		
		List<String> searchTypes = searchItem.getSelectedSearchType();
		
		List<SearchTargetType> targetTypes;
		
		if ((searchTypes == null) || (searchTypes.isEmpty()) || searchTypes.size() == 1 && searchTypes.get(0).isEmpty()) {
			targetTypes = Collections.singletonList(SearchTargetType.ANYTHING);
		} 
		else {
			targetTypes = new ArrayList<SearchTargetType>(searchTypes.size());
			for (String searchType : searchTypes) {
				targetTypes.add(SearchTargetType.valueOf(searchType));
			}
		}
		
		Page<Object> hits = this.globalRepository.search(
		    searchString, false, locationFilter, targetTypes, new PageRequest(0, Integer.MAX_VALUE));
		log.info("Size of search hits " + hits.getSize() 
				                 + " number of hits " + hits.getNumberOfElements());
		
		for (Object entity : hits) {
			//FIXME Method overloading? Visitor pattern? also currently
			// I am catching and swoalling the exception. We have to think about 
			// a better way
           if (entity instanceof SocialPerson) {
        	   
				try {
					SocialPerson person = (SocialPerson)entity;
					this.addSocialPersonInSearchResult(person, resultSet, searchResultDto);
				}
				catch(Exception exp){
					// We do not do any thing now except swallowing the exception
					// This is for now to just handle the inconsistent data
				}
           }
           else if(entity instanceof SocialOrganization){
        	   
               try {
                   SocialOrganization organization = (SocialOrganization)entity;
                   if(orgId.compareTo(organization.getId()) != 0){
                       this.addSocialBusinessInSearchResult(organization, resultSet, searchResultDto);
                   }
               }
               catch(Exception exp){
            	    // We do not do any thing now except swallowing the exception
					// This is for now to just handle the inconsistent data         	   
               }
           }
		}
		
		resultSet.getSearchSummery().setTimeToSearchInMilliSec((System.currentTimeMillis() - elapsedTime));
		searchResultDto.setResultSummary(resultSet.getSearchSummery());
		resultSet.generateMapCenter();
		
		/* publish the event */
		//CommunitySpringEvent springEvent = new CommunitySpringEvent(searchEventDto);
		//this.eventPublisher.publishEvent(springEvent);
		
		return resultSet;
	}

	/**
	 * advanced search
	 */
	public DomainSearchResultSet advancedSearch(MemberInfoDto searcherInfo,
			AdvancedSearchInput advSearchInput) throws Exception {
		
		log.info(" Advanced search " + advSearchInput.toString());
		
		/* populate all the input values */
		String searchString = advSearchInput.getKeyWord();
		Long searcherId = searcherInfo.getId();
		
		SearchItemType searchItem = new SearchItemType();
		
		if(advSearchInput.getSelectedSearchType().size() >= 0){
			//lets check the search types here if user has not selected 
			//anything then it should be SOCIAL_ORGANIZATION_ANY
			Iterator<String> searchItemIterator = advSearchInput.getSelectedSearchType()
					                                            .iterator();
			while(searchItemIterator.hasNext()){
				if(StringUtils.isEmpty(searchItemIterator.next())){
					searchItemIterator.remove();
				}
			}
			if(advSearchInput.getSelectedSearchType().size() >= 0){
				searchItem.setSelectedSearchType(advSearchInput.getSelectedSearchType());
			}
			else {
				List<String> searchItemArr = new ArrayList<String>();
				searchItemArr.add(SearchTargetType.SOCIAL_ORGANIZATION_ANY.toString());
				searchItem.setSelectedSearchType(searchItemArr);
			}
		}
		else {
			List<String> searchStringTypes = new ArrayList<String>();
			for (SearchTargetType searchTargetType : SearchTargetType.values()) {
				if (!searchTargetType.isSearchable()) {
					continue;
				}
				searchStringTypes.add(searchTargetType.name());
			}
			searchItem.setSelectedSearchType(searchStringTypes);
		}
		
		LocationFilter locationFilter = null;
		Double radius = Double.valueOf("25");
		
		/**
		 * 1. Construct the address and using geo code to get the lat/ long
		 * 2. Populate the LocationFilter and SearchType then launch the search
		 */
		try {
			
      if (!StringUtils.isBlank(advSearchInput.getStreet())
          || !StringUtils.isBlank(advSearchInput.getCity())
          || !StringUtils.isBlank(advSearchInput.getZipCode())) {
        AddressDto[] address = geocoder.geocode(
            advSearchInput.getStreet() + ", " + advSearchInput.getCity() + ", "
                + advSearchInput.getState() + " " + advSearchInput.getZipCode()
                + ", " + advSearchInput.getCountry(), null, null, "en");

        if (address.length >= 1) {

          if (advSearchInput.getRadius() != null) {
            radius = Double.valueOf(advSearchInput.getRadius());
          }

          AddressDto addr = address[0];
          log.info("Lat " + addr.getLatitude() + " long "
              + addr.getLongitude());

          Position position = GeometryUtils.toPositon(addr.getLongitude(),
              addr.getLatitude());
          locationFilter = new LocationFilter(position, radius, null);
        }
      }
      if (locationFilter == null
          && (!StringUtils.isBlank(advSearchInput.getStreet())
          || !StringUtils.isBlank(advSearchInput.getCity())
          || !StringUtils.isBlank(advSearchInput.getState())
          || !StringUtils.isBlank(advSearchInput.getZipCode())
          || !StringUtils.isBlank(advSearchInput.getCountry()))) {
        locationFilter = new LocationFilter(null, null,
            advSearchInput.getStreet() + " " + advSearchInput.getCity() + " "
                + advSearchInput.getState() + " " + advSearchInput.getZipCode()
                + " " + advSearchInput.getCountry());
      }
    }
		catch(Exception exp){
			log.error("Geo coding error", exp);
		}
		
		/* now perform actual search and return result */
		DomainSearchResultSet retSet = this.memberSearch(searchString, searcherId,
				                                   searchItem, locationFilter);
		return retSet;
	}
	
	/**
	 * 
	 * @param person
	 * @param resultSet
	 * @param searchResultDto
	 * @throws Exception
	 */
	private void addSocialPersonInSearchResult(SocialPerson person,  
                                               DomainSearchResultSet resultSet, 
                                               SearchResultDto searchResultDto) throws Exception{
		
		/* check for duplicate */
	    if(resultSet.getSearchResultMap()
			       .containsKey(person.getId()) == true){
		   return;
		   
	    }
		
		MemberInfoDto memberInfo = new MemberInfoDto();
		this.memberAttributeUpdater.update(person, memberInfo);

		List<Address> addresses = person.getAddresses();
		if (!addresses.isEmpty()) {
			
			Address address = addresses.iterator().next();
			memberInfo.setCity(address.getCity());
			memberInfo.setStreet(address.getNumber() == null ? null : address.getNumber() + " " + address.getStreet());
			memberInfo.setState(address.getState());
			memberInfo.setCountry(address.getCountry());
			memberInfo.setZipCode(address.getZipCode());
			double[] coordinates = GeometryUtils.toCoordinates(address.getPosition());
			memberInfo.setLongitude(coordinates[0]);
			memberInfo.setLatidute(coordinates[1]);
		}
		
		/* get employer info */
		try {
			SocialEmployee employerRel = person.getEmployer();
			
			if(employerRel != null){
				
				SocialOrganization org = (SocialOrganization)employerRel.getContactFrom();
				OrganizationBasicInfoDto employerTag = new OrganizationBasicInfoDto();
				employerTag.setId(org.getId());
				employerTag.setName(org.getBusinessName());
				memberInfo.setEmployer(employerTag);
			}
		}
		catch(Exception exp){
			log.error("Error in getting employer info ", exp);
		}
		
		DomainSearchResult result = new DomainSearchResult();
		result.setSearchItemType(SearchResultType.Person);
		result.setPersonType(true);
		result.setMemberInfo(memberInfo);
		resultSet.getSearchResultList().add(result);
		resultSet.getSearchResultMap().put(person.getId(), result);
		resultSet.getSearchSummery().increamentTotalPerson();
		resultSet.getSearchSummery().increamentTotalCount();
		
		SearchResultItemDto searchResultItemDto = new SearchResultItemDto();
		searchResultItemDto.setId(memberInfo.getId());
		searchResultItemDto.setRank(resultSet.getSearchSummery().getTotalRecords());
		searchResultItemDto.setContactEmail(person.getPrimaryEmail());
		searchResultItemDto.setContactPhone(person.getBusinessPhone());
		searchResultItemDto.setFirstName(person.getFirstName());
		searchResultItemDto.setForSocialActorId(person.getId());
		searchResultDto.getSearchResults().add(searchResultItemDto);
		

		
	}
	
	/**
	 * 
	 * @param businessContact
	 * @param resultSet
	 * @return
	 * @throws Exception
	 */
	private void addSocialBusinessInSearchResult(SocialOrganization businessContact, 
			                                            DomainSearchResultSet resultSet, 
			                                            SearchResultDto searchResultDto) throws Exception{
		
		   /* check for duplicate */
		   if(resultSet.getSearchResultMap()
				       .containsKey(businessContact.getId()) == true){
			   log.info("Cannot add business due to duplicate id " + businessContact.getId());
			   return;
			   
		   }
		   
		   OrganizationDetailInfoDto orgContactInfo = new OrganizationDetailInfoDto();

		   this.orgAttributeUpdater.update(businessContact, orgContactInfo);
		   List<Address> addresses = businessContact.getAddresses();
		   
           for (Address locationAddr : addresses) {
        	   double[] coordinates = null;
        	   if (locationAddr.getPosition() != null) {
               coordinates = GeometryUtils.toCoordinates(locationAddr.getPosition());
             }
             OrganizationAddressDto addr = new OrganizationAddressDto(locationAddr.getId(),
                 locationAddr.getCountry(), locationAddr.getCity(), locationAddr.getCounty(),
                 locationAddr.getNumber(), locationAddr.getStreet(), locationAddr.getZipCode(),
                 locationAddr.getState(), coordinates == null ? null : coordinates[1], coordinates == null ? null : coordinates[0]);
	           orgContactInfo.getLocations().add(addr);
	           if (coordinates != null) {
	             resultSet.addMapOrgMarker(coordinates[0], coordinates[1], orgContactInfo, 
	        		                  this.markerColorCode.get(orgContactInfo.getBusinessType()));
	           }
           }
           
           /* set the first visible address */
           OrganizationAddressDto searchVisibleAddr = null;
           if((orgContactInfo.getLocations() != null) && (orgContactInfo.getLocations().size() > 0)){
           	
	           	searchVisibleAddr = orgContactInfo.getLocations().get(0);
	           	log.info("Search visible addr " + searchVisibleAddr.getStreet());
	           	orgContactInfo.setPrimarySearchVisibleLocation(orgContactInfo.getLocations().get(0));
           }
           
           /* put the search result item */
           resultSet.getSearchSummery().increamentTotalBusiness();
           resultSet.getSearchSummery().increamentTotalCount();
           
           DomainSearchResult result = new DomainSearchResult();
           result.setSearchItemType(SearchResultType.Organization);
           result.setOrganizationType(true);
           result.setOrgInfo(orgContactInfo);
           resultSet.getSearchResultList().add(result);
           resultSet.getSearchResultMap().put(businessContact.getId(), result);
           
		   SearchResultItemDto searchResultItemDto = new SearchResultItemDto();
		   searchResultItemDto.setId(businessContact.getId());
		   searchResultItemDto.setRank(resultSet.getSearchSummery().getTotalRecords());
		   
		   searchResultItemDto.setContactEmail(businessContact.getPrimaryEmail());
		   searchResultItemDto.setContactPhone(businessContact.getBusinessPhoneNumber());
		   searchResultItemDto.setName(businessContact.getBusinessName());
		   searchResultItemDto.setForSocialActorId(businessContact.getId());
		   
		   if(searchVisibleAddr != null){
			   searchResultItemDto.getLocation().setStreet(searchVisibleAddr.getStreet());
			   searchResultItemDto.getLocation().setCity(searchVisibleAddr.getCity());
			   searchResultItemDto.getLocation().setZipCode(searchVisibleAddr.getZipCode());
		   }

		   searchResultDto.getSearchResults().add(searchResultItemDto);

	}

	/**
	 * 
	 * @param orgInfo
	 * @param advSearchInput
	 * @return
	 * @throws Exception 
	 */
	public DomainSearchResultSet businessAssociateSearch(Long orgId,
			AdvancedSearchInput advSearchInput) throws Exception {
		
		log.info("Business associate search " + advSearchInput.getSelectedSearchType().toString() 
				                 + " for business " + orgId);
		
		DomainSearchResultSet resultSet = new DomainSearchResultSet();
		SearchResultDto searchResultDto = new SearchResultDto();
		long startTime = System.currentTimeMillis();
		
		/* get the lat long from address input */
		AddressDto[] address = geocoder.geocode(
				advSearchInput.getStreet() + ", " + advSearchInput.getCity() + ", "
						+ advSearchInput.getState() + " "
						+ advSearchInput.getZipCode() + ", "
						+ advSearchInput.getCountry(), null, null, "en");
		
		Double radius = Double.MIN_VALUE;
		// TODO I suggest we use a null object here.
		AddressDto addrFrom = null;
		
		if (address.length >= 1) {
			
			if(advSearchInput.getRadius() != null){
			   radius = advSearchInput.getRadius();	
			}
			else {
				radius = Double.valueOf("50");
			}
			
			addrFrom = address[0];
			log.info("Lat " + addrFrom.getLatitude() + " long " + addrFrom.getLongitude());
		} 
		
		/**
		 * 1. Get all the business contacts
		 * 2. Filter them based on search type
		 * 3. Do a bounding box test based on their lat long
		 *    with the current one
		 */
		SocialOrganization socialOrg = this.organizationRepository.findOne(orgId);
		List<SocialContact> contactList = this.socialContactRepository.findByContactFrom(socialOrg);
		
		if(contactList != null){
			
			log.info("Checking organization contacts size " + contactList.size());
			
			for (SocialContact contact : contactList) {
				SocialActor contactActor = contact.getContactTo();
				log.info("Analyzing contact " + contactActor);
				
				if (!(contactActor instanceof SocialOrganization)){
					log.info("Social contact " + contactActor + " is not an organization");
					continue;
				}
				SocialOrganization businessContact = (SocialOrganization) contactActor;

				log.info("Evaluating organization "
						+ businessContact.getBusinessName() + " [ "
						+ businessContact.getId() + "]" + " with type "
						+ businessContact.getOrganizationType());
				
				if (contact.getContactType() == ContactType.ChannelPartner) {

					/* do the bounding box check */
					List<Address> locations = businessContact.getAddresses();
					log.info("Evaluating search distance for the following addresses: " + locations);
					
					for (Address addrTo : locations) {
						
						/* we will do the bounding box test here if there is a valid radius 
						 * street defined otherwise we match 
						 *  1. 1st country
						 *  2. then state 
						 *  3. then city for a match 
						 *  */

						if (this.locationProximityTest(addrTo, addrFrom, radius) == true) {
							
							this.addSocialBusinessInSearchResult(
									businessContact, resultSet, searchResultDto);
							
							log.info("Found match for address " + addrTo + " added to results for business " 
                                    + businessContact.getBusinessName());
						}
					}
				}
			}
		}
		
		/* result summary */
		resultSet.getSearchSummery().setTimeToSearchInMilliSec((System.currentTimeMillis() - startTime));		
		resultSet.generateMapCenter();
		
		/* here save the partner search event */
		this.searchAnalyticService.storePartnerSearchEvent(orgId, resultSet);
		return resultSet;
	}
	
	private boolean locationProximityTest(Address toAddress, AddressDto fromAddress, 
            Double radius){
		
		if(fromAddress == null || toAddress == null){
			return true;
		}
		
		if(StringUtils.isEmpty(fromAddress.getCity()) != true
				     && StringUtils.isEmpty(fromAddress.getState()) != true 
				     && StringUtils.isEmpty(fromAddress.getCountry()) != true) {
			
			return this.locationProximityBoundingBoxTest(toAddress, 
					 fromAddress, radius);
		}
		else {
			if(StringUtils.isEmpty(toAddress.getCountry()) != true 
					&& StringUtils.isEmpty(toAddress.getState()) != true){
				
				if(toAddress.getCountry().equalsIgnoreCase(fromAddress.getCountry()) == true 
						&& toAddress.getState().equalsIgnoreCase(fromAddress.getState()) == true){
					return true;
				}
			}
			else if(StringUtils.isEmpty(toAddress.getCountry()) != true 
					&& StringUtils.isEmpty(toAddress.getState()) == true){
				if(toAddress.getCountry().equalsIgnoreCase(fromAddress.getCountry()) == true){
					return true;
				}
				
			}
		}
		
		return false;
	}

	private boolean locationProximityBoundingBoxTest(Address toAddress, AddressDto fromAddress, 
			                              Double radius) {
		/**
		 * 1. If radius is not given we assume that user i snot interested in location 
		 *    search
		 * 2. If not check whether we have a valid from address. If valid from address is 
		 *    not given then also we assume user is is not interested in location search
		 * 3. Now do the bounding box
		 */
		if(radius == Double.MIN_VALUE || radius == null){
			return true;
		}
		
		if(fromAddress == null || toAddress == null){
			return true;
		}
		double[] coordinates = GeometryUtils.toCoordinates(toAddress.getPosition());
		return GeoHaversineDistance.computeProximity(
		    coordinates[1], coordinates[0], fromAddress.getLatitude(),
				fromAddress.getLongitude(), radius);
	}

}
