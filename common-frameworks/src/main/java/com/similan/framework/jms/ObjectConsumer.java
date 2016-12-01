package com.similan.framework.jms;

import java.io.Serializable;

public interface ObjectConsumer<T extends Serializable> {

	void consume(T object);
}
