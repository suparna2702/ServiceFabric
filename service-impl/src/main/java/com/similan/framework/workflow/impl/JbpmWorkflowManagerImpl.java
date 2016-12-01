package com.similan.framework.workflow.impl;

import java.util.Map;
import java.util.concurrent.locks.Lock;

import lombok.extern.slf4j.Slf4j;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.similan.framework.workflow.api.LockManager;
import com.similan.framework.workflow.api.WorkflowContextVariables;
import com.similan.framework.workflow.api.WorkflowInitialRequest;
import com.similan.framework.workflow.api.WorkflowManager;
import com.similan.framework.workflow.api.WorkflowResponse;
import com.similan.framework.workflow.api.WorkflowResponseCode;
import com.similan.framework.workflow.api.WorkflowResumeRequest;

/**
 * 
 * @author suparnap
 * 
 */
@Slf4j
@Component
public class JbpmWorkflowManagerImpl implements WorkflowManager {
  @Autowired
  private ExecutionService executionService;
  @Autowired
  private ProcessEngine processEngine;
  @Autowired
  protected RepositoryService repositoryService;
  @Autowired
  private LockManager lockManager;

  /**
   * initiates workflow
   */
  public WorkflowResponse initiate(WorkflowInitialRequest request) {

    /* get the parameter */
    ProcessInstance processInstance = null;
    Map<String, Object> parameterMap = request.getExecutionParameters();

    processInstance = this.executionService
        .startProcessInstanceByKey(request.getFlowDefinitionName(),
            parameterMap, request.getWorkflowInstanceId());

    log.info("executed workfloe instance " + processInstance.getId());

    WorkflowResponse response = null;
    /* get the work flow execution result */
    if (this.executionService.findProcessInstanceById(processInstance.getId()) != null) {
      response = (WorkflowResponse) this.executionService.getVariable(
          processInstance.getId(), WorkflowContextVariables.WORKFLOW_RESULT);
    }

    if (response == null) {
      response = new WorkflowResponse();
      log.info("Process instance id " + processInstance.getId());
      response.setProcessInstanceId(processInstance.getId());
      response.setResponseCode(WorkflowResponseCode.Success);
    }

    return response;
  }

  /**
   * initiates workflow
   */
  public void initiateWithoutResponse(WorkflowInitialRequest request) {

    /* get the parameter */
    ProcessInstance processInstance = null;
    Map<String, Object> parameterMap = request.getExecutionParameters();

    if (parameterMap != null) {
      processInstance = this.executionService.startProcessInstanceByKey(
          request.getFlowDefinitionName(), parameterMap);
    } else {
      processInstance = this.executionService.startProcessInstanceByKey(request
          .getFlowDefinitionName());
    }

    log.info("executed workfloe instance " + processInstance.getId());
  }

  /**
   * resumes request
   */
  public WorkflowResponse resume(WorkflowResumeRequest request) {

    final Lock lock = this.lockManager.acquireLock(request
        .getWorkflowInstanceId());
    lock.lock();
    boolean destroyLock = false;
    WorkflowResponse response = new WorkflowResponse();
    response.setProcessInstanceId(request.getWorkflowInstanceId());
    response.setResponseCode(WorkflowResponseCode.Success);

    try {

      /* check for end of process instance */
      ProcessInstance processInstance = this.executionService
          .findProcessInstanceById(request.getWorkflowInstanceId());
      if (processInstance == null) {
        log.info("Process instance does not exist. We cannot resume "
            + request.getWorkflowInstanceId());
        response.setResponseCode(WorkflowResponseCode.WonflowAlreadyEnded);
        return response;

      }

      /* get the parameter */
      Map<String, Object> parameterMap = request.getExecutionParameters();

      this.executionService.signalExecutionById(
          request.getWorkflowInstanceId(), parameterMap);

    } catch (Exception exp) {
      log.error("Failure to execute workflow ", exp);
      response.setResponseCode(WorkflowResponseCode.Failure);

    } finally {
      this.releaseLock(request.getWorkflowInstanceId(), lock, destroyLock);
    }

    return response;

  }

  /**
   * Releases the given lock, after the transaction completes if possible. If
   * there's no synchronization active then the lock will be immediately
   * released and a warning will be logged (as there are potential race
   * conditions anyway).
   * 
   * @param instanceId
   *          The workflow instance id, for logging purposes
   * @param lock
   *          The lock to release, cannot be null.
   * @param destroy
   *          Whether to remove the lock from the locks map.
   */
  private void releaseLock(final String string, final Lock lock,
      final boolean destroy) {

    if (!TransactionSynchronizationManager.isSynchronizationActive()) {
      log
          .warn("Transaction Synchronization is not active: "
              + "POTENTIAL RACE CONDITION with workflow instanceid " + string);

      if (destroy) {
        this.lockManager.destroyLock(string);
      }
      lock.unlock();
    } else {

      /*
       * This log statement will provide the time just before registering
       * callback with Spring transaction synchronization manager
       */
      log.info("About to register lock with the synchronization manager for workflow "
              + string);
      TransactionSynchronizationManager
          .registerSynchronization(new TransactionSynchronizationAdapter() {
            /**
             * In future we may think of providing a counter for transaction
             * completion time (Suparna)
             */
            @Override
            public void beforeCompletion() {
              /*
               * This will provide time stamp just before registering commiting
               * transaction
               */
              log.info("Just before completing the transaction for workflow instance"
                      + string);
            }

            @Override
            public void afterCompletion(final int status) {
              log.info("Releasing lock for workflow instance " + string);
              if (destroy) {
                JbpmWorkflowManagerImpl.this.lockManager.destroyLock(string);
              }
              lock.unlock();
            }
          });

      log.info("Transaction synchronization registered successfully "
              + "for workflow instance " + string
              + ". The lock will be released when the transaction completes");
    }
  }

  @Override
  public Object getVariable(String workflowInstanceId, String variable) {
    return processEngine.getExecutionService().getVariable(workflowInstanceId,
        variable);
  }
}
