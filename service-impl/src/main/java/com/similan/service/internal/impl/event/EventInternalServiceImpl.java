package com.similan.service.internal.impl.event;

import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.similan.service.impl.base.ServiceImpl;
import com.similan.service.internal.api.event.EventInternalService;
import com.similan.service.internal.api.event.io.Event;

@Slf4j
@Service
public class EventInternalServiceImpl extends ServiceImpl implements
    EventInternalService {
  @Resource(name = "eventProcessors")
  private Map<Class<? extends Event>, EventProcessor<?>> processors;

  private EventProcessor<?> findProcessor(Class<?> eventType) {
    while (eventType != Object.class) {
      EventProcessor<?> processor = processors.get(eventType);
      if (processor != null) {
        return processor;
      }
    }
    return null;
  }

  @Override
  @Transactional
  public void fire(Event event) {
    log.info("Event received " + event);
    Class<? extends Event> eventType = event.getClass();
    EventProcessor<?> processor = findProcessor(eventType);
    if (processor != null && processor instanceof EventPreProcessor) {
      preProcess((EventPreProcessor<?>) processor, event);
    }
    if (processor != null && processor instanceof EventPostProcessor) {
      enqueue(event);
    }
  }

  private void enqueue(Event event) {
    // TODO use real queue
    postProcess(event);
  }

  private void postProcess(Event event) {
    Class<? extends Event> eventType = event.getClass();
    EventProcessor<?> postProcessor = findProcessor(eventType);
    if (postProcessor != null && postProcessor instanceof EventPostProcessor) {
      postProcess((EventPostProcessor<?>) postProcessor, event);
    }
  }

  @SuppressWarnings("unchecked")
  private void preProcess(EventPreProcessor<?> processor, Event event) {
    ((EventPreProcessor<Event>) processor).preProcess(event);
  }

  @SuppressWarnings("unchecked")
  private void postProcess(EventPostProcessor<?> processor, Event event) {
    ((EventPostProcessor<Event>) processor).postProcess(event);
  }
}
