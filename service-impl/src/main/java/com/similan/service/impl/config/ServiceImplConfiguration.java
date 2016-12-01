package com.similan.service.impl.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.apache.tika.Tika;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.ManagementService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.gravity.goose.Goose;
import com.similan.framework.box.BoxClient;
import com.similan.framework.workflow.impl.hibernate.EntityManagerSpringHelper;
import com.similan.service.impl.ServiceImplPackage;
import com.similan.service.internal.ServiceInternalPackage;
import com.similan.service.internal.impl.linkreference.goose.AlternativeConfiguration;
import com.similan.service.internal.impl.linkreference.goose.AlternativeGoose;

@Configuration
@ComponentScan(basePackageClasses = { ServiceImplPackage.class,
    ServiceInternalPackage.class })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceImplConfiguration {
  @Autowired
  AWSCredentials awsCredentials;

  @Bean
  public AmazonS3Client s3client() {
    return new AmazonS3Client(awsCredentials);
  }

  @Bean
  public Tika tika() {
    return new Tika();
  }

  @Bean
  public com.gravity.goose.Configuration gooseConfiguration() {
    return new AlternativeConfiguration();
  }

  @Bean
  public Goose goose() {
    return new AlternativeGoose(gooseConfiguration());
  }

  @Bean
  public BoxClient boxClient() {
    return new BoxClient();
  }

  @Configuration
  public static class ServiceImplJbpmConfiguration {
    @Bean
    public EntityManagerSpringHelper jbpmSpringHelper() {
      EntityManagerSpringHelper helper = new EntityManagerSpringHelper();
      helper.setJbpmCfg("com/similan/framework/jbpm.cfg.xml");
      return helper;
    }

    @Bean
    public ProcessEngine jbpmProcessEngine() {
      return jbpmSpringHelper().createProcessEngine();
    }

    @Bean
    public RepositoryService jbpmRepositoryService() {
      return jbpmProcessEngine().getRepositoryService();
    }

    @Bean
    public ExecutionService jbpmExecutionService() {
      return jbpmProcessEngine().getExecutionService();
    }

    @Bean
    public TaskService jbpmTaskService() {
      return jbpmProcessEngine().getTaskService();
    }

    @Bean
    public HistoryService jbpmHistoryService() {
      return jbpmProcessEngine().getHistoryService();
    }

    @Bean
    public ManagementService jbpmManagementService() {
      return jbpmProcessEngine().getManagementService();
    }
  }
}
