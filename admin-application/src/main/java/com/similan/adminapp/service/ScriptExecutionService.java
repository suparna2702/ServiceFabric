package com.similan.adminapp.service;

import java.util.List;

import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;


public interface ScriptExecutionService {
	
	public List<BeanInfoDto> getBeans();
	
	public List<CodeSnippetDto> getSnippets();

  public String execute(String script);

}
