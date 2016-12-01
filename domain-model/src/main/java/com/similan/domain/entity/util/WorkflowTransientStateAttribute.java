package com.similan.domain.entity.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name="WorkflowTransientStateAttribute")
@Getter
@Setter
@ToString
public class WorkflowTransientStateAttribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	@Column(length=800)
	private String attributeName;
	
	@Column
	private String attributeType;
	
	@Column(length=8000)
	private String attributeValue;


}
