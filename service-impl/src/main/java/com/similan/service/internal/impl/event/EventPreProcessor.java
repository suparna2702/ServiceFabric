package com.similan.service.internal.impl.event;

import com.similan.service.internal.api.event.io.Event;

public interface EventPreProcessor<T extends Event> extends EventProcessor<T> {

  public void preProcess(T event);
}
