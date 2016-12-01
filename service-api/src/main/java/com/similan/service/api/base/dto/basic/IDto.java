package com.similan.service.api.base.dto.basic;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.similan.service.api.base.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface IDto {

}
