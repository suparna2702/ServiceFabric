package com.similan.service.api.base.dto.basic;

import javax.xml.bind.annotation.XmlElement;

import com.similan.service.api.base.dto.key.IKey;

public interface IKeyHolderDto<ConcreteKey extends IKey> extends IDto {

  @XmlElement
  ConcreteKey getKey();

}
