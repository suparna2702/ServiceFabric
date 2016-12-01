package com.similan.framework.service;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.util.AddressDto;
import com.similan.domain.repository.common.AddressRepository;
import com.similan.domain.util.GeometryUtils;
import com.similan.framework.geo.Geocoder;

@Slf4j
@Service
public class GeoCoderServiceImpl implements GeoCoderService {
  @Autowired
	private Geocoder geocoder;
	
	@Autowired
	private AddressRepository addressRepository;
	
	private Address getGeoCodeVerifiedAddress(AddressDto addrDto){
		
		Address address = null;
		String query = getGeocoderAddressQuery(addrDto);
		
		log.info("Geocoder address query string " + query);
		
		AddressDto[] addresses = geocoder.geocode(query, null, null, "en");
		
		if (addresses.length >= 1) {
			
           AddressDto addressDto = addresses[0];
  
    	   log.info("Adding new address ");
	       address = addressRepository.create(addrDto.getAddrName(), addrDto.getType(),
			          GeometryUtils.toPositon(addressDto.getLongitude(),
			              addressDto.getLatitude()), addressDto.getNumber(),
			          addressDto.getStreet(), addressDto.getCity(),
			          addressDto.getArea(), addressDto.getCounty(),
			          addressDto.getState(), addressDto.getZipCode(),
			          addressDto.getCountry(), addressDto.getFormattedAddress(),
			          addressDto.getSearchableAddress());
        	   
	       this.addressRepository.save(address);
  
		}
			
		return address;
	}

	private String getGeocoderAddressQuery(AddressDto addrDto) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(addrDto.getNumber()))
			sb.append(addrDto.getNumber());
		if (!StringUtils.isEmpty(addrDto.getNumber())
				&& !StringUtils.isEmpty(addrDto.getStreet()))
			sb.append(" ");

		if (!StringUtils.isEmpty(addrDto.getStreet()))
			sb.append(addrDto.getStreet());
		if (!StringUtils.isEmpty(addrDto.getStreet())
				&& (!StringUtils.isEmpty(addrDto.getCity())
						|| !StringUtils.isEmpty(addrDto.getState())
						|| !StringUtils.isEmpty(addrDto.getZipCode()) || !StringUtils
							.isEmpty(addrDto.getCountry())))
			sb.append(", ");
		if (!StringUtils.isEmpty(addrDto.getCity()))
			sb.append(addrDto.getCity());
		if (!StringUtils.isEmpty(addrDto.getCity())
				&& (!StringUtils.isEmpty(addrDto.getState())
						|| !StringUtils.isEmpty(addrDto.getZipCode()) || !StringUtils
							.isEmpty(addrDto.getCountry())))
			sb.append(", ");
		if (!StringUtils.isEmpty(addrDto.getState()))
			sb.append(addrDto.getState());
		if (!StringUtils.isEmpty(addrDto.getState())
				&& (!StringUtils.isEmpty(addrDto.getZipCode()) || !StringUtils
						.isEmpty(addrDto.getCountry())))
			sb.append(" ");
		if (!StringUtils.isEmpty(addrDto.getZipCode()))
			sb.append(addrDto.getZipCode());
		if (!StringUtils.isEmpty(addrDto.getZipCode())
				&& (!StringUtils.isEmpty(addrDto.getCountry())))
			sb.append(" ");
		if (!StringUtils.isEmpty(addrDto.getCountry()))
			sb.append(addrDto.getCountry());
		return sb.toString();
	}
	
	private Address getGeoCodeUnVerifiedAddress(AddressDto addressDto){
        
 	   log.info("Adding new address ");

    Address address = addressRepository.create(
        addressDto.getAddrName(),
        addressDto.getType(),
        GeometryUtils.toPositon(addressDto.getLongitude(),
            addressDto.getLatitude()), addressDto.getNumber(),
        addressDto.getStreet(), addressDto.getCity(), addressDto.getArea(),
        addressDto.getCounty(), addressDto.getState(), addressDto.getZipCode(),
        addressDto.getCountry(), addressDto.getFormattedAddress(),
        addressDto.getSearchableAddress());
 	   
        this.addressRepository.save(address);
			
		return address;
	}
	
	/**
	 * This method first tries to get a verified address using geocoding service.
	 * If for whatever reason that verification fails then it creates an unverified address.
	 * At least in this situation the user data is saved. 
	 */
	public Address getVerifiedAddressFromDto(AddressDto addrDto) {
		log.info("address dto " + addrDto.toString());
		
		Address address = null;
		try {
			if(addrDto.getCountry() != null)
				address = this.getGeoCodeVerifiedAddress(addrDto);
			
			if(address == null)
				address = this.getGeoCodeUnVerifiedAddress(addrDto);
		}
		catch(Exception exp){
			log.error("Cannot create or update address domain", exp);
			address = this.getGeoCodeUnVerifiedAddress(addrDto);
		}
		
		return address;
	}

}
