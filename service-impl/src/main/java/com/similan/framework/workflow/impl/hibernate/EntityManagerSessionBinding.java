package com.similan.framework.workflow.impl.hibernate;

import org.jbpm.pvm.internal.wire.binding.HibernateSessionBinding;
import org.jbpm.pvm.internal.xml.Parse;
import org.jbpm.pvm.internal.xml.Parser;
import org.w3c.dom.Element;

public class EntityManagerSessionBinding extends HibernateSessionBinding {

  public EntityManagerSessionBinding() {
    setTagName("hibernate-entity-manager");
  }

  public Object parse(Element element, Parse parse, Parser parser) {
    EntityManagerSessionDescriptor descriptor = new EntityManagerSessionDescriptor();

    if (element.hasAttribute("factory")) {
      descriptor.setFactoryName(element.getAttribute("factory"));
    }

    return descriptor;
  }
}
