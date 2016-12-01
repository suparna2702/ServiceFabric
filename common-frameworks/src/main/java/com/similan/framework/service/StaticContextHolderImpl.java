package com.similan.framework.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class StaticContextHolderImpl implements BeanFactoryAware {

    public static BeanFactory CONTEXT;

    public StaticContextHolderImpl() {
    }

    public static Object getBean(String s) throws BeansException {
        return CONTEXT.getBean(s);
    }

    public static <T> T getBean(String s, Class<T> tClass) throws BeansException {
        return CONTEXT.getBean(s, tClass);
    }

    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return CONTEXT.getBean(tClass);
    }

    public static Object getBean(String s, Object... objects) throws BeansException {
        return CONTEXT.getBean(s, objects);
    }

    public static boolean containsBean(String s) {
        return CONTEXT.containsBean(s);
    }

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
	        CONTEXT = beanFactory;		
	}
}