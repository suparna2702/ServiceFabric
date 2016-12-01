package com.similan.domain.repository.api;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;

public interface DomainSearchManager {
	
	public List<?> serachAll(String searchString) throws ParseException;
}
