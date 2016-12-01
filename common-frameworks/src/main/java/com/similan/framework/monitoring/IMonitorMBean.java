package com.similan.framework.monitoring;

public interface IMonitorMBean {
	  /**
	   * Return how many times the monitored method has been triggered since last
	   * reset.
	   * 
	   * @return long
	   * @author suparna
	   */
	  public long getCounter();

	  /**
	   * Return average execution time of the monitored method.
	   * 
	   * @return long
	   * @author suparna
	   */
	  public long getAvgMethodExecution();

	  /**
	   * Return last execution time of the monitored method.
	   * 
	   * @return long
	   * @author suparna
	   */
	  public long getLastExecution();

	  /**
	   * Returns the minimum execution time.
	   * @return The min execution time.
	   */
	  public long getMinimumExecution();

	  /**
	   * Returns the maximum execution time.
	   * @return The max execution time.
	   */
	  public long getMaximumExecution();

	  /**
	   * Returns the standard deviation.
	   * @return The std deviation.
	   */
	  public long getStdDeviation();

	  /**
	   * Reset the watch for the monitored method. @see
	   * org.javasimon.Stopwatch#reset()
	   * 
	   * @author suparna
	   */
	  public void reset();

	  /**
	   * Returns the variance of the sampled times.
	   * @return The variance.
	   */
	  public long getVariance();
	}
