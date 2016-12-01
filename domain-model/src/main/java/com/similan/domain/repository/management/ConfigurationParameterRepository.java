package com.similan.domain.repository.management;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.management.ConfigurationParameter;
import com.similan.domain.repository.management.jpa.JpaConfigurationParameterRepository;

/**
 * This interface stores the global system settings such as the host URL
 * 
 * @author suparnap
 * 
 */
@Repository
public class ConfigurationParameterRepository {
  @Autowired
  private JpaConfigurationParameterRepository repository;

	public ConfigurationParameter findByBeanNameAndPropertyName(
			String beanName, String propertyName) {
    return repository.findByBeanNameAndPropertyName(
			beanName, propertyName);
  }
	
	public List<ConfigurationParameter> findAll() {
    return repository.findAll();
  }
	
	public ConfigurationParameter save(ConfigurationParameter entity) {
    return repository.save(entity);
  }

	public ConfigurationParameter create(String type, String beanName,
			String propertyName, String value) {
		ConfigurationParameter configurationParameter = new ConfigurationParameter(type, beanName,
								propertyName, value);
		return configurationParameter;
	}
}
