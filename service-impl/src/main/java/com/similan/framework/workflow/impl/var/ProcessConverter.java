package com.similan.framework.workflow.impl.var;

import java.lang.reflect.Field;
import java.util.Map;

import org.jbpm.pvm.internal.env.Context;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.hibernate.ConverterType;
import org.jbpm.pvm.internal.model.ScopeInstanceImpl;
import org.jbpm.pvm.internal.type.Converter;
import org.jbpm.pvm.internal.type.Variable;

import com.similan.domain.entity.process.BusinessProcess;
import com.similan.service.internal.api.process.BusinessProcessInternalService;
import com.similan.service.internal.impl.process.BusinessProcessActions;

public class ProcessConverter implements Converter {
  private static final long serialVersionUID = 1L;

  private static final String NAME = "process-hibid";

  static {
    try {
      Field converterNamesField = ConverterType.class
          .getDeclaredField("converterNames");
      converterNamesField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<Class<?>, String> converterNames = (Map<Class<?>, String>) converterNamesField
          .get(null);
      Field convertersField = ConverterType.class
          .getDeclaredField("converters");
      convertersField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<String, Converter> converters = (Map<String, Converter>) convertersField
          .get(null);
      converterNames.put(ProcessConverter.class, NAME);
      converters.put(NAME, new ProcessConverter());
    } catch (ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  @Override
  public boolean supports(Object value, ScopeInstanceImpl scopeInstance,
      Variable variable) {
    return value instanceof BusinessProcessActions;
  }

  @Override
  public Object convert(Object o, ScopeInstanceImpl scopeInstance,
      Variable variable) {
    return ((BusinessProcessActions<?>) o).getProcess();
  }

  @Override
  public Object revert(Object o, ScopeInstanceImpl scopeInstance,
      Variable variable) {
    EnvironmentImpl environment = EnvironmentImpl.getCurrent();
    Context context = environment.getContext("spring");
    BusinessProcessInternalService service = context
        .get(BusinessProcessInternalService.class);
    return service.createActions((BusinessProcess) o);
  }
}
