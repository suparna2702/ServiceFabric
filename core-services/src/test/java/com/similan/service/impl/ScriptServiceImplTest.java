package com.similan.service.impl;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;

import com.similan.domain.repository.script.CodeSnippetRepository;
import com.similan.service.impl.ScriptServiceImpl.Console;

@RunWith(PowerMockRunner.class)
public class ScriptServiceImplTest {

  public static class MyBeanClass {
    public void myMethod(Console console) {
      console.print("My bean was called");
    }
  }

  @Test
  public void executeUsingConsole() {
    CodeSnippetRepository codeSnippetRepository = createMock(CodeSnippetRepository.class);

    ApplicationContext applicationContext = createMock(ApplicationContext.class);
    expect(applicationContext.getBeanDefinitionNames())
        .andReturn(new String[0]);

    replayAll();

    ScriptServiceImpl scriptService = new ScriptServiceImpl();
    Whitebox.setInternalState(scriptService, codeSnippetRepository);
    scriptService.setApplicationContext(applicationContext);

    StringBuilder script = new StringBuilder();
    script.append("console.append('X');\n");
    script.append("console.print('Y');\n");
    script.append("console.append('Z');\n");
    script.append("console.error(null);\n");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("XY\nZ"));
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("[Error]\nnull\n"));

    verifyAll();
  }

  @Test
  public void executeUsingSingletonBean() {

    CodeSnippetRepository codeSnippetRepository = createMock(CodeSnippetRepository.class);

    ApplicationContext applicationContext = createMock(ApplicationContext.class);
    expect(applicationContext.getBeanDefinitionNames()).andReturn(
        new String[] { "myBean" });
    expect(applicationContext.isSingleton("myBean")).andReturn(true);
    expect(applicationContext.getBean("myBean")).andReturn(new MyBeanClass());

    replayAll();

    ScriptServiceImpl scriptService = new ScriptServiceImpl();
    Whitebox.setInternalState(scriptService, codeSnippetRepository);
    scriptService.setApplicationContext(applicationContext);

    StringBuilder script = new StringBuilder();
    script.append("beans.myBean.myMethod(console)");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("My bean was called"));
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));

    verifyAll();
  }

  @Test
  public void executeUsingPrototypeBean() {

    CodeSnippetRepository codeSnippetRepository = createMock(CodeSnippetRepository.class);

    ApplicationContext applicationContext = createMock(ApplicationContext.class);
    expect(applicationContext.getBeanDefinitionNames()).andReturn(
        new String[] { "myBean" });
    expect(applicationContext.isSingleton("myBean")).andReturn(false);
    expect(applicationContext.getBean("myBean")).andReturn(new MyBeanClass());

    replayAll();

    ScriptServiceImpl scriptService = new ScriptServiceImpl();
    Whitebox.setInternalState(scriptService, codeSnippetRepository);
    scriptService.setApplicationContext(applicationContext);

    StringBuilder script = new StringBuilder();
    script.append("beans.myBean.create().myMethod(console)");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("My bean was called"));
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));

    verifyAll();
  }

  @Test
  public void executeUsingContext() {

    CodeSnippetRepository codeSnippetRepository = createMock(CodeSnippetRepository.class);

    ApplicationContext applicationContext = createMock(ApplicationContext.class);
    expect(applicationContext.getBeanDefinitionNames())
        .andReturn(new String[0]);
    expect(applicationContext.getId()).andReturn("This is the context id");

    replayAll();

    ScriptServiceImpl scriptService = new ScriptServiceImpl();
    Whitebox.setInternalState(scriptService, codeSnippetRepository);
    scriptService.setApplicationContext(applicationContext);

    StringBuilder script = new StringBuilder();
    script.append("console.print(applicationContext.getId())");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        JUnitMatchers.containsString("This is the context id"));
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));

    verifyAll();
  }

  @Test
  public void executeUsingCustomJavaObject() {
    CodeSnippetRepository codeSnippetRepository = createMock(CodeSnippetRepository.class);

    ApplicationContext applicationContext = createMock(ApplicationContext.class);
    expect(applicationContext.getBeanDefinitionNames())
        .andReturn(new String[0]);

    replayAll();

    ScriptServiceImpl scriptService = new ScriptServiceImpl();
    Whitebox.setInternalState(scriptService, codeSnippetRepository);
    scriptService.setApplicationContext(applicationContext);

    StringBuilder script = new StringBuilder();
    script
        .append("new com.similan.framework.dto.script.BeanInfoDto('A', 'B', true);");
    String output = scriptService.execute(script.toString());
    Assert.assertThat("Was: " + output, output,
        CoreMatchers.not(JUnitMatchers.containsString("[Error]")));

    verifyAll();
  }

}
