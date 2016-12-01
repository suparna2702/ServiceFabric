package com.similan.domain.repository.impl;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TO_BE_DELETED_DomainSearchManagerImpl extends HibernateDaoSupport {
	
	public List<?> serachAll(String searchString) throws ParseException {

		/* build the Lucene query */
		List<?> searchResults = null;
/*		MultiFieldQueryParser parser = new MultiFieldQueryParser(
				Version.LUCENE_35,
				new String[] { "name", "member.name", "product.name",
						"product.description", "catalogueName" },
				new StandardAnalyzer(Version.LUCENE_35));
		Query luceneQuery = parser.parse(searchString);

		 create a full text session 

		FullTextSession fullTextSession = Search.getFullTextSession(this
				.getSession());
		FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
				luceneQuery, JpaMember.class, JpaProduct.class,
				JpaProductCatalogue.class);

		 create the full text query 
		searchResults = fullTextQuery.list();*/
		return searchResults;
	}

}
