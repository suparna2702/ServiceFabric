package com.similan.framework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * 
 * @author supapal
 *
 */
@Slf4j
public class DomainSearchResultSet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String searchString;
	
	private long sercherId;
	
	private String searchSource;
	
	private List<DomainSearchResult> searchResultList = new ArrayList<DomainSearchResult>();
	
	private Map<Long, DomainSearchResult> searchResultMap = new HashMap<Long, DomainSearchResult>();
	
	private MapModel simpleModel;  
	
    private DomainSearchResult  selectedResult;
    
    private SearchResultSummery searchSummery = new SearchResultSummery();
    
    /* US latitude longitude by default 37.702387, 238.406370 (Dublin) */
    private Double mapCenterLat = 37.702387;
    
    private Double mapCenterLong = 238.406370;
    
    public DomainSearchResult getSearchResultItemById(Long id){
    	
    	for(DomainSearchResult resultItem : searchResultList){
    		log.info("Search result id " + resultItem.getId() 
    				       + " type " + resultItem.getSearchItemType());
    		if(resultItem.getId() == id){
    			return resultItem;
    		}
    	}
    	
    	return null;
    }
    
	public Map<Long, DomainSearchResult> getSearchResultMap() {
		return searchResultMap;
	}

	public void setSearchResultMap(Map<Long, DomainSearchResult> searchResultMap) {
		this.searchResultMap = searchResultMap;
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
	
	public void addMapOrgMarker(Double longitude, Double latitude,
			                 OrganizationDetailInfoDto orgInfo, String markerColor){
		
		String markerTag = "Business";
		
		if(orgInfo.getBusinessName() != null){
			markerTag = orgInfo.getBusinessName();
		}
        
		if(simpleModel == null){
			simpleModel = new DefaultMapModel();
        }
         
        if((latitude != null) && (longitude != null)){
				
			if((latitude != Double.MIN_VALUE) && (longitude != Double.MIN_VALUE)){
				
				log.info("Latitude " + latitude + " longitude " + longitude);
				LatLng coordOrg = new LatLng(latitude, longitude);
				Marker mapMarker = new Marker(coordOrg, markerTag, orgInfo, markerColor);
				
				simpleModel.addOverlay(mapMarker);
			}
		}
	}
	
	/* we will find an algo later */
	public void generateMapCenter(){
		
		if(this.simpleModel != null){
			List<Marker> markerList = this.simpleModel.getMarkers();
			
			if((markerList != null) && (markerList.size() > 0)){
				
				Marker center = markerList.get(0);
				this.mapCenterLat = center.getLatlng().getLat();
				this.mapCenterLong = center.getLatlng().getLng();
				log.info("Map center Latitude " + this.mapCenterLat 
						                   + " longitude " + this.mapCenterLong);
			}
		}
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}

	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public long getSercherId() {
		return sercherId;
	}

	public void setSercherId(long sercherId) {
		this.sercherId = sercherId;
	}

	public String getSearchSource() {
		return searchSource;
	}

	public void setSearchSource(String searchSource) {
		this.searchSource = searchSource;
	}

	public DomainSearchResult getSelectedResult() {
		return selectedResult;
	}

	public void setSelectedResult(DomainSearchResult selectedResult) {
		this.selectedResult = selectedResult;
	}

	public List<DomainSearchResult> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<DomainSearchResult> searchResultList) {
		this.searchResultList = searchResultList;
	}
	
	public SearchResultSummery getSearchSummery() {
		return searchSummery;
	}

	public void setSearchSummery(SearchResultSummery searchSummery) {
		this.searchSummery = searchSummery;
	}

	public void onRowSelect(SelectEvent event) {
		DomainSearchResult result = (DomainSearchResult)event.getObject();
		log.info("selected result id :" + result.getId());
		setSelectedResult(result);
	}
	
}
