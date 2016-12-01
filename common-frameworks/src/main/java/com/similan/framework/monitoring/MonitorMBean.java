package com.similan.framework.monitoring;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.javasimon.SimonManager;
import org.javasimon.StopwatchSample;

/**
 * The MBean checks whether the core service is healthy by monitoring the
 * execution counter, the average execution time and last execution time of
 * important methods. Methods with @Monitored will be monitored.
 * 
 * @author suparna
 * 
 */
@Slf4j
public class MonitorMBean implements IMonitorMBean {
	
  private final String stopWatchName;
  private long samplePeriod = 10000;

  private volatile StopwatchSample sample = null;
  private Sampler sampler = null;
  private Timer timer = null;

   public MonitorMBean(final String name) {
    this.stopWatchName = name;
    timer = new Timer("Timer for " + name, true); //make it a daemon
    sampler = new Sampler();
    timer.scheduleAtFixedRate(sampler, 1000, samplePeriod); 
  }

  /**
   * Return how many times the monitored method has been triggered since last
   * reset.
   * 
   * @return long
   * @author suparna
   */
  public long getCounter() {
    return sample.getCounter();
  }

  /**
   * Return average execution time of the monitored method.
   * 
   * @return long
   * @author suparna
   */
  public long getAvgMethodExecution() {
    return TimeUnit.NANOSECONDS.toMillis((long)sample.getMean());
  }

  /**
   * Return last execution time of the monitored method.
   * 
   * @return long
   * @author suparna
   */
  public long getLastExecution() {
    return TimeUnit.NANOSECONDS.toMillis(sample.getLast());
  }

  /**
   * Returns the minimum execution time.
   * @return The min execution time.
   */
  public long getMinimumExecution() {
    return TimeUnit.NANOSECONDS.toMillis(sample.getMin());
  }

  /**
   * Returns the maximum execution time.
   * @return The max execution time.
   */
  public long getMaximumExecution() {
    return TimeUnit.NANOSECONDS.toMillis(sample.getMax());
  }

  /**
   * Returns the standard deviation.
   * @return The std deviation.
   */
  public long getStdDeviation() {
    return toMilisAndRound(sample.getStandardDeviation());
  }

  /**
   * Returns the variance of the sampled times.
   * @return The variance.
   */
  public long getVariance() {
    return toMilisAndRound(sample.getVarianceN());
  }

  /**
   * Converts a value relative to nanoseconds to milliseconds and then rounds it
   * to convert it to long.
   * 
   * @param value
   *          The value to convert
   * @return The resulting value from the conversion and rounding.
   */
  private long toMilisAndRound(final double value) {
    return Math.round(value / (1000L * 1000L));
  }

  /**
   * Reset the watch for the monitored method. @see
   * org.javasimon.Stopwatch#reset()
   * 
   * @author suparna
   */
  public void reset() {
    SimonManager.getStopwatch(stopWatchName).reset();
  }
  
  /**
   * Reset the sample period if it is greater than 0.
   * @param samplePeriod
   * @author suparna
   */
  public void setSamplePeriod(long samplePeriod) {
    if(samplePeriod!=0){
      this.samplePeriod = samplePeriod;
      sampler.cancel();
      timer.purge();
      sampler = new Sampler();
      timer.scheduleAtFixedRate(sampler, 1000, samplePeriod); 
    }
  }

  /**
   * Return stop watch sampling period
   * @return long
   * @author suparna
   */
  public long getSamplePeriod() {
    return samplePeriod;
  }

  /**
   * A running thread to sample periodically, so the counter and avgMethodExecution will be consistent.
   * @author suparna
   *
   */
  class Sampler extends TimerTask {
    public void run() {
      sample = (StopwatchSample) SimonManager.getStopwatch(stopWatchName).sample();       
    } 
  }

}

