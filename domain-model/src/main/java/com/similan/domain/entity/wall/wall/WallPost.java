package com.similan.domain.entity.wall.wall;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import com.similan.domain.entity.wall.WallEntry;
import com.similan.service.api.wall.dto.basic.WallEntryType;

@Entity(name = "WallPost")
@DiscriminatorValue("WALL_POST")
@Setter
@Getter
public class WallPost extends WallEntry {
  @Column(length = 5000)
  private String content;
  
  @OneToOne
  @JoinColumn
  private LinkReference link;

  protected WallPost() {
  }

  public WallPost(int number, Date date, String content) {
    super(WallEntryType.WALL_POST, number, date);
    this.content = content;
  }
}
