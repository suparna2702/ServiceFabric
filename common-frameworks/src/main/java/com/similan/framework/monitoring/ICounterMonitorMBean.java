package com.similan.framework.monitoring;

/**
 * @author Suparna
 */

public interface ICounterMonitorMBean {

	  /**
	   * returns the number current value of the counter
	   * 
	   * @return long the counter value
	   */
	  public long getCount();

	  /**
	   * resets the underlying counter
	   */
	  public void reset();

}

