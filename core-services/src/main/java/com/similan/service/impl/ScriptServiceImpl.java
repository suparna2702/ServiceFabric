package com.similan.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.similan.domain.entity.script.CodeSnippet;
import com.similan.domain.repository.script.CodeSnippetRepository;
import com.similan.framework.dto.script.BeanInfoDto;
import com.similan.framework.dto.script.CodeSnippetDto;
import com.similan.service.api.ScriptService;

public class ScriptServiceImpl implements ScriptService,
    ApplicationContextAware, InitializingBean {

  public static class PrototypeBeanAccessor {

    private final ApplicationContext applicationContext;

    private final String name;

    public PrototypeBeanAccessor(ApplicationContext applicationContext,
        String name) {
      this.applicationContext = applicationContext;
      this.name = name;
    }

    public Object create() {
      return applicationContext.getBean(name);
    }

    public Object createWith(Object... arguments) {
      return applicationContext.getBean(name, arguments);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static class Console {
    private final StringBuilder builder = new StringBuilder();

    public void append(Object text) {
      builder.append(text);
    }

    public void line() {
      builder.append('\n');
    }

    public void print(Object text) {
      append(text);
      line();
    }

    public void error(Throwable error) {
      print("[Error]");
      if (error == null) {
        print(error);
        return;
      }
      append(error.getClass().getName());
      append(": ");
      print(error.toString());
      print(ExceptionUtils.getFullStackTrace(error));
    }

    @Override
    public String toString() {
      return builder.toString();
    }
  }

  public static class Loader {

    private final CodeSnippetRepository codeSnippetRepository;
    private final Context context;
    private final Scriptable scope;

    public Loader(CodeSnippetRepository codeSnippetRepository, Context context,
        Scriptable scope) {
      this.codeSnippetRepository = codeSnippetRepository;
      this.context = context;
      this.scope = scope;
    }

    public void load(String snippetName) {
      CodeSnippet snippet = codeSnippetRepository.findByName(snippetName);
      if (snippet == null) {
        throw new IllegalArgumentException("Unknown snippet: " + snippetName);
      }
      context.evaluateString(scope, snippet.getSnippet(), "<snippet:"
          + snippetName + ">", 1, null);
    }
  }

  @Autowired
  private CodeSnippetRepository codeSnippetRepository;

  private ApplicationContext applicationContext;

  @PersistenceContext
  private EntityManager entityManager;

  public ScriptServiceImpl() {
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public String execute(String script) {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    NativeObject beans = new NativeObject();
    for (String beanName : beanNames) {
      if (applicationContext.isSingleton(beanName)) {
        Object bean = applicationContext.getBean(beanName);
        beans.defineProperty(beanName, bean, NativeObject.READONLY);
      } else {
        PrototypeBeanAccessor accessor = new PrototypeBeanAccessor(
            applicationContext, beanName);
        beans.defineProperty(beanName, accessor, NativeObject.READONLY);
      }
    }
    Console console = new Console();
    Context context = Context.enter();
    Scriptable scope = context.initStandardObjects();
    Loader loader = new Loader(codeSnippetRepository, context, scope);
    scope.put("beans", scope, beans);
    scope.put("applicationContext", scope, applicationContext);
    scope.put("entityManager", scope, entityManager);
    scope.put("console", scope, console);
    scope.put("loader", scope, loader);

    try {
      Object output = context.evaluateString(scope, script, "<cmd>", 1, null);
      console.print(Context.toString(output));
    } catch (Exception e) {
      console.error(e);
    }
    return console.toString();
  }

  public List<BeanInfoDto> getBeans() {
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    List<BeanInfoDto> beansInfo = new ArrayList<BeanInfoDto>(beanNames.length);
    for (String beanName : beanNames) {
      Class<?> beanClass = applicationContext.getType(beanName);
      boolean singleton = applicationContext.isSingleton(beanName);
      beansInfo.add(new BeanInfoDto(beanName, beanClass == null ? null
          : beanClass.getName(), singleton));
    }
    Collections.sort(beansInfo, new Comparator<BeanInfoDto>() {
      public int compare(BeanInfoDto bean1, BeanInfoDto bean2) {
        return bean1.getName().compareTo(bean2.getName());
      }
    });
    return beansInfo;
  }

  @Transactional
  public List<CodeSnippetDto> getSnippets() {
    List<CodeSnippet> codeSnippets = codeSnippetRepository.findAll(new Sort(
        "name"));
    List<CodeSnippetDto> codeSnippetDtos = new ArrayList<CodeSnippetDto>(
        codeSnippets.size());
    for (CodeSnippet codeSnippet : codeSnippets) {
      codeSnippetDtos.add(new CodeSnippetDto(codeSnippet.getId(), codeSnippet
          .getName(), codeSnippet.getSnippet()));
    }
    return codeSnippetDtos;
  }

  public void afterPropertiesSet() throws Exception {
    Preconditions.checkState(applicationContext != null,
        "Application context cannot be null.");
    Preconditions.checkState(codeSnippetRepository != null,
        "Code snippet repository cannot be null.");
  }

}
