package com.similan.framework.workflow.impl.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.jbpm.internal.log.Log;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.wire.WireContext;
import org.jbpm.pvm.internal.wire.WireDefinition;
import org.jbpm.pvm.internal.wire.WireException;
import org.jbpm.pvm.internal.wire.descriptor.AbstractDescriptor;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public class EntityManagerSessionDescriptor extends AbstractDescriptor {
  private static final long serialVersionUID = 1L;
  private static final Log log = Log
      .getLog(EntityManagerSessionDescriptor.class.getName());

  @Setter
  @Getter
  protected String factoryName;

  public Object construct(WireContext wireContext) {
    EnvironmentImpl environment = EnvironmentImpl.getCurrent();
    if (environment == null) {
      throw new WireException("no environment");
    }

    // get the hibernate-entity-manager-factory
    EntityManagerFactory entityManagerFactory = null;
    if (factoryName != null) {
      entityManagerFactory = (EntityManagerFactory) wireContext
          .get(factoryName);
    } else {
      entityManagerFactory = environment.get(EntityManagerFactory.class);
    }
    if (entityManagerFactory == null) {
      throw new WireException("couldn't find hibernate-entity-manager-factory "
          + (factoryName != null ? "'" + factoryName + "'" : "by type ")
          + "to open a hibernate-session");
    }

    // open the hibernate-session
    EntityManager entityManager = null;
    if (log.isTraceEnabled())
      log.trace("getting current hibernate session");
    entityManager = EntityManagerFactoryUtils
        .getTransactionalEntityManager(entityManagerFactory);
    if (entityManager == null) {
      throw new UnsupportedOperationException(
          "Entity manager not found. Did you initiate a transaction?");
    }

    Session session = entityManager.unwrap(Session.class);
    return session;
  }

  public Class<?> getType(WireDefinition wireDefinition) {
    return SessionImpl.class;
  }

}
