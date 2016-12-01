package com.similan.adminapp.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.extern.slf4j.Slf4j;

import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;

@ViewScoped
@ManagedBean(name = "scriptExecutionView")
@Slf4j
public class ScriptExecutionView extends BaseAdminView {

	private static final long serialVersionUID = 1L;
	
	private List<BeanInfoDto> beanInfoList;
	
	private List<CodeSnippetDto> codeSnippetList;
	
	private String scriptText;
	
	private String scriptResult = "";
	
	@PostConstruct
	public void init() {
		log.info("Script view initialization");
		beanInfoList = this.scriptService.getBeans();
		codeSnippetList = this.scriptService.getSnippets();
	}
	
	public String getScriptResult() {
		return scriptResult;
	}

	public void setScriptResult(String scriptResult) {
		this.scriptResult = scriptResult;
	}

	public String getScriptText() {
		return scriptText;
	}

	public void setScriptText(String scriptText) {
		this.scriptText = scriptText;
	}

	public void executeScript(){
		log.info("Executing script:\n" + this.scriptText);
		this.scriptResult = this.scriptService.execute(this.scriptText);
    log.info("Script output was:\n" + this.scriptResult);
	}



	public List<BeanInfoDto> getBeanInfoList() {
		return beanInfoList;
	}

	public void setBeanInfoList(List<BeanInfoDto> beanInfoList) {
		this.beanInfoList = beanInfoList;
	}

	public List<CodeSnippetDto> getCodeSnippetList() {
		return codeSnippetList;
	}

	public void setCodeSnippetList(List<CodeSnippetDto> codeSnippetList) {
		this.codeSnippetList = codeSnippetList;
	}

}
