package com.similan.domain.entity.wall.partner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.partner.PartnerProgramDefinition;
import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "PartnerProgramWallEntry")
public abstract class PartnerProgramWallEntry extends WallEntry {
  
  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private PartnerProgramDefinition partnerProgram;

  protected PartnerProgramWallEntry() {

  }

  public PartnerProgramWallEntry(WallEntryType type, int number, Date date) {
    super(type, number, date);
  }

  public PartnerProgramDefinition getPartnerProgram() {
    return partnerProgram;
  }

  public void setPartnerProgram(PartnerProgramDefinition partnerProgram) {
    this.partnerProgram = partnerProgram;
  }

}
