package com.similan.domain.repository.base;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class BaseRepositoryImpl<T, K extends Serializable> extends
		HibernateDaoSupport implements Repository<T, K> {

	protected static class FieldValue {
		private String name;
		private Object value;

		public FieldValue(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}

		@Override
		public String toString() {
			return name + "=" + value;
		}
	}

	protected FieldValue field(String name, Object value) {
		return new FieldValue(name, value);
	}

	private Class<T> theClass;

	public BaseRepositoryImpl(Class<T> theClass) {
		this.theClass = theClass;
	}

	public T get(K id) {
		Object object = getHibernateTemplate().get(theClass, id);
		T casted = theClass.cast(object);
		return casted;
	}

	public boolean delete(K id) {
		Object object = get(id);
		if (object == null) {
			return false;
		}
		getHibernateTemplate().delete(object);
		return true;
	}

	public T fetch(K id) {
		T element = get(id);
		if (element == null) {
			throw new IllegalArgumentException("No " + theClass.getSimpleName()
					+ " with id " + id);
		}
		return element;
	};

	protected T getByValue(String fieldName, Object fieldValue) {
		return getByValues(field(fieldName, fieldValue));
	}

	protected T getByValues(FieldValue... fieldValues) {
		List<T> list = getListByValues(fieldValues);
		if (list.isEmpty()) {
			return null;
		} else if (list.size() == 1) {
			Object element = list.get(0);
			return theClass.cast(element);
		} else {
			throw new IllegalArgumentException(list.size() + " "
					+ theClass.getSimpleName() + " with "
					+ Arrays.toString(fieldValues));
		}
	}

	protected List<T> getListByValue(String fieldName, Object fieldValue) {
		return getListByValues(field(fieldName, fieldValue));
	}

	protected List<T> getListByValues(FieldValue... fieldValues) {
		DetachedCriteria criteria = DetachedCriteria.forClass(theClass);
		Set<String> aliases = new HashSet<String>();
		for (FieldValue fieldValue : fieldValues) {
			String name = fieldValue.getName();
			int pathEndIndex = name.lastIndexOf('.');
			String nameAlias;
			if (pathEndIndex == -1) {
				nameAlias = name;
			} else {
				String path = name.substring(0, pathEndIndex);
				String pathAlias = path.replace('.', '_');
				nameAlias = pathAlias + name.substring(pathEndIndex);
				if (aliases.add(path)) {
					criteria.createAlias(path, pathAlias);
				}
			}
			Object value = fieldValue.getValue();
			criteria.add(Restrictions.eq(nameAlias, value));
		}
		List<?> list = getHibernateTemplate().findByCriteria(criteria);
		return castList(list);
	}

	protected T fetchByValue(String fieldName, Object fieldValue) {
		return fetchByValues(field(fieldName, fieldValue));
	}

	protected T fetchByValues(FieldValue... fieldValues) {
		T element = getByValues(fieldValues);
		if (element == null) {
			throw new IllegalArgumentException("No " + theClass.getSimpleName()
					+ " with " + Arrays.toString(fieldValues));
		}
		return element;
	}

	@SuppressWarnings("unchecked")
	protected List<T> castList(List<?> list) {
		return (List<T>) list;
	}

	public void put(T object) {
		getHibernateTemplate().save(object);
	}

	protected DetachedCriteria getCriteria() {
		return getCriteria(this.theClass);
	}

	protected DetachedCriteria getCriteria(Class<? extends T> theClass) {
		return DetachedCriteria.forClass(theClass);
	}

	protected List<T> getListByCriteria(final DetachedCriteria criteria) {
		return getListByCriteria(criteria, true);
	}

	protected List<T> getListByCriteria(final DetachedCriteria criteria,
			boolean distinctRoot) {
		return getListByCriteria(criteria, distinctRoot, null, null);
	}

	protected List<T> getListByCriteria(final DetachedCriteria criteria,
			boolean distinctRoot, Integer firstResult, Integer maxResults) {
		if (distinctRoot) {
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		List<?> list;
		if (firstResult != null || maxResults != null) {
			list = this.getHibernateTemplate().findByCriteria(criteria,
					firstResult, maxResults);
		} else {
			list = this.getHibernateTemplate().findByCriteria(criteria);
		}
		return castList(list);
	}

	protected T getByCriteria(final DetachedCriteria criteria) {
		return getByCriteria(criteria, true);
	}

	protected T getByCriteria(final DetachedCriteria criteria,
			boolean distinctRoot) {
		List<T> list = getListByCriteria(criteria, distinctRoot);
		if (list.isEmpty()) {
			return null;
		} else if (list.size() >= 1) {
			Object element = list.get(0);
			return theClass.cast(element);
		} else {
			throw new IllegalArgumentException(list.size() + " "
					+ theClass.getSimpleName() + " with criteria: " + criteria);
		}
	}

	protected List<T> getListByQuery(final String queryName, String parameter,
			Object value) {
		return getListByQuery(queryName, new String[] { parameter },
				new Object[] { value });
	}

	protected List<T> getListByQuery(final String queryName,
			String[] parameters, Object[] values) {
		List<?> list = this.getHibernateTemplate()
				.findByNamedQueryAndNamedParam(queryName, parameters, values);
		return castList(list);
	}

	protected T getByQuery(final String queryName, String parameter,
			Object value) {
		return getByQuery(queryName, new String[] { parameter },
				new Object[] { value });
	}

	protected T getByQuery(final String queryName, String[] parameters,
			Object[] values) {
		List<T> list = getListByQuery(queryName, parameters, values);
		if (list.isEmpty()) {
			return null;
		} else if (list.size() >= 1) {
			Object element = list.get(0);
			return theClass.cast(element);
		} else {
			throw new IllegalArgumentException(list.size() + " "
					+ theClass.getSimpleName() + " with query: " + queryName
					+ ", parameters: " + Arrays.toString(parameters)
					+ ", values: " + Arrays.toString(values));
		}
	}

	protected T fetchByCriteria(final DetachedCriteria criteria) {
		T element = getByCriteria(criteria);
		if (element == null) {
			throw new IllegalArgumentException("No " + theClass.getSimpleName()
					+ " with criteria: " + criteria);
		}
		return element;
	}

	public List<T> getAll() {
		List<?> list = this.getHibernateTemplate().loadAll(this.theClass);
		return castList(list);
	}
}
