package com.similan.framework.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class FaceletUtils {

	private static Map<String, Class<?>> enumMap = new HashMap<String, Class<?>>();

	static {
	}

	@SuppressWarnings("rawtypes")
	public static List<SelectItem> toSelectItems(String enumKey) {

		validateEnumKey(enumKey);

		Class enumClazz = enumMap.get(enumKey);
		Enum[] enumValues = (Enum[]) enumClazz.getEnumConstants();

		List<SelectItem> selectItemsToReturn = new ArrayList<SelectItem>();
		for (Enum enumValue : enumValues) {
			SelectItemEnum selectItemEnumValue = (SelectItemEnum) enumValue;
			SelectItem si = new SelectItem(selectItemEnumValue, selectItemEnumValue.getLabel());
			selectItemsToReturn.add(si);
		}

		return selectItemsToReturn;
	}

	private static void validateEnumKey(String enumKey) {
		if (!enumMap.containsKey(enumKey)) {
			throw new IllegalArgumentException("Enum for key " + enumKey + " does not exist");
		}

		if (!enumMap.get(enumKey).isEnum())
			throw new RuntimeException("The class " + enumMap.get(enumKey).getName() + " is not an enum!");
	}


	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean hasMessages() {
		return FacesContext.getCurrentInstance().getMessages().hasNext();
	}

	public static int size(Collection<?> collection) {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}

	@SuppressWarnings("unchecked")
	public static Collection<?> sort(Collection<?> coll) {
		if (coll == null) {
			return null;
		}
		List<Comparable<Object>> list = new ArrayList<Comparable<Object>>((Collection<Comparable<Object>>)coll);

		try {
			Collections.sort(list);
		} catch (ClassCastException cce) {
			// do nothing, the class does not implement comparable. just return
			// the list
		}

		return list;
	}

	public static List<SelectItem> convertToSelectItems(Collection<?> collection) {

		if (collection == null || collection.isEmpty()) {
			return new ArrayList<SelectItem>();
		}

		List<SelectItem> items = new ArrayList<SelectItem>();
		for (Object o : collection) {
			items.add(new SelectItem(o, o.toString()));
		}

		return items;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List toList(Set<?> set) {
		if (set == null || set.size() == 0) {
			return new ArrayList();
		}
		return new ArrayList(set);

	}

	public static boolean areObjectsEqual(Object o1, Object o2) {
		return o2.equals(o1);
	}

	public static String substring(String str, int maxLength) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		if (str.length() > maxLength) {
			return str.subSequence(0, maxLength) + "...";
		}

		return str;
	}

	public static String contextPath() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	public static String getSessionId(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		return request.getSession().getId();
	}

	public static boolean isNull(Object obj) {
		return (obj == null);
	}

	public static boolean isBlank(String str) {
		return StringUtils.isEmpty(str);
	}
	
	public static String integerToString(Integer inputInt) {
		if(inputInt != null){
			return String.valueOf(inputInt);
		}
		return StringUtils.EMPTY;
	}
	

}