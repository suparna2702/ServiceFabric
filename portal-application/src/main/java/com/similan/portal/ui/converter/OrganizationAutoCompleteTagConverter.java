package com.similan.portal.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.similan.framework.dto.OrganizationBasicInfoDto;
import com.similan.framework.util.JsfUtils;
import com.similan.portal.databean.OrganizationAutoCompleteBean;

@FacesConverter( value="orgTagConverter" )
public class OrganizationAutoCompleteTagConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		
		OrganizationAutoCompleteBean completeBean = (OrganizationAutoCompleteBean)JsfUtils.findBackingBean("orgAutoCompleteBean");
		OrganizationBasicInfoDto  orgTag = null;
		
		if(completeBean != null){
			orgTag = completeBean.getOrgByTagName(value);
			
			if(orgTag == null){
				orgTag = new OrganizationBasicInfoDto();
				orgTag.setName(value);
			}
		}
		
		return orgTag;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object obj) throws ConverterException {
		
		if(obj != null){
			  if(obj instanceof OrganizationBasicInfoDto){
				  OrganizationBasicInfoDto orgTag = (OrganizationBasicInfoDto) obj;
				  return orgTag.getName();
				  
			  }
			}
			
			return null;
	}

}
