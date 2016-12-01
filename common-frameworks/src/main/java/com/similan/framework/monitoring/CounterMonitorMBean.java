package com.similan.framework.monitoring;

import org.javasimon.Counter;
import org.javasimon.SimonManager;

/**
 * This will be used for persistent task state transition tracking
 * 
 * @author suparnap
 * 
 */
public class CounterMonitorMBean implements ICounterMonitorMBean {

  /**
   * The counter name
   */
  private final String counterName;

  /**
   * Public constructor
   * 
   * @param name
   *          the counter name to set.
   */
  public CounterMonitorMBean(final String name) {
    this.counterName = name;
  }

  /**
   * {@inheritDoc}
   */
  public long getCount() {
    Counter counter = SimonManager.getCounter(this.counterName);

    if (counter == null) {
      throw new IllegalStateException("Invalid counter name :"
          + this.counterName);
    }

    return counter.getCounter();
  }

  /**
   * {@inheritDoc}
   */
  public void reset() {
    Counter counter = SimonManager.getCounter(this.counterName);

    if (counter == null) {
      throw new IllegalStateException("Invalid counter name :"
          + this.counterName);
    }

    counter.reset();
  }

}
