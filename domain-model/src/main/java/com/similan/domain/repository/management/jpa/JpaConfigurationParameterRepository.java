package com.similan.domain.repository.management.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.similan.domain.entity.management.ConfigurationParameter;

/**
 * This interface stores the global system settings such as the host URL
 * 
 * @author suparnap
 * 
 */
@Repository
public interface JpaConfigurationParameterRepository extends
		JpaRepository<ConfigurationParameter, Long> {

	ConfigurationParameter findByBeanNameAndPropertyName(String beanName,
			String propertyName);

}
