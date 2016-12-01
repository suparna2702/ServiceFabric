package com.similan.framework;

/**
 * This interface should be dropped in favour of
 * {@link org.apache.commons.lang.mutable.Mutable} once Apache's commons-lang 3
 * is out there.
 * 
 * @author Enrique
 * 
 * @param <T>
 */
public interface Mutable<T> {

	public T getValue();

	public void setValue(T value);

}
