package com.similan.framework.importexport.csv;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.PropertyUtils;

import au.com.bytecode.opencsv.CSVWriter;

@Slf4j
public class CsvJavaBeanExporter extends CsvJavaBeanBase {
	
	
	/**
	 * This method takes a list of java bean object and convert them 
	 * into Csv stream
	 * @param beanList
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IOException 
	 */
	public void exportIntoCsv(List<Object> beanList, Writer writer) throws ClassNotFoundException, IllegalAccessException, 
	                          InvocationTargetException, NoSuchMethodException, IOException{
		
		/* loop through all objects and check type. If the type 
		 * matches then  1. get the relevant properties
		 * 2. Put them in Csv */
	    List<String[]> lineList = new ArrayList<String[]>();
	    
		for(Object obj : beanList){
			
		   String[] line = new String[attributeMapping.size()];
		   
		   for(int i=0; i<attributeMapping.size(); i++){
			   
			   String propName = attributeMapping.get(i);
			   Object property = PropertyUtils.getSimpleProperty(obj, propName);
			   String propString = property.toString();
			   log.info("Bean property name :" + propName + " property value :" + property.toString());
			   line[i] = propString;
		   }
		   
		   lineList.add(line);
		}
		
		/* create the CsvWriter */
		@Cleanup
		CSVWriter csvWriter = new CSVWriter(writer);
		csvWriter.writeAll(lineList);
		csvWriter.flush();
	}
	
	

}
