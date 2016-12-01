package com.similan.framework.dto.script;

import java.io.Serializable;

public class BeanInfoDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;

  private String className;

  private boolean singleton;

  public BeanInfoDto() {
  }

  public BeanInfoDto(String name, String className, boolean singleton) {
    this.name = name;
    this.className = className;
    this.singleton = singleton;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public boolean isSingleton() {
    return singleton;
  }

  public void setSingleton(boolean singleton) {
    this.singleton = singleton;
  }
}
