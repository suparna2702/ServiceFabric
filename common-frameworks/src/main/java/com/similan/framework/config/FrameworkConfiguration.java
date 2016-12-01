package com.similan.framework.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.similan.framework.FrameworkPackage;
import com.similan.framework.template.TemplateResourceLoader;
import com.similan.framework.util.ApiCredentials;

@Configuration
@ComponentScan(basePackageClasses = FrameworkPackage.class)
public class FrameworkConfiguration {
  @Bean
  public ApiCredentials awsCredentials() {
    return new ApiCredentials();
  }

  @Bean
  public BasicAWSCredentials realAwsCredentials() {
    ApiCredentials awsCredentials = awsCredentials();
    return new BasicAWSCredentials(awsCredentials.getClientId(),
        awsCredentials.getClientSecret());
  }

  @Bean
  public AmazonSimpleEmailServiceClient awsEmailClient() {
    return new AmazonSimpleEmailServiceClient(realAwsCredentials());
  }

  @Bean
  public org.springframework.mail.javamail.JavaMailSenderImpl springJavaMailSender() {
    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost("localhost");
    return sender;
  }

  @Bean
  public VelocityEngineFactoryBean velocityEngine() {
    VelocityEngineFactoryBean engineFactory = new VelocityEngineFactoryBean();
    Properties properties = new Properties();
    properties.setProperty("resource.loader", "string");
    properties.setProperty("string.resource.loader.class",
        TemplateResourceLoader.class.getName());
    engineFactory.setVelocityProperties(properties);
    return engineFactory;
  }
}
