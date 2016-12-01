package com.similan.framework.workflow.impl.hibernate;

import java.lang.reflect.Field;
import java.sql.Blob;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.lob.BlobStrategyBlob;
import org.jbpm.pvm.internal.lob.Lob;

public class Hibernate4BlobStrategyBlob extends BlobStrategyBlob {
  private static final Field cachedBytesField;
  private static final Field blobField;

  static {
    try {
      cachedBytesField = Lob.class.getDeclaredField("cachedBytes");
      cachedBytesField.setAccessible(true);
      blobField = Lob.class.getDeclaredField("blob");
      blobField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      throw new NoSuchFieldError("Error initializing strategy: " + e);
    }
  }

  public void set(byte[] bytes, Lob lob) {
    try {
      if (bytes != null) {
        cachedBytesField.set(lob, bytes);
        Session session = EnvironmentImpl.getFromCurrent(Session.class, true);
        Blob blob = Hibernate.getLobCreator(session).createBlob(bytes);
        blobField.set(lob, blob);
      }
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
