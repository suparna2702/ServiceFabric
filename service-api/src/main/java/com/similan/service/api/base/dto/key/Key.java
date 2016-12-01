package com.similan.service.api.base.dto.key;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class Key implements IKey {
  @Deprecated
  @JsonIgnore
  private Long id;
  @Deprecated
  @JsonIgnore
  public Long getId() {
    return id;
  }
  @Deprecated
  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public final String toString() {
    return getEntityType().getUniqueSegment() + ":"
        + StringUtils.join(getEntityType().getPath(this), '/');
  }

  @Override
  public final String getUri() {
    return getEntityType().getUri(this);
  }

  @Override
  public final int hashCode() {
    return Arrays.hashCode(getEntityType().getPath(this));
  }

  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof IKey)) {
      return false;
    }
    IKey otherKey = (IKey) other;
    String[] otherKeyPath = otherKey.getEntityType().getPath(otherKey);
    String[] keyPath = getEntityType().getPath(this);
    return Arrays.equals(keyPath, otherKeyPath);
  }

}
