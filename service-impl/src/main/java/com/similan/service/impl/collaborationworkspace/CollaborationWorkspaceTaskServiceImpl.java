package com.similan.service.impl.collaborationworkspace;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;
import com.similan.domain.entity.collaborationworkspace.Task;
import com.similan.domain.entity.community.SocialActor;
import com.similan.domain.entity.community.SocialPerson;
import com.similan.domain.repository.collaborationworkspace.TaskRepository;
import com.similan.service.api.collaborationworkspace.CollaborationWorkspaceTaskService;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskDto;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskStatus;
import com.similan.service.api.collaborationworkspace.dto.basic.TaskType;
import com.similan.service.api.collaborationworkspace.dto.key.CollaborationWorkspaceKey;
import com.similan.service.api.collaborationworkspace.dto.key.TaskKey;
import com.similan.service.api.collaborationworkspace.dto.operation.NewTaskDto;
import com.similan.service.api.community.dto.key.SocialActorKey;
import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.impl.community.SocialActorMarshaller;
import com.similan.service.internal.api.email.EmailInternalService;
import com.similan.service.internal.api.email.io.NewEmail;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.collaborationworkspace.TaskCreatedEvent;

@Service
public class CollaborationWorkspaceTaskServiceImpl extends ServiceImpl
    implements CollaborationWorkspaceTaskService {
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private EmailInternalService emailMessagingService;
  @Autowired
  private EventInternalService eventInternalService;
  @Autowired
  private SocialActorMarshaller actorMarshaller;
  @Autowired
  private CollaborationWorkspaceMarshaller collaborationWorkspaceMarshaller;

  @Override
  @Transactional
  public TaskDto create(TaskKey taskKey, NewTaskDto newTaskDto) {
    SocialPerson initiator = (SocialPerson) actorMarshaller.unmarshallActorKey(newTaskDto.getCreator(), true);
    SocialPerson assignee = (SocialPerson) actorMarshaller.unmarshallActorKey(newTaskDto.getAssignee(), true);
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller.unmarshallWorkspaceKey(
            taskKey.getWorkspace(), true);

    String name = taskKey.getName();
    String description = newTaskDto.getDescription();
    Date dueDate = newTaskDto.getDueDate();
    TaskType type = newTaskDto.getTaskType();
    Task task = this.taskRepository.create(workspace, initiator, new Date(),
        type, name, description, dueDate, TaskStatus.PENDING, assignee);
    this.taskRepository.save(task);

    /* send email to the assigned actor */
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("workspaceName", workspace.getName());
    String initiatorName = initiator.getFirstName() + " "
        + initiator.getLastName();
    String assigneeName = assignee.getFirstName() + " "
        + assignee.getLastName();

    parameters.put("assignedFrom", initiatorName);
    parameters.put("assignedTo", assigneeName);
    parameters.put("taskName", taskKey.getName());
    parameters.put("taskDescription", newTaskDto.getDescription());
    parameters.put("taskDuedate", newTaskDto.getDueDate().toString());

    List<SocialActor> toList = new ArrayList<SocialActor>();
    toList.add(assignee);

    NewEmail newEmail = new NewEmail(initiator, toList, "taskAssignment.vm",
        parameters);

    this.emailMessagingService.send(newEmail);
    TaskDto taskDto = collaborationWorkspaceMarshaller.marshallTask(task);

    eventInternalService.fire(new TaskCreatedEvent(task.getId()));

    return taskDto;
  }

  @Override
  @Transactional
  public List<TaskDto> getByAssigneeAndDate(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey assigneeKey,
      Date date) {
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    SocialActor assignee = actorMarshaller
        .unmarshallActorKey(assigneeKey, true);
    List<Task> tasks = this.taskRepository.findByWorkspaceAndAssignee(
        workspace, assignee);
    List<TaskDto> taskDtos = collaborationWorkspaceMarshaller.marshallTasks(tasks);
    return taskDtos;
  }

  @Override
  public List<TaskDto> getFutureByAssignee(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey assigneeKey) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<TaskDto> getOverdueByAssignee(
      CollaborationWorkspaceKey workspaceKey, SocialActorKey assigneeKey) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TaskDto get(TaskKey taskKey) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @Transactional
  public List<TaskDto> getAllOpenTasks(CollaborationWorkspaceKey workspaceKey,
      SocialActorKey assigneeKey) {
    SocialActor assignee = actorMarshaller
        .unmarshallActorKey(assigneeKey, true);
    CollaborationWorkspace workspace = collaborationWorkspaceMarshaller
        .unmarshallWorkspaceKey(workspaceKey, true);
    List<Task> tasks = this.taskRepository.findByWorkspaceAndAssigneeAndStatus(
        workspace, assignee, TaskStatus.PENDING);
    List<TaskDto> taskDtos = collaborationWorkspaceMarshaller.marshallTasks(tasks);
    return taskDtos;
  }

  @Override
  @Transactional
  public void cancelTask(TaskKey taskKey) {
    Task cancelTask = collaborationWorkspaceMarshaller.unmarshallTaskKey(taskKey, true);
    cancelTask.setStatus(TaskStatus.CANCELED);
    this.taskRepository.save(cancelTask);

  }

  @Override
  @Transactional
  public void completeTask(TaskKey taskKey) {
    Task openTask = collaborationWorkspaceMarshaller
        .unmarshallTaskKey(taskKey, true);
    openTask.setStatus(TaskStatus.COMPLETE);
    this.taskRepository.save(openTask);
  }

}
