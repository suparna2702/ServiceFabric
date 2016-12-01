package com.similan.framework.workflow.api;

import java.util.concurrent.locks.Lock;

/**
 * Lock management interface.
 * 
 * @author enrique.dacosta
 */
public interface LockManager {
  
  /**
   * Obtains a lock for the given id and acquires a lock on it.
   * If the lock already exists
   * then it's returned, otherwise a new one is created and stored for later
   * reference.
   * <p>
   * <em>Note</em>: This operation is thread-safe.
   * @param id
   *          The id to obtain the lock for.
   * @return A lock for the instance id, that will be shared by all the threads
   *         attempting to execute it. Never null.
   */
  Lock acquireLock(final String string);
  
  /**
   * Destroys the lock for the given id. This should
   * usually be called when the workflow has completed and won't be executed
   * again.
   * 
   * @param id
   *          The id whose lock will be destroyed.
   */
  void destroyLock(final String string);
  
}

