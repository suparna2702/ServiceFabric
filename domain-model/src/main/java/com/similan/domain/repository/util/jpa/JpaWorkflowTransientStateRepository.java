package com.similan.domain.repository.util.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.similan.domain.entity.util.WorkflowTransientState;
import com.similan.domain.entity.util.WorkflowTransientStateType;

public interface JpaWorkflowTransientStateRepository extends
                                             JpaRepository<WorkflowTransientState, Long>{
	
	@Query("select state from WorkflowTransientState state where state.memberId=:id and state.stateType=:stateType")
	public WorkflowTransientState findByMemberIdAndType(@Param("id")Long id, @Param("stateType")WorkflowTransientStateType transientStateType);
	
	@Query("select state from WorkflowTransientState state where state.stateType=:stateType")
	public List<WorkflowTransientState> findByType(@Param("stateType")WorkflowTransientStateType stateType);

	@Query("select state from WorkflowTransientState state where state.memberId=:id and state.processInstanceId=:pid")
	public WorkflowTransientState findByMemberIdAndProcessInstanceId(@Param("id")Long id, @Param("pid")String pid);
	
	@Query("select state from WorkflowTransientState state where state.processInstanceId=:pid")
	public WorkflowTransientState findByProcessInstance(@Param("pid")String pid);

}
