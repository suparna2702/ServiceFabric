package com.similan.framework.manager.management;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;

import com.similan.framework.Mutable;

public class ConfigurationManagerImpl implements ConfigurationManager,
    BeanPostProcessor, BeanFactoryAware, PriorityOrdered {

  private static final String SQL_TABLE_NAME = "ConfigurationParameter";
  private static final String SQL_ID_COLUMN_NAME = "id";
  private static final String SQL_TYPE_COLUMN_NAME = "type";
  private static final String SQL_BEAN_NAME_COLUMN_NAME = "beanName";
  private static final String SQL_PROPERTY_NAME_COLUMN_NAME = "propertyName";
  private static final String SQL_VALUE_COLUMN_NAME = "value";

  private static class BeanAndPropertyNames {
    private String beanName;
    private String propertyName;

    public BeanAndPropertyNames(String beanName, String propertyName) {
      this.beanName = beanName;
      this.propertyName = propertyName;
    }

    public String getBeanName() {
      return beanName;
    }

    public String getPropertyName() {
      return propertyName;
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof BeanAndPropertyNames)) {
        return false;
      }
      BeanAndPropertyNames other = (BeanAndPropertyNames) obj;
      return (this.beanName == null && other.beanName == null || this.beanName != null
          && this.beanName.equals(other.beanName))
          && (this.propertyName == null && other.propertyName == null || this.propertyName != null
              && this.propertyName.equals(other.propertyName));
    }

    @Override
    public int hashCode() {
      return (this.beanName == null ? 0 : this.beanName.hashCode())
          ^ (this.propertyName == null ? 0 : this.propertyName.hashCode());
    }
  }

  public static final String MUTABLE_PROPERTY_NAME_SUFFIX = "Mutable";

  private Object parametersLock = new Object();
  private Map<String, Map<String, ConfigurationParameterReference<Object>>> parameters = null;

  private DataSource dataSource;
  private BeanFactory beanFactory;

  public ConfigurationManagerImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private void load() {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      if (parameters == null) {
        parameters = new HashMap<String, Map<String, ConfigurationParameterReference<Object>>>();
      }
      PreparedStatement findAllQuery = connection.prepareStatement("SELECT "
          + SQL_ID_COLUMN_NAME + ", " + SQL_TYPE_COLUMN_NAME + ", "
          + SQL_BEAN_NAME_COLUMN_NAME + ", " + SQL_PROPERTY_NAME_COLUMN_NAME
          + ", " + SQL_VALUE_COLUMN_NAME + " FROM " + SQL_TABLE_NAME);
      ResultSet storedParameters = findAllQuery.executeQuery();
      Set<BeanAndPropertyNames> removedParameters = new HashSet<BeanAndPropertyNames>();
      for (Map.Entry<String, Map<String, ConfigurationParameterReference<Object>>> parameterEntry : parameters
          .entrySet()) {
        String beanName = parameterEntry.getKey();
        Map<String, ConfigurationParameterReference<Object>> beanParameters = parameterEntry
            .getValue();
        for (String propertyName : beanParameters.keySet()) {
          removedParameters
              .add(new BeanAndPropertyNames(beanName, propertyName));
        }

      }
      while (storedParameters.next()) {
        String beanName = storedParameters.getString(SQL_BEAN_NAME_COLUMN_NAME);
        String propertyName = storedParameters
            .getString(SQL_PROPERTY_NAME_COLUMN_NAME);
        String valueString = storedParameters.getString(SQL_VALUE_COLUMN_NAME);
        String typeName = storedParameters.getString(SQL_TYPE_COLUMN_NAME);
        JavaType type = getType(beanName, propertyName, typeName);
        Object value = fromString(type, beanName, propertyName, valueString);
        Map<String, ConfigurationParameterReference<Object>> beanParameters = parameters
            .get(beanName);
        if (beanParameters == null) {
          beanParameters = new HashMap<String, ConfigurationParameterReference<Object>>();
          parameters.put(beanName, beanParameters);
        }
        ConfigurationParameterReference<Object> valueReference = beanParameters
            .get(propertyName);
        if (valueReference == null) {
          valueReference = new ConfigurationParameterReference<Object>(this,
              type, beanName, propertyName);
          beanParameters.put(propertyName, valueReference);
        }
        valueReference.setRemoved(false);
        valueReference.setUsed(false);
        valueReference.doSetValue(value);

        removedParameters.remove(new BeanAndPropertyNames(beanName,
            propertyName));
      }
      storedParameters.close();
      for (BeanAndPropertyNames removedParameter : removedParameters) {
        String beanName = removedParameter.getBeanName();
        String propertyName = removedParameter.getPropertyName();
        Map<String, ConfigurationParameterReference<Object>> beanParameters = parameters
            .get(beanName);
        ConfigurationParameterReference<Object> valueReference = beanParameters
            .get(propertyName);
        valueReference.setRemoved(true);
        valueReference.setUsed(false);
        valueReference.doSetValue(null);
      }
      connection.commit();
    } catch (Throwable e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException e2) {
          // ignore
        }
      }
      throw new IllegalStateException(
          "Error loading configuration parameters.", e);
    }
  }

  private Object fromString(JavaType type, String beanName,
      String propertyName, String valueString) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(valueString, type);
    } catch (JsonParseException e) {
      throw new IllegalArgumentException("Error parsing JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value string: "
          + valueString, e);
    } catch (JsonMappingException e) {
      throw new IllegalArgumentException("Error parsing JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value string: "
          + valueString, e);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error parsing JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value string: "
          + valueString, e);
    }
  }

  private String toString(JavaType type, String beanName, String propertyName,
      Object value) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(value);
    } catch (JsonGenerationException e) {
      throw new IllegalArgumentException("Error generating JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value: " + value, e);
    } catch (JsonMappingException e) {
      throw new IllegalArgumentException("Error generating JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value: " + value, e);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error generating JSON string for '"
          + beanName + '.' + propertyName + "': " + e + " value: " + value, e);
    }
  }

  private JavaType getType(String beanName, String propertyName, String typeName) {
    try {
      TypeFactory typeFactory = TypeFactory.defaultInstance();
      JavaType type = typeFactory.constructFromCanonical(typeName);
      return type;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid type for '" + beanName + '.'
          + propertyName + "': " + typeName + " - " + e, e);
    }
  }

  public void reload() {
    synchronized (parametersLock) {
      load();
      for (Map.Entry<String, Map<String, ConfigurationParameterReference<Object>>> beanParametersEntry : parameters
          .entrySet()) {
        String beanName = beanParametersEntry.getKey();
        Map<String, ConfigurationParameterReference<Object>> beanParameters = beanParametersEntry
            .getValue();
        Object bean = beanFactory.getBean(beanName);
        setBeanParameters(bean, beanName, beanParameters);
      }
      checkUsed();
    }
  }

  private void checkUsed() {
    for (Map.Entry<String, Map<String, ConfigurationParameterReference<Object>>> beanParametersEntry : parameters
        .entrySet()) {
      String beanName = beanParametersEntry.getKey();
      Map<String, ConfigurationParameterReference<Object>> beanParameters = beanParametersEntry
          .getValue();
      for (Map.Entry<String, ConfigurationParameterReference<Object>> parameterEntry : beanParameters
          .entrySet()) {
        String propertyName = parameterEntry.getKey();
        ConfigurationParameterReference<Object> valueReference = parameterEntry
            .getValue();
        Object value = valueReference.getValue();
        if (!valueReference.isUsed() && !valueReference.isRemoved()) {
          throw new IllegalArgumentException("Value was not used: " + beanName
              + "." + propertyName + ", value class: "
              + (value == null ? "null" : value.getClass()) + ", value: "
              + value);
        }
      }
    }
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    synchronized (parametersLock) {
      if (parameters == null) {
        load();
      }
      Map<String, ConfigurationParameterReference<Object>> beanParameters = parameters
          .get(beanName);
      setBeanParameters(bean, beanName, beanParameters);
    }
    return bean;
  }

  private void setBeanParameters(Object bean, String beanName,
      Map<String, ConfigurationParameterReference<Object>> beanParameters) {
    if (beanParameters == null || beanParameters.isEmpty()) {
      return;
    }
    Class<?> beanClass = bean.getClass();
    BeanWrapper propertyAccessor = PropertyAccessorFactory
        .forBeanPropertyAccess(bean);
    for (Map.Entry<String, ConfigurationParameterReference<Object>> parameterEntry : beanParameters
        .entrySet()) {
      String propertyName = parameterEntry.getKey();
      ConfigurationParameterReference<Object> valueReference = parameterEntry
          .getValue();
      Object value = valueReference.getValue();
      try {
        Method mutableSetter = getMutableSetter(beanClass, propertyName, value);
        if (mutableSetter != null) {
          mutableSetter.invoke(bean, valueReference);
        } else {
          propertyAccessor.setPropertyValue(propertyName, value);
        }
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Error setting property for " + beanName + "." + propertyName
                + " (at class " + beanClass.getName() + ")" + ", value class: "
                + (value == null ? "null" : value.getClass()) + ", value: "
                + value, e);
      }
    }
  }

  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  public <T> void setValue(JavaType type, String beanName, String propertyName,
      T value) {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      if (parameters == null) {
        load();
      }
      Map<String, ConfigurationParameterReference<Object>> beanParameters = parameters
          .get(beanName);
      if (beanParameters == null) {
        beanParameters = new HashMap<String, ConfigurationParameterReference<Object>>();
        parameters.put(beanName, beanParameters);
      }
      ConfigurationParameterReference<Object> valueReference = beanParameters
          .get(propertyName);
      if (valueReference == null) {
        valueReference = new ConfigurationParameterReference<Object>(this,
            type, beanName, propertyName);
        beanParameters.put(propertyName, valueReference);
      }
      valueReference.doSetValue(value);
      Object bean = beanFactory.getBean(beanName);
      BeanWrapperImpl wrapper = new BeanWrapperImpl(bean);
      Class<?> propertyType = wrapper.getPropertyType(propertyName);
      if (propertyType == Mutable.class) {
        wrapper.setPropertyValue(propertyName, valueReference);
      } else {
        wrapper.setPropertyValue(propertyName, value);
      }
      PreparedStatement findByBeanNameAndPropertyNameQuery = connection
          .prepareStatement("SELECT " + SQL_ID_COLUMN_NAME + ", "
              + SQL_TYPE_COLUMN_NAME + ", " + SQL_BEAN_NAME_COLUMN_NAME + ", "
              + SQL_PROPERTY_NAME_COLUMN_NAME + ", " + SQL_VALUE_COLUMN_NAME
              + " FROM " + SQL_TABLE_NAME + " WHERE "
              + SQL_BEAN_NAME_COLUMN_NAME + " = ? AND "
              + SQL_PROPERTY_NAME_COLUMN_NAME + " = ?");
      findByBeanNameAndPropertyNameQuery.setString(1, beanName);
      findByBeanNameAndPropertyNameQuery.setString(1, propertyName);
      ResultSet parameter = findByBeanNameAndPropertyNameQuery.executeQuery();
      String valueString = toString(type, beanName, propertyName,
          valueReference);
      if (!parameter.next()) {
        parameter.close();
        PreparedStatement insert = connection.prepareStatement("INSERT INTO "
            + SQL_TABLE_NAME + " (" + SQL_TYPE_COLUMN_NAME + ", "
            + SQL_BEAN_NAME_COLUMN_NAME + ", " + SQL_PROPERTY_NAME_COLUMN_NAME
            + ", " + SQL_VALUE_COLUMN_NAME + ") VALUES (?, ?, ?, ?)");
        insert.setString(1, type.toCanonical());
        insert.setString(2, beanName);
        insert.setString(3, propertyName);
        insert.setString(4, valueString);
        if (!insert.execute()) {
          throw new IllegalStateException(
              "Could not insert configuration parameter " + beanName + "."
                  + propertyName);
        }
      } else {
        long id = parameter.getLong(SQL_ID_COLUMN_NAME);
        PreparedStatement update = connection.prepareStatement("UPDATE "
            + SQL_TABLE_NAME + " SET " + SQL_TYPE_COLUMN_NAME + " = ?, "
            + SQL_VALUE_COLUMN_NAME + " = ? WHERE " + SQL_ID_COLUMN_NAME
            + " = ?");
        update.setString(1, type.toCanonical());
        update.setString(2, valueString);
        update.setLong(3, id);
        if (!update.execute()) {
          throw new IllegalStateException(
              "Could not update configuration parameter " + beanName + "."
                  + propertyName);
        }
      }
      connection.commit();
    } catch (Throwable e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException e2) {
          // ignore
        }
      }
      throw new IllegalStateException(
          "Error loading configuration parameters.", e);
    }
  }

  private Method getMutableSetter(Class<?> beanClass, String propertyName,
      Object value) {
    int propertyNameLength = propertyName.length();
    String setterName = "set"
        + (propertyNameLength == 0 ? propertyName : (propertyName.substring(0,
            1).toUpperCase() + propertyName.substring(1)));
    for (Method method : beanClass.getMethods()) {
      if (method.getName().equals(setterName)) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1
            && method.getParameterTypes()[0] == Mutable.class) {
          return method;
        }
      }
    }
    setterName += MUTABLE_PROPERTY_NAME_SUFFIX;
    for (Method method : beanClass.getMethods()) {
      if (method.getName().equals(setterName)) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1
            && method.getParameterTypes()[0] == Mutable.class) {
          return method;
        }
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue(String beanName, String propertyName) {
    if (parameters == null) {
      load();
    }
    Map<String, ConfigurationParameterReference<Object>> beanParameters = parameters
        .get(beanName);
    if (beanParameters == null) {
      return null;
    }
    ConfigurationParameterReference<Object> valueReference = beanParameters
        .get(propertyName);
    if (valueReference == null) {
      return null;
    }
    return (T) valueReference.getValue();
  }

  public int getOrder() {
    return HIGHEST_PRECEDENCE;
  }

  public List<ConfigurationParameterDto> getParameters() {
    if (parameters == null) {
      load();
    }
    synchronized (parametersLock) {
      List<ConfigurationParameterDto> parameterDtos = new LinkedList<ConfigurationParameterDto>();
      for (Entry<String, Map<String, ConfigurationParameterReference<Object>>> beanParametersEntry : parameters
          .entrySet()) {
        String beanName = beanParametersEntry.getKey();
        Map<String, ConfigurationParameterReference<Object>> beanParameters = beanParametersEntry
            .getValue();
        for (Map.Entry<String, ConfigurationParameterReference<Object>> parameterEntry : beanParameters
            .entrySet()) {
          String propertyName = parameterEntry.getKey();
          ConfigurationParameterReference<Object> parameterReference = parameterEntry
              .getValue();
          JavaType type = parameterReference.getType();
          Object value = parameterReference.getValue();
          String valueString = toString(type, beanName, propertyName, value);
          ConfigurationParameterDto parameterDto = new ConfigurationParameterDto(
              beanName, propertyName, type.toCanonical(), valueString);
          parameterDtos.add(parameterDto);
        }
      }
      return parameterDtos;
    }
  }
}
