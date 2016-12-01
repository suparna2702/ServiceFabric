package com.similan.service.api;

import java.util.List;

import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;

public interface ScriptService {

  String execute(String script);

  List<BeanInfoDto> getBeans();

  List<CodeSnippetDto> getSnippets();

}
