package com.similan.domain.config;

import javax.sql.DataSource;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;

import com.similan.domain.DomainPackage;

@Configuration
@ComponentScan(basePackageClasses = DomainPackage.class)
public class DomainConfiguration {
  static {
    SLF4JBridgeHandler.install();
  }
  
  @Autowired
  private DataSource dataSource;

  @Bean
  public HibernateExceptionTranslator hibernateExceptionTranslator() {
    return new HibernateExceptionTranslator();
  }
}
