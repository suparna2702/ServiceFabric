package com.similan.framework.service;

import com.similan.domain.entity.common.Address;
import com.similan.domain.entity.util.AddressDto;

public interface GeoCoderService {
	
	public Address getVerifiedAddressFromDto(AddressDto addrDto);

}
