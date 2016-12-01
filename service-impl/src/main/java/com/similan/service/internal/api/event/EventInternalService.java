package com.similan.service.internal.api.event;

import com.similan.service.internal.api.event.io.Event;

public interface EventInternalService {

  void fire(Event event);

}
