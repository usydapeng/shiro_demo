package com.dapeng.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

public class BasicMappingExceptionResolver extends SimpleMappingExceptionResolver{
	private static final Logger logger = LoggerFactory.getLogger(BasicMappingExceptionResolver.class);

	private static final int MINUTE = 60 * 1000;

	private static final AtomicLong lastNotifyAdminTime = new AtomicLong(-1);

	protected ModelAndView doResolveException(HttpServletRequest request,
											  HttpServletResponse response,
											  Object handler,
											  Exception ex) {
		logger.warn(buildLogMessage(ex, request) + "requestUrl :" + request.getServletPath(), ex);

		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			return getModelAndView(viewName, ex, request);
		}
		else {
			return null;
		}
	}

	private void notifyAdmins(String msg) {
		try {
			if((System.currentTimeMillis() - lastNotifyAdminTime.get()) > 10 * MINUTE) {
				lastNotifyAdminTime.set(System.currentTimeMillis());
			}
		}
		catch (Exception e) {
			logger.info(e.getMessage(), e);
		}
	}
}
