package com.similan.service.internal.impl.asset.store;

public abstract class AbstractAssetStore implements AssetStore {

  private final String id;

  public AbstractAssetStore(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

}
