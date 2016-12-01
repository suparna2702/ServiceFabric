package com.similan.domain.entity.asset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "AssetAdditionalMetadata")
public class AssetAdditionalMetadata {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(nullable = false)
  private AssetMetadata metadata;

  @Column(nullable = false, length=500)
  private String name;

  @Column(nullable = true, length=1500)
  private String value;

  protected AssetAdditionalMetadata() {
  }

  public AssetAdditionalMetadata(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AssetMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(AssetMetadata metadata) {
    this.metadata = metadata;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
