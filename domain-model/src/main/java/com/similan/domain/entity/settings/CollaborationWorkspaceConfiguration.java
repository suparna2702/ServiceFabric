package com.similan.domain.entity.settings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.collaborationworkspace.CollaborationWorkspace;

@Getter
@Setter
@Entity(name = "CollaborationWorkspaceConfiguration")
public class CollaborationWorkspaceConfiguration {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;
  
  @ManyToOne
  @JoinColumn
  CollaborationWorkspace workspace;
  
  @Column
  private Boolean showParticipants = Boolean.TRUE;
  

}
