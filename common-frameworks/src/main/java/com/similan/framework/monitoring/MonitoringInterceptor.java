package com.similan.framework.monitoring;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.javasimon.SimonManager;
import org.javasimon.Split;

/**
 * Method interceptor that measures the duration of the intercepted call with a Stopwatch.
 *
 * @author Suparna
 */
@Slf4j
public final class MonitoringInterceptor implements MethodInterceptor, Serializable {
  private static final long serialVersionUID = 8036838143207972190L;
  
	/**
	 * Performs method invocation and wraps it with Stopwatch.
	 *
	 * @param invocation method invocation
	 * @return return object from the method
	 * @throws Throwable anything thrown by the method
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String monitorName = MonitoredHelper.getMonitorName(invocation);
		System.out.println("The method name :" + monitorName);
		
		Split split = SimonManager.getStopwatch(monitorName).start();
		try {
			return invocation.proceed();
		} finally {
			split.stop();
		}
	}
}
