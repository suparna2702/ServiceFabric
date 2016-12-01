package com.similan.domain.entity.asset;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "AssetMetadata")
public class AssetMetadata {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = true, length=500)
  private String contentType;

  @Column(nullable = true, length=1000)
  private String contentDisposition;

  @OneToMany(mappedBy = "metadata")
  private List<AssetAdditionalMetadata> additionals;

  protected AssetMetadata() {
  }

  public AssetMetadata(String contentType, String contentDisposition) {
    super();
    this.contentType = contentType;
    this.contentDisposition = contentDisposition;
  }

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getContentDisposition() {
    return contentDisposition;
  }

  public void setContentDisposition(String contentDisposition) {
    this.contentDisposition = contentDisposition;
  }

  public List<AssetAdditionalMetadata> getAdditionals() {
    return additionals;
  }

}
