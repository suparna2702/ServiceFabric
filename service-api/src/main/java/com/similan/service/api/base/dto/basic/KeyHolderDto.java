package com.similan.service.api.base.dto.basic;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.similan.service.api.base.dto.key.IKey;

public class KeyHolderDto<ConcreteKey extends IKey> extends Dto implements
    IKeyHolderDto<ConcreteKey> {

  private ConcreteKey key;

  protected KeyHolderDto() {
  }

  public KeyHolderDto(ConcreteKey key) {
    this.key = key;
  }

  @Override
  public ConcreteKey getKey() {
    return key;
  }

  @Override
  public String toString() {
    return key.toString()
        + ":"
        + new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .setExcludeFieldNames("key").build();
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof IKeyHolderDto)) {
      return false;
    }
    IKey otherKey = ((IKeyHolderDto<?>) other).getKey();
    return key.equals(otherKey);
  }

}
