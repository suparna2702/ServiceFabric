package com.similan.portal.view;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.ArrayUtils;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.jsf.FacesContextUtils;

@Component("facesHelper")
@Slf4j
public class FacesHelper implements Serializable {

	private static final long serialVersionUID = -4502251921541100055L;
	
	public void addMessage(String clientId, FacesMessage.Severity severity,
			String summary, String detail) {
		FacesMessage message = new FacesMessage();
		message.setSeverity(severity);
		message.setSummary(summary);
		message.setDetail(detail);
		getFacesContext().addMessage(clientId, message);
	}

	private String getDefaultSummary(Severity severity) {
		String summaryKey = null;
		if (severity == FacesMessage.SEVERITY_ERROR)
			summaryKey = "error.header";
		if (severity == FacesMessage.SEVERITY_FATAL)
			summaryKey = "error.header";
		if (severity == FacesMessage.SEVERITY_WARN)
			summaryKey = "warning.header";
		if (severity == FacesMessage.SEVERITY_INFO)
			summaryKey = "info.header";
		return summaryKey;
	}

	public void addMessage(Severity severity, String errorCode, Object[] params) {
		addMessage(null, severity, getDefaultSummary(severity), errorCode,
				params);
	}

	public void addMessage(Severity severity, String errorCode) {
		addMessage(severity, errorCode, new Object[] {});
	}

	public void addMessage(String clientId, Severity severity, String errorCode) {
		String detail = getMessageFromBundle(errorCode, new Object[] {});
		addMessage(clientId, severity, detail, detail);
	}

	public void addMessage(String clientId, Severity severity,
			String summaryKey, String detailKey, Object[] params) {
		String detail = getMessageFromBundle(detailKey, params);
		String summary = getMessageFromBundle(summaryKey);
		addMessage(clientId, severity, summary, detail);

	}

	public void addDirectMessage(String clientId, Severity severity,
			String message) {
		String detail = message;
		String summary = "";
		addMessage(clientId, severity, summary, detail);
	}

	public void addMessage(String clientId, Severity severity,
			String detailKey, Object[] params) {
		String detail = getMessageFromBundle(detailKey, params);
		String summary = getMessageFromBundle(getDefaultSummary(severity));
		addMessage(clientId, severity, summary, detail);

	}

	public void addMessage(Severity severity, String summaryKey,
			String detailKey, Object[] params) {
		String detail = getMessageFromBundle(detailKey, params);
		String summary = getMessageFromBundle(summaryKey);
		addMessage(null, severity, summary, detail);
	}

	public void addMessage(Severity severity, String summaryKey,
			String detailKey) {
		String detail = getMessageFromBundle(detailKey);
		String summary = getMessageFromBundle(summaryKey);
		addMessage(null, severity, summary, detail);
	}

	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public RequestContext getRequestContext() {
		return RequestContext.getCurrentInstance();
	}

	public Flash getFlash() {
		return getFacesContext().getExternalContext().getFlash();
	}

	public Object flashGet(String parameter) {
		return getFlash().get(parameter);
	}

	public void flashPutNow(String parameter, Object value) {
		getFlash().putNow(parameter, value);
	}

	public void flashPut(String parameter, Object value) {
		getFlash().put(parameter, value);
	}

	public ExternalContext getExternalContext() {
		return getFacesContext().getExternalContext();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	public void forward(String path) {
		try {
			RequestDispatcher dispatcher = getRequest().getRequestDispatcher(
					path);
			dispatcher.forward(getRequest(), getResponse());
			getFacesContext().responseComplete();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void redirect(String path) {
		try {
			
			String contextPath = getExternalContext().getRequestContextPath();
			String redirectPath = contextPath + path;
			
			log.info("Context path " + contextPath 
					      + "redirect path " + redirectPath 
					      + " supplied path " + path);
			
			getExternalContext().redirect(contextPath + path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void flashClear(String parameter) {
		flashPutNow(parameter, null);
	}

	public String getRequestParameter(String parameterName) {
		return getExternalContext().getRequestParameterMap().get(parameterName);
	}

	public void addPartialUpdateTarget(String target) {
		getRequestContext().update(target);
	}

	public void executeJS(String js) {
		getRequestContext().execute(js);
	}

	public String getCurrentViewName() {
		return getFacesContext().getViewRoot().getViewId();
	}

	public HttpSession getSession() {
		return (HttpSession) getFacesContext().getExternalContext().getSession(
				true);
	}

	public void putInSession(String parameterName, String errorMessage) {
		getSession().setAttribute(parameterName, errorMessage);

	}

	public void invalidateSession() {
		HttpSession session = (HttpSession) getExternalContext().getSession(
				false);
		if (session != null) {
			session.invalidate();
		}
	}

	public String getMessageFromBundle(String messageKey, Object... params) {
		Locale locale = getCurrentLocale();
		ClassLoader loader = getClassLoader();

		if (messageKey.indexOf('.') == -1) {
			return "???" + messageKey + "???";
		}

		ResourceBundle bundle = ResourceBundle.getBundle("i18n.bundle", locale,
				loader);
		if (ArrayUtils.isEmpty(params)) {
			return bundle.getString(messageKey);
		}

		MessageFormat formatter = new MessageFormat(
				bundle.getString(messageKey), locale);
		return formatter.format(params);
	}

	public Locale getCurrentLocale() {

		Locale locale = null;

		UIViewRoot viewRoot = getFacesContext().getViewRoot();
		if (viewRoot != null) {
			locale = viewRoot.getLocale();
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		return locale;

	}

	private ClassLoader getClassLoader() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = ClassLoader.getSystemClassLoader();
		}
		return loader;
	}

	public List<FacesMessage> getAllMessages() {
		return getFacesContext().getMessageList();
	}

	public Object getBean(String beanName) {
		return FacesContextUtils.getWebApplicationContext(getFacesContext())
				.getBean(beanName);
	}

	public String redirectTo(String view) {
		return view + "faces-redirect=true";
	}
}