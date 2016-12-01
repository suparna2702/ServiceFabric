package com.similan.framework.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanPropertyUpdator {
	private Class<?> fromBeanClass;
	
	private Class<?> toBeanClass;
	
	private Map<String, String> propertyMap;

	public Class<?> getFromBeanClass() {
		return fromBeanClass;
	}

	public void setFromBeanClass(Class<?> fromBeanClass) {
		this.fromBeanClass = fromBeanClass;
	}

	public Class<?> getToBeanClass() {
		return toBeanClass;
	}

	public void setToBeanClass(Class<?> toBeanClass) {
		this.toBeanClass = toBeanClass;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}
	
	/**
	 * 
	 * @param fromBean
	 * @param toBean
	 * @throws Exception
	 */
	public void update(Object fromBean, Object toBean) throws Exception{
		
		/* perform type check */
		if(fromBean == null || toBean == null){
			throw new Exception("Null parameter ");
		}
		
		if (!fromBeanClass.isAssignableFrom(fromBean.getClass())){
			throw new Exception("Wrong object type fromBean " 
		                      + fromBean.getClass().toString());
		}
		
		if (!toBeanClass.isAssignableFrom(toBean.getClass())) {
			throw new Exception("Wrong object type toBean " 
		                 + fromBean.getClass().toString());
		}
		
		/* 1. get the key = frombean property and value = to bean property 
		 * 2. Get the properties from respective beans 
		 * 3. Compare
		 * 4. If not null and differ then copy to to bean */
		Set<String> keySet = propertyMap.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		
		while(keyIterator.hasNext()){
			String fromPropertyName = keyIterator.next();
			String toPropertyName = propertyMap.get(fromPropertyName);
			
			/* get property */
			Object fromProperty = PropertyUtils.getSimpleProperty(fromBean, fromPropertyName);
			Object toProperty = PropertyUtils.getSimpleProperty(toBean, toPropertyName);
			
			/* check null and compare */
			if(fromProperty != null){ 
				 
				if(fromProperty.equals(toProperty) == false){
					PropertyUtils.setSimpleProperty(toBean, toPropertyName, fromProperty);
				}
				
			}
		}
		
	}

}
