package com.similan.domain.entity.wall.partner;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.similan.domain.entity.partner.Partnership;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "PartnerProgramJoinWallEntry")
@DiscriminatorValue("PARTNER_JOINED")
public class PartnerProgramJoinWallEntry extends PartnerProgramWallEntry {
  
  @ManyToOne(optional = true)
  @JoinColumn(nullable = true)
  private Partnership partnership;
  
  public PartnerProgramJoinWallEntry(){
    
  }
  
  public PartnerProgramJoinWallEntry(int number, Date date){
    super(WallEntryType.PARTNER_JOINED, number, date);
  }

  public Partnership getPartnership() {
    return partnership;
  }

  public void setPartnership(Partnership partnership) {
    this.partnership = partnership;
  }

}
