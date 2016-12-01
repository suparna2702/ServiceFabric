package com.similan.domain.entity.lead;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "crmLeadFieldSettingConfig")
public class CrmLeadFieldSettingConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private List<String> leadFieldExports;
	
	private List<String> leadFieldImports;
	
	public CrmLeadFieldSettingConfig(){
		leadFieldExports = new ArrayList<String>();
		leadFieldImports = new ArrayList<String>();
	}

	@XmlElements(@XmlElement(name="leadFieldExports"))
	public List<String> getLeadFieldExports() {
		return leadFieldExports;
	}

	public void setLeadFieldExports(List<String> leadFieldExports) {
		this.leadFieldExports = leadFieldExports;
	}

	@XmlElements(@XmlElement(name="leadFieldImports"))
	public List<String> getLeadFieldImports() {
		return leadFieldImports;
	}

	public void setLeadFieldImports(List<String> leadFieldImports) {
		this.leadFieldImports = leadFieldImports;
	}

}
