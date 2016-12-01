package com.similan.service.rest.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.apache.commons.lang3.ClassUtils;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import scala.collection.JavaConversions;

import com.similan.service.api.configuration.ConfigurationService;
import com.similan.service.api.configuration.dto.basic.ConfigurationDto;
import com.similan.service.rest.ServiceRestRootPackage;
import com.similan.service.rest.base.ClientErrorExceptionMapper;
import com.similan.service.rest.base.GenericExceptionMapper;
import com.similan.service.rest.base.RestApplication;
import com.similan.service.rest.base.SimilanExceptionMapper;
import com.similan.service.rest.base.SimilanResourceComparator;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@Configuration
@ComponentScan(basePackageClasses = ServiceRestRootPackage.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceRestConfiguration {
  @Autowired
  RestApplication restApplication;
  @Autowired
  SimilanResourceComparator resourceComparator;
  @Autowired
  ConfigurationService configurationService;
  @Autowired
  SimilanExceptionMapper similanExceptionMapper;
  @Autowired
  GenericExceptionMapper genericExceptionMapper;
  @Autowired
  ClientErrorExceptionMapper clientErrorExceptionMapper;

  @Bean(destroyMethod = "shutdown")
  public SpringBus cxf() {
    return new SpringBus();
  }

  @Bean
  public JacksonJsonProvider jacksonJsonProvider() {
    return new JacksonJsonProvider();
  }

  @Bean
  public LoggingFeature loggingFeature() {
    return new LoggingFeature();
  }

  @Bean(initMethod = "create")
  public JAXRSServerFactoryBean jaxRsServer() {
    JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
    factory.setApplication(restApplication);
    factory.setServiceBeans(new ArrayList<>(restApplication.getSingletons()));
    factory.setProvider(new JacksonJsonProvider());
    factory.setProvider(similanExceptionMapper);
    factory.setProvider(clientErrorExceptionMapper);
    factory.setProvider(genericExceptionMapper);
    factory.setResourceComparator(resourceComparator);
    factory.setFeatures(Arrays.asList(loggingFeature()));
    configureSwagger(factory);
    return factory;
  }

  @Bean
  public ResourceListingProvider apiResourceListingProvider() {
    return new ResourceListingProvider();
  }

  @Bean
  public ApiDeclarationProvider apiDeclarationProvider() {
    return new ApiDeclarationProvider();
  }

  @Bean
  public ApiListingResourceJSON apiListingResourceJSON() {
    return new ApiListingResourceJSON();
  }

  private void configureSwagger(JAXRSServerFactoryBean factory) {
    factory.setProvider(apiResourceListingProvider());
    factory.setProvider(apiDeclarationProvider());
    factory.setServiceBean(apiListingResourceJSON());
    setupSwagger();
  }

  private void setupSwagger() {
    BeanConfig swaggerConfig = new BeanConfig() {
      @Override
      public scala.collection.immutable.List<Class<?>> classesFromContext(
          Application app, ServletConfig sc) {
        List<Class<?>> apiClasses = new LinkedList<>();
        for (Object api : app.getSingletons()) {
          Class<?> apiClass = api.getClass();
          apiClasses.add(apiClass);
          apiClasses.addAll(ClassUtils.getAllSuperclasses(apiClass));
          apiClasses.addAll(ClassUtils.getAllInterfaces(apiClass));
        }
        for (Class<?> apiClass : app.getClasses()) {
          apiClasses.add(apiClass);
          apiClasses.addAll(ClassUtils.getAllSuperclasses(apiClass));
          apiClasses.addAll(ClassUtils.getAllInterfaces(apiClass));
        }
        return JavaConversions.asScalaBuffer(apiClasses).toList();
      }
    };
    ConfigurationDto configuration = configurationService.get();
    swaggerConfig.setVersion("0.0.1-SNAPSHOT");
    swaggerConfig.setBasePath(configuration.getBaseUrl()
        + RestApplication.API_PATH);
    swaggerConfig.setTitle("Similan API");
    swaggerConfig.setDescription("This the Similan backend API.");
    swaggerConfig.setContact(configuration.getContactEmail());
    swaggerConfig.setLicense("License");
    swaggerConfig.setLicenseUrl(configuration.getBaseUrl() + "api-license");
    swaggerConfig.setScan(true);
  }
}