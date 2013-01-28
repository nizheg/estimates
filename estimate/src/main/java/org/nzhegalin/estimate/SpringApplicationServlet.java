package org.nzhegalin.estimate;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;

@SuppressWarnings("serial")
public class SpringApplicationServlet extends ApplicationServlet {

	private WebApplicationContext ctx;
	private String beanName;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		this.ctx = (WebApplicationContext) servletConfig.getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		this.beanName = servletConfig.getInitParameter("springBeanName");
		if (beanName == null) {
			throw new ServletException("Application bean name not specified in servlet parameters");
		}
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request) throws ServletException {
		return (Application) ctx.getBean(this.beanName);
	}

}
