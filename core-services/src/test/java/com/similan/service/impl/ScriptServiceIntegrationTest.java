package com.similan.service.impl;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;
import com.similan.service.BaseCoreServiceTest;
import com.similan.service.api.ScriptService;

public class ScriptServiceIntegrationTest extends BaseCoreServiceTest {

  @Autowired
  private ScriptService scriptService;

  @Test
  public void execute() {
    StringBuilder script = new StringBuilder();
    script.append("var s = beans.codeSnippetRepository.findOne(-1);");
    script
        .append("if (s == null) { console.print('snippet not found'); } else { console.print('snippet found'); } ");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("snippet not found"));
  }

  @Test
  public void getSnippets() {
    List<CodeSnippetDto> snippets = scriptService.getSnippets();
    Assert.assertNotNull(snippets);
    for (CodeSnippetDto codeSnippet : snippets) {
      Assert.assertNotNull(codeSnippet);
      Assert.assertNotNull(codeSnippet.getId());
      Assert.assertNotNull(codeSnippet.getName());
      Assert.assertNotNull(codeSnippet.getSnippet());
    }
  }

  @Test
  public void getBeans() {
    List<BeanInfoDto> beans = scriptService.getBeans();
    Assert.assertNotNull(beans);
    Assert.assertFalse(beans.isEmpty());
    for (BeanInfoDto bean : beans) {
      Assert.assertNotNull(bean);
      Assert.assertNotNull(bean.getName());
    }
  }

  @Test
  public void executeUsingCustomJavaObject() {
    StringBuilder script = new StringBuilder();
    script
        .append("new com.similan.framework.dto.script.BeanInfoDto('A', 'B', true);");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));
  }
}
