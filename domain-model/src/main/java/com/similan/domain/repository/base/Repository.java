package com.similan.domain.repository.base;

import java.io.Serializable;
import java.util.List;

public interface Repository<T, K extends Serializable> {

	T get(K id);

	T fetch(K id);

	void put(T object);

	List<T> getAll();

}
