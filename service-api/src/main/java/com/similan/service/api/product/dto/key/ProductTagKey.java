package com.similan.service.api.product.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.EntityType;
import com.similan.service.api.base.dto.key.Key;

@Deprecated
public class ProductTagKey extends Key {

  @XmlElement
  private ProductKey product;

  @XmlAttribute
  private String name;

  @XmlAttribute
  private Long id;

  public ProductTagKey() {
  }

  public ProductTagKey(String productOwnerName, String productName, String name) {
    this(new ProductKey(productOwnerName, productName), name);
  }

  public ProductTagKey(ProductKey product, String name) {
    this.name = name;
    this.product = product;
  }

  public ProductKey getProduct() {
    return product;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.PRODUCT_TAG;
  }

}
