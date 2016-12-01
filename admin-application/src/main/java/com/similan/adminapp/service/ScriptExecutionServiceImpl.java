package com.similan.adminapp.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;
import com.similan.service.api.ScriptService;

@Service("scriptExecutionService")
public class ScriptExecutionServiceImpl implements ScriptExecutionService,
    Serializable {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ScriptService scriptService;

  public ScriptService getScriptService() {
    return scriptService;
  }

  public void setScriptService(ScriptService scriptService) {
    this.scriptService = scriptService;
  }

  @Transactional
  public List<BeanInfoDto> getBeans() {
    return scriptService.getBeans();
  }

  @Transactional
  public List<CodeSnippetDto> getSnippets() {
    return scriptService.getSnippets();
  }

  @Transactional
  public String execute(String script) {
    return scriptService.execute(script);
  }

}
