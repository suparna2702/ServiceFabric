package com.similan.domain.entity.wall;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.similan.domain.entity.common.GenericReference;
import com.similan.domain.entity.common.IDomainEntity;
import com.similan.service.api.base.dto.key.EntityType;

@Entity(name = "Wall")
public class Wall implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  private GenericReference<IWallContainer> container;

  @OneToMany(mappedBy = "wall")
  @OrderBy("date desc, number desc")
  private List<WallEntry> entries;

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private WallEntry lastEntry;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.WALL;
  }

  public GenericReference<IWallContainer> getContainer() {
    return container;
  }

  public void setContainer(GenericReference<IWallContainer> container) {
    this.container = container;
  }

  public List<WallEntry> getEntries() {
    return entries;
  }

  public void setLastEntry(WallEntry lastEntry) {
    this.lastEntry = lastEntry;
  }

  public WallEntry getLastEntry() {
    return lastEntry;
  }
}
