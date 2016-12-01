package com.similan.domain.entity.product;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.similan.domain.entity.common.IDomainEntity;
import com.similan.domain.entity.community.SocialActor;
import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.product.ProductNodeType;

@Entity(name = "Product")
public class Product implements IDomainEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String name;

  @Column
  private String productNodeCode;

  @Column
  private String imageLocation;

  @Column(length = 5000)
  private String description;

  @Column(length = 500)
  private String domainId = UUID.randomUUID().toString();

  @ManyToOne
  @JoinColumn
  private SocialActor owner;

  @Column
  @Enumerated(EnumType.STRING)
  private ProductNodeType productNodeType;

  @OneToMany
  @JoinColumn
  private List<ProductFeature> features;

  @OneToMany
  @JoinColumn
  private List<ProductTag> tags;

  public String getDomainId() {
    return domainId;
  }

  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProductNodeCode() {
    return productNodeCode;
  }

  public void setProductNodeCode(String productNodeCode) {
    this.productNodeCode = productNodeCode;
  }

  public String getImageLocation() {
    return imageLocation;
  }

  public void setImageLocation(String imageLocation) {
    this.imageLocation = imageLocation;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public SocialActor getOwner() {
    return owner;
  }

  public void setOwner(SocialActor owner) {
    this.owner = owner;
  }

  public ProductNodeType getProductNodeType() {
    return productNodeType;
  }

  public void setProductNodeType(ProductNodeType productNodeType) {
    this.productNodeType = productNodeType;
  }

  public List<ProductFeature> getFeatures() {
    return features;
  }

  public void setFeatures(List<ProductFeature> features) {
    this.features = features;
  }

  public List<ProductTag> getTags() {
    return tags;
  }

  public void setTags(List<ProductTag> tags) {
    this.tags = tags;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.PRODUCT;
  }

}
