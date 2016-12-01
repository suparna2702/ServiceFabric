package com.similan.domain.entity.management;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ConfigurationParameter")
@Getter
@Setter
@ToString
public class ConfigurationParameter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String beanName;

  @Column(nullable = false)
  private String propertyName;

  @Column(nullable = false)
  private String value;

  protected ConfigurationParameter() {
  }

  public ConfigurationParameter(String type, String beanName,
      String propertyName, String value) {
    this.type = type;
    this.beanName = beanName;
    this.propertyName = propertyName;
    this.value = value;
  }

}
