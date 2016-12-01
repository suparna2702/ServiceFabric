package com.similan.framework.workflow.impl;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.activity.ActivityBehaviour;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.pvm.internal.id.InitializePropertiesCmd;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.similan.domain.entity.process.BusinessProcessDefinition;
import com.similan.domain.repository.process.BusinessProcessDefinitionRepository;
import com.similan.service.internal.impl.process.BusinessProcessActions;
import com.similan.service.internal.impl.process.BusinessProcessInternalServiceImpl;

@Component
public class WorkflowDeploymentManager {
  @Autowired
	private RepositoryService repositoryService;
	@Autowired
	private BusinessProcessDefinitionRepository businessProcessDefinitionRepository;
  @Autowired
  private ProcessEngine processEngine;
  @Autowired
  private ApplicationContext applicationContext;
  @javax.annotation.Resource(name = "workflowList")
  private Map<String, String> workflowMap;

  private String readContents(String processName, String processClassPath) {
    try {
      Resource resource = new ClassPathResource(processClassPath);
      return IOUtils.toString(resource.getInputStream());
    } catch (IOException e) {
      throw new IllegalStateException("Error reading process " + processName
          + ":" + processClassPath, e);
    }
  }
	
	private void deployProcessFromClassPath(String processName) {
		BusinessProcessDefinition businessProcess = businessProcessDefinitionRepository.findByName(processName);
		String processClassPath = workflowMap.get(processName);
		checkState(processClassPath !=null, "No entry for process " + processName);
		String content = readContents(processName, processClassPath);
    validate(processName, processClassPath, content);
		String hash = DigestUtils.shaHex(content);
    if (businessProcess == null) {
      businessProcess = businessProcessDefinitionRepository.create();
      businessProcess.setName(processName);
    } else {
      if (hash.equals(businessProcess.getHash())) {
        return;
      }
		}
		
		NewDeployment deployment = this.repositoryService.createDeployment();
		deployment.addResourceFromClasspath(processClassPath);
		deployment.deploy();
		
    businessProcess.setHash(hash);
    businessProcess.setTimeStamp(new Date());

		this.businessProcessDefinitionRepository.save(
				businessProcess);
	}

  private List<String> validate(String processName, String processClassPath,
      String content) {
    if (processClassPath.startsWith("com/similan/service/internal/impl/process")) {
      String actionClassName = BusinessProcessInternalServiceImpl.BASE_ACTIONS_PACKAGE + processName + BusinessProcessInternalServiceImpl.ACTIONS_SUFFIX;
      try {
        @SuppressWarnings("unchecked")
        Class<? extends BusinessProcessActions<?>> actionClass = (Class<? extends BusinessProcessActions<?>>) Class.forName(actionClassName);
        return validate(actionClass, processName, processClassPath, content);
      } catch (ClassNotFoundException e) {
        return Collections.singletonList(String.format("%s actions class not found for process %s", actionClassName, processName));
      }
    } else {
      return validateOld(processName, processClassPath, content);
    }
  }

  private static final String CUSTOM_NODE = "custom";
  private static final String HANDLER_NODE = "handler";
  private static final Pattern EXPR_PATTERN = Pattern.compile("<("
      + CUSTOM_NODE + "|" + HANDLER_NODE
      + ")[^>]+expr\\s*=\\s*[\"']([^\"']*)[\"']", Pattern.MULTILINE
      | Pattern.CASE_INSENSITIVE);

  private static final String EXPR_PREFIX = "#{";
  private static final String EXPR_SUFFIX = "}";

  private static final String ENGINE_EXECUTE_PREFIX = "engine.execute(";
  private static final String ENGINE_DECIDE_PREFIX = "engine.decide(";
  private static final String ENGINE_SUFFIX = ")";
  
  private static final Pattern CALL_PATTERN = Pattern
      .compile("^([\\w\\d_]+).([\\w\\d_]+)\\((.*)\\)$", Pattern.MULTILINE);

  private List<String> validateOld(String processName, String processClassPath,
      String content) {
    Matcher matcher = EXPR_PATTERN.matcher(content);
    List<String> errors = new LinkedList<>();
    while (matcher.find()) {
      String node = matcher.group(1);
      String expression = matcher.group(2);
      if (!expression.startsWith(EXPR_PREFIX)
          || !expression.endsWith(EXPR_SUFFIX)) {
        errors.add(String.format("Process %s (%s) contains an invalid expression: %s",
            processName, processClassPath, expression));
        continue;
      }
      Class<?> expected = node.equalsIgnoreCase(CUSTOM_NODE) ? ActivityBehaviour.class : DecisionHandler.class;
      String expressionValue = expression.substring(EXPR_PREFIX.length(),
          expression.length() - EXPR_SUFFIX.length());
      Object bean = validateBean(processName, processClassPath, errors,
          expression, expressionValue);
      if (bean != null) {
        if (!expected.isInstance(bean)) {
          errors.add(String.format("Process %s (%s) references a bean (%s) of an invalid type (%s) instead of the expected %s: %s",
              processName, processClassPath, expressionValue, bean.getClass().getName(), expected.getName(), expression));
        }
      }
    }
    return errors;
  }

  private List<String> validate(
      Class<? extends BusinessProcessActions<?>> actionClass,
      String processName, String processClassPath, String content) {
    Matcher matcher = EXPR_PATTERN.matcher(content);
    List<String> errors = new LinkedList<>();
    while (matcher.find()) {
      String node = matcher.group(1);
      String expression = matcher.group(2);
      if (!expression.startsWith(EXPR_PREFIX)
          || !expression.endsWith(EXPR_SUFFIX)) {
        errors.add(String.format("Process %s (%s) contains an invalid expression: %s",
            processName, processClassPath, expression));
        continue;
      }
      Class<?> expected = node.equalsIgnoreCase(CUSTOM_NODE) ? ActivityBehaviour.class : DecisionHandler.class;
      String expressionValue = expression.substring(EXPR_PREFIX.length(),
          expression.length() - EXPR_SUFFIX.length());
      validateEngineCall(actionClass, processName, processClassPath, errors,
          expression, expressionValue, expected);
    }
    return errors;
  }

  private void validateEngineCall(
      Class<? extends BusinessProcessActions<?>> actionClass,
      String processName, String processClassPath, List<String> errors,
      String expression, String invocation, Class<?> expected) {
    boolean execution = invocation.startsWith(ENGINE_EXECUTE_PREFIX);
    if (expected == ActivityBehaviour.class && !execution) {
      errors.add(String.format("Process %s (%s) uses %s...%s but %s...%s is expected in this node: %s",
          processName, processClassPath, ENGINE_EXECUTE_PREFIX, ENGINE_SUFFIX, ENGINE_DECIDE_PREFIX, ENGINE_SUFFIX, expression));
      return;
    } else if (expected == DecisionHandler.class && execution) {
      errors.add(String.format("Process %s (%s) uses %s...%s but %s...%s is expected in this node: %s",
          processName, processClassPath, ENGINE_DECIDE_PREFIX, ENGINE_SUFFIX, ENGINE_EXECUTE_PREFIX, ENGINE_SUFFIX, expression));
      return;
    }
    String call = invocation.substring((execution ? ENGINE_EXECUTE_PREFIX
        : ENGINE_DECIDE_PREFIX).length(),
        invocation.length() - ENGINE_SUFFIX.length());
    Matcher matcher = CALL_PATTERN.matcher(call);
    if (!matcher.find()) {
      errors.add(String.format("Process %s (%s) contains an invalid call: %s",
          processName, processClassPath, expression));
      return;
    }
    String beanName = matcher.group(1);
    String methodName = matcher.group(2);
    String argumentsString = matcher.group(3);

    Class<?> beanClass;
    if (beanName.equals(BusinessProcessInternalServiceImpl.PROCESS_VARIABLE)) {
      beanClass = actionClass;
    } else {
      Object bean = validateBean(processName, processClassPath, errors,
          expression, beanName);
      if (bean == null) {
        // validateBean reports the error.
        return;
      }
      beanClass = bean.getClass();
    }
    String[] arguments = StringUtils.splitPreserveAllTokens(argumentsString,
        ',');
    List<Method> methods = findMethod(beanClass, methodName, arguments.length);
    if (methods.isEmpty()) {
      errors
          .add(String
              .format(
                  "Process %s (%s) contains an invalid method reference, no method %s with %s args: %s",
                  processName, processClassPath, methodName, arguments.length,
                  expression));
    } else if (methods.size() > 1) {
      errors
          .add(String
              .format(
                  "Process %s (%s) contains an invalid method reference, %s methods %s with %s args: %s",
                  processName, processClassPath, methods.size(), methodName,
                  arguments.length, expression));
    } else {
      Method method = methods.get(0);
      if (execution && method.getReturnType() != void.class) {
        errors
        .add(String
            .format(
                "Process %s (%s) contains an invalid method reference, method %s does not return void: %s",
                processName, processClassPath, methods.size(), methodName,
                arguments.length, expression));
      } else if (!execution && method.getReturnType() == void.class) {
        errors
        .add(String
            .format(
                "Process %s (%s) contains an invalid method reference, method %s returns void: %s",
                processName, processClassPath, methods.size(), methodName,
                arguments.length, expression));
      }
    }
  }

  private List<Method> findMethod(Class<?> beanClass, String methodName, int length) {
    try {
      if (length == 0) {
        return Collections.singletonList(beanClass.getMethod(methodName));
      }
      List<Method> methods = new LinkedList<>();
      for (Method method : beanClass.getMethods()) {
        if (method.getName().equals(methodName) && method.getParameterTypes().length == length) {
          methods.add(method);
        }
      }
      return methods;
    } catch (ReflectiveOperationException e) {
      return Collections.emptyList();
    }
  }

  private Object validateBean(String processName,
      String processClassPath, List<String> errors, String expression, String beanName) {
   try {
      return applicationContext.getBean(beanName);
    } catch (BeansException e) {
      errors.add(String.format(
          "Process %s (%s) contains an invalid bean name: %s (%s): %s", processName,
          processClassPath, beanName, e.getMessage(), expression));
      return null;
    }
  }

  /**
   * redeployes all the workflow
   */
  public void deployAllWorkflows() {

    List<String> workflowKeys = new LinkedList<>(workflowMap.keySet());
    Collections.sort(workflowKeys);
    List<String> errors = new LinkedList<>();
    for (final String workflowName : workflowKeys) {
      String path = workflowMap.get(workflowName);
      String contents = readContents(workflowName, path);
      errors.addAll(validate(workflowName, path, contents));
    }
    checkState(errors.isEmpty(), "Errors found in business processes:\n" + StringUtils.join(errors, "\n"));
    for (final String workflowName : workflowKeys) {
      processEngine.execute(new Command<Void>() {
        private static final long serialVersionUID = 1L;

        @Override
        public Void execute(Environment environment) throws Exception {
          deployProcessFromClassPath(workflowName);
          return null;
        }
      });
    }
  }

  @PostConstruct
  public void initialize() throws Exception {
    processEngine.execute(new InitializePropertiesCmd(0));
  }
}
