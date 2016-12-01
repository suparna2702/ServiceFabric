package com.similan.domain.entity.document;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.similan.domain.entity.asset.Asset;
import com.similan.service.api.document.dto.basic.DocumentViewerItemType;

@Entity(name = "DocumentViewerAsset")
@DiscriminatorValue("ASSET")
public class DocumentViewerAsset extends DocumentViewerItem {

  @OneToOne(optional = true)
  @JoinColumn(nullable = true)
  private Asset asset;

  protected DocumentViewerAsset() {
  }

  public DocumentViewerAsset(String name) {
    super(name, DocumentViewerItemType.ASSET);
  }

  public Asset getAsset() {
    return asset;
  }

  public void setAsset(Asset asset) {
    this.asset = asset;
  }
}
