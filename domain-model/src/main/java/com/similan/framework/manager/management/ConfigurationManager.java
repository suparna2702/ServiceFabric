package com.similan.framework.manager.management;

import java.util.List;

public interface ConfigurationManager {

	void reload();
	
	List<ConfigurationParameterDto> getParameters();
	
}
