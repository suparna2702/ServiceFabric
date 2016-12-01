package com.similan.framework.importexport.csv;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import au.com.bytecode.opencsv.CSVReader;

@Slf4j
public abstract class CsvJavaBeanImporter extends CsvJavaBeanBase {
	
    
    protected abstract Object create();
	
	@SuppressWarnings("unchecked")
  public List<Object> importFromCsv(Reader fileStream) throws IOException, InstantiationException, 
	                                                     IllegalAccessException, InvocationTargetException, 
	                                                     NoSuchMethodException, ClassNotFoundException {
		
		/*
		 * 1. Read the CSV file
		 * 2. In a loop parse through each line of the CSV file
		 * 3. Extract each attribute values
		 * 4. Create an object of theBeanClass
		 * 4. Populate and put it in the List  
		 */
		List<Object> objectList = new ArrayList<Object>();
		
		@Cleanup
		CSVReader reader = new CSVReader(fileStream);
		String [] nextLine = null;
		
		while ((nextLine = reader.readNext()) != null) {
			
			if (nextLine.length < this.attributeMapping.size()) {
			  reader.close();
				throw new BeanImportingException(String.format(
						"Invalid number of CSV columns, expected {0}, got {1}",
						this.attributeMapping.size(), nextLine.length));
			}
			
			/* loop through and get each attribute and associated 
			 * mapping value */
			/* create object and populate value */
			Object beanObject = create();
			
			for(int i=0; i<this.attributeMapping.size(); i++){
				
				/* get the csvAttribute value */
				String csvAttributeValue = nextLine[i];
				log.info("csvAttributeValue : " + csvAttributeValue);
				
				String beanAttributeName = this.attributeMapping.get(i);
				log.info("beanAttributeName : " + beanAttributeName);
				
				/* get the property type */
				PropertyDescriptor propDescriptor = PropertyUtils.getPropertyDescriptor(beanObject, beanAttributeName);
				log.info("Property Descriptor Name: " + propDescriptor.getName());
				log.info("Property Descriptor ActivityType: " + propDescriptor.getPropertyType().getName());
				
				BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
				ConvertUtilsBean convertUtil = beanUtils.getConvertUtils();
				Object attibuteObject = convertUtil.convert(csvAttributeValue, propDescriptor.getPropertyType());
				
		        /* now set the property */
				try{
					
					/* Since exception is much more expensive than a comparison 
					 * and exception will be generated in a loop it is better to move 
					 * it to try block 
					 * */
                    if (propDescriptor.getPropertyType().isEnum()) {
						
						Enum<?>[] propertyValues =(Enum<?>[])propDescriptor.getPropertyType().getEnumConstants(); 
						Enum<?> propertyObject= propertyValues[0];                   
						attibuteObject= Enum.valueOf(propertyObject.getClass(), csvAttributeValue);
                    }
                    
					PropertyUtils.setSimpleProperty(beanObject, beanAttributeName, attibuteObject);
				
				}
				catch (IllegalArgumentException e){
                    log.error("Unknown property type :", e);					
				}
			}

			
			objectList.add(beanObject);
		}
		
		return objectList;
	}
	
}
