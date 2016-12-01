package com.similan.service.marshallers;

import java.util.ArrayList;
import java.util.List;

import com.similan.domain.entity.community.SocialOrganization;
import com.similan.framework.dto.OrganizationBasicInfoDto;

public class BusinessMarshaller {


  public OrganizationBasicInfoDto marshallBusiness(SocialOrganization business) {
    OrganizationBasicInfoDto retBusiness = new OrganizationBasicInfoDto();
    retBusiness.setId(business.getId());
    retBusiness.setLogoLocation(business.getImage());
    retBusiness.setName(business.getBusinessName());

    return retBusiness;
  }

  public List<OrganizationBasicInfoDto> marshallBusinesses(
      List<SocialOrganization> businesses) {
    List<OrganizationBasicInfoDto> businessList = new ArrayList<OrganizationBasicInfoDto>();
    for (SocialOrganization business : businesses) {
      businessList.add(marshallBusiness(business));
    }

    return businessList;
  }

}
