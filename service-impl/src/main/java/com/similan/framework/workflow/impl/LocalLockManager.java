package com.similan.framework.workflow.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.similan.framework.workflow.api.LockManager;


@Slf4j
@Component
public class LocalLockManager implements LockManager {
	  /**
	   * The initial capacity.
	   */
  @Getter @Setter
	  private int initialCapacity = 16;
	  
	  /**
	   * The load factor.
	   */
	  @Getter @Setter
	  private float loadFactor = 0.75f;
	  
	  /**
	   * The concurrency level.
	   */
	  @Getter @Setter
	  private int concurrencyLevel = 16;
	  
	  /**
	   * The locks.
	   */
	  private ConcurrentMap<String, Lock> locks;
	  
	  public Lock acquireLock(final String instanceId) {
		  log.info("Obtaining workflow instance lock for workflow " + instanceId);
		  
	    Lock tmpLock = this.createLock(instanceId);
	    final Lock lock = this.locks.putIfAbsent(instanceId, tmpLock);
	    if (lock != null) {
	      // There was already a lock, let's return that one.
	      return lock;
	    }
	    // No lock associated, so the one we added is the one we should use.
	    return tmpLock;
	  }
	  
	  protected Lock createLock(final String instanceId) {
	    return new ReentrantLock();
	  }
	  
	  public void destroyLock(final String instanceId) {
		  log.info("Removing workflow instance lock for workflow " + instanceId);
	    Lock removed = this.locks.remove(instanceId);
	    if (removed == null) {
	    	log.info("The lock had already been removed by another thread.");
	    }
	  }
	  
	  @PostConstruct
	  public void initialize() {
	    this.locks = new ConcurrentHashMap<String, Lock>(this.initialCapacity,
	        this.loadFactor, this.concurrencyLevel);
	  }
}
