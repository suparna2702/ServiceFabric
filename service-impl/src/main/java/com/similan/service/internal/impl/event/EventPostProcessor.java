package com.similan.service.internal.impl.event;

import com.similan.service.internal.api.event.io.Event;

public interface EventPostProcessor<T extends Event> extends EventProcessor<T> {

  public void postProcess(T event);
}
