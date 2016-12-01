package com.similan.domain.entity.managementworkspace;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.community.SocialActor;

@Entity(name = "ManagementWorkspaceParticipation")
public class ManagementWorkspaceParticipation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn
  private ManagementWorkspace workspace;

  @ManyToOne
  @JoinColumn
  private SocialActor participant;

  @Column
  private Date joinDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ManagementWorkspace getWorkspace() {
    return workspace;
  }

  public void setManagementWorkspace(ManagementWorkspace workspace) {
    this.workspace = workspace;
  }

  public SocialActor getParticipant() {
    return participant;
  }

  public void setPerticipant(SocialActor participant) {
    this.participant = participant;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(Date joinDate) {
    this.joinDate = joinDate;
  }

}
