package com.similan.framework.workflow.impl.hibernate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.jbpm.api.ProcessEngine;
import org.jbpm.pvm.internal.cfg.ConfigurationImpl;
import org.jbpm.pvm.internal.processengine.ProcessEngineImpl;
import org.jbpm.pvm.internal.processengine.SpringHelper;
import org.jbpm.pvm.internal.processengine.SpringProcessEngine;
import org.jbpm.pvm.internal.wire.descriptor.ProvidedObjectDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityManagerSpringHelper extends SpringHelper {

  @Autowired
  private HibernateEntityManagerFactory entityManagerFactory;

  @Override
  public ProcessEngine createProcessEngine() {
    SessionFactory sessionFactory = entityManagerFactory.getSessionFactory();
    ConfigurationImpl configuration = new ConfigurationImpl().springInitiated(
        applicationContext).setHibernateSessionFactory(sessionFactory).setResource(jbpmCfg);
    configuration
        .getProcessEngineWireContext()
        .getWireDefinition()
        .addDescriptor(new ProvidedObjectDescriptor(entityManagerFactory, true));
    processEngine = buildProcessEngine(configuration);

    return processEngine;
  }

  private ProcessEngine buildProcessEngine(ConfigurationImpl configuration) {
    SpringProcessEngine engine = new SpringProcessEngine();
    try {
      Field applicationContextField = SpringProcessEngine.class
          .getDeclaredField("applicationContext");
      applicationContextField.setAccessible(true);
      applicationContextField
          .set(engine, configuration.getApplicationContext());
      Method initializeProcessEngineMethod = ProcessEngineImpl.class
          .getDeclaredMethod("initializeProcessEngine", ConfigurationImpl.class);
      initializeProcessEngineMethod.setAccessible(true);
      initializeProcessEngineMethod.invoke(engine, configuration);
    } catch (NoSuchFieldException | SecurityException
        | IllegalArgumentException | IllegalAccessException
        | NoSuchMethodException | InvocationTargetException e) {
      throw new IllegalStateException("Could not initialize process engine.", e);
    }
    return engine;
  }

  public void setEntityManagerFactory(HibernateEntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }
}
