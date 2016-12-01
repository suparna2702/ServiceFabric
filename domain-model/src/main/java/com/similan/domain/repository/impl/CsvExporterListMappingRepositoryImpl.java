package com.similan.domain.repository.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.similan.domain.csv.CsvExporterListMapping;
import com.similan.domain.csv.CsvExporterListMappingType;
import com.similan.domain.repository.api.CsvExporterListMappingRepository;
import com.similan.domain.repository.base.BaseRepositoryImpl;

public class CsvExporterListMappingRepositoryImpl
    extends BaseRepositoryImpl<CsvExporterListMapping, Long>
    implements CsvExporterListMappingRepository {

	public CsvExporterListMappingRepositoryImpl(
			Class<CsvExporterListMapping> theClass) {
		super(theClass);
	}

	public CsvExporterListMappingRepositoryImpl(){
		super(CsvExporterListMapping.class);
	}

	public CsvExporterListMapping getExporterMapping(
			CsvExporterListMappingType type, String name) {
		
		DetachedCriteria criteria = this.getCriteria();
		criteria.add(Restrictions.eq("mappingType", type));
		criteria.add(Restrictions.eq("name", name));
		
		return this.getByCriteria(criteria);
	}

	public List<CsvExporterListMapping> getExporterMapping(
			CsvExporterListMappingType type) {
		
		DetachedCriteria criteria = this.getCriteria();
		criteria.add(Restrictions.eq("mappingType", type));
		
		return this.getListByCriteria(criteria);
	}
}
