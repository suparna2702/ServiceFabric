package com.similan.service.api.product.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

public class ProductFeatureKey extends Key {

  @XmlElement
  private ProductKey product;

  @XmlAttribute
  private String domainId;

  @XmlAttribute
  private Long id;

  public ProductFeatureKey() {
  }

  public ProductFeatureKey(String productOwnerName, String productName,
      String domainId) {
    this(new ProductKey(productOwnerName, productName), domainId);
  }

  public ProductFeatureKey(ProductKey product, String domainId) {
    this.domainId = domainId;
    this.product = product;
  }

  public ProductKey getProduct() {
    return product;
  }

  public String getName() {
    return domainId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.PRODUCT_FEATURE;
  }

}
