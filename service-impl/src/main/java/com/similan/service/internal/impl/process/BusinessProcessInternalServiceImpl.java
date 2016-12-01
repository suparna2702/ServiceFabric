package com.similan.service.internal.impl.process;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.process.BusinessProcess;
import com.similan.domain.repository.process.BusinessProcessRepository;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResumeRequest;
import com.similan.framework.workflow.impl.WorkflowDeploymentManager;
import com.similan.service.internal.api.process.BusinessProcessInternalService;

@Service
public class BusinessProcessInternalServiceImpl implements
    BusinessProcessInternalService {
  public static final String PROCESS_VARIABLE = "process";
  public static final String BASE_PROCESS_PACKAGE = BusinessProcess.class
      .getPackage().getName() + '.';
  public static final String BASE_ACTIONS_PACKAGE = BusinessProcessActions.class
      .getPackage().getName() + '.';
  public static final String ACTIONS_SUFFIX = "Actions";
  public static final String PROCESS_SUFFIX = "Process";

  @Autowired
  private WorkflowDeploymentManager deploymentManager;
  @Autowired
  private WorkflowManager workflowManager;
  @Autowired
  private AutowireCapableBeanFactory beanFactory;
  @Autowired
  private BusinessProcessRepository businessProcessRepository;

  private String getSimpleName(Class<?> processClass) {
    String name = processClass.getName();
    checkArgument(name.startsWith(BASE_PROCESS_PACKAGE),
        "Invalid process class package location: " + name);
    checkArgument(name.endsWith(PROCESS_SUFFIX), "Invalid process class name: "
        + name);
    return name.substring(BASE_PROCESS_PACKAGE.length(), name.length()
        - PROCESS_SUFFIX.length());
  }

  private String getDefinitionKey(Class<?> processClass) {
    return getSimpleName(processClass).replace('.', '_');
  }

  private String getInstanceKey(BusinessProcess process) {
    return process.getUuid();
  }

  private String getFullInstanceKey(BusinessProcess process) {
    return getDefinitionKey(process.getClass()) + '.' + process.getUuid();
  }

  @Transactional
  public void deployModified() {
    deploymentManager.deployAllWorkflows();
  }

  @SuppressWarnings("unchecked")
  public BusinessProcessActions<?> createActions(BusinessProcess process) {
    String name = getSimpleName(process.getClass());
    String actionsName = BASE_ACTIONS_PACKAGE + name + ACTIONS_SUFFIX;
    BusinessProcessActions<BusinessProcess> actions;
    try {
      Class<?> actionsClass = Class.forName(actionsName, true,
          BusinessProcessActions.class.getClassLoader());
      actions = (BusinessProcessActions<BusinessProcess>) actionsClass
          .newInstance();
    } catch (ReflectiveOperationException e) {
      throw new IllegalArgumentException(
          "Could not create business process actions for business process: "
              + process.getClass().getName(), e);
    }

    actions.setProcess(process);
    beanFactory.autowireBean(actions);
    return actions;
  }

  @Override
  public <PROCESS extends BusinessProcess> void initiate(PROCESS process) {
    checkArgument(process.getUuid() == null, "Process is not new");
    process.setUuid(UUID.randomUUID().toString());
    businessProcessRepository.save(process);
    BusinessProcessActions<?> actions = createActions(process);
    String definitionKey = getDefinitionKey(process.getClass());
    String instanceKey = getInstanceKey(process);
    WorkflowInitialRequest request = new WorkflowInitialRequest();
    request.setFlowDefinitionName(definitionKey);
    request.setWorkflowInstanceId(instanceKey);
    request.setExecutionParameters(Collections.<String, Object> singletonMap(
        PROCESS_VARIABLE, actions));
    workflowManager.initiate(request);
  }

  public <PROCESS extends BusinessProcess> void resume(PROCESS process) {
    String fullInstanceKey = getFullInstanceKey(process);
    WorkflowResumeRequest request = new WorkflowResumeRequest();
    request.setWorkflowInstanceId(fullInstanceKey);
    workflowManager.resume(request);
  }
}
