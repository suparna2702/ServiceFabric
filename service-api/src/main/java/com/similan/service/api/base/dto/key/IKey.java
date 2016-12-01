package com.similan.service.api.base.dto.key;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.similan.service.api.base.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface IKey {
  @XmlAttribute
  EntityType getEntityType();

  @XmlAttribute
  String getUri();
}
