package com.similan.domain.entity.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name="WorkflowTransientState")
@Getter
@Setter
@ToString
public class WorkflowTransientState {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column
	private Long memberId;
	
	@Enumerated(EnumType.STRING)
	@Column
	private WorkflowTransientStateType stateType;
	
	@Column
	private String processInstanceId;
	
	@Column
	private Date timeStamp = new Date();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<WorkflowTransientStateAttribute> attributes = new ArrayList<WorkflowTransientStateAttribute>();
	
	public WorkflowTransientStateAttribute getAttributeByName(String attrName){
		WorkflowTransientStateAttribute retAttr = null;
		
		for(WorkflowTransientStateAttribute attr : attributes){
			if(attr.getAttributeName().equalsIgnoreCase(attrName)) {
				retAttr = attr;
				break;
			}
		}
		
		return retAttr;
	}
}
