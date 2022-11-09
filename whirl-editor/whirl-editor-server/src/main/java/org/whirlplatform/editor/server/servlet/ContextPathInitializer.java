//package org.whirlplatform.editor.server.servlet;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.PropertyConfigurator;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//public class ContextPathInitializer implements ServletContextListener {
//
//	@Override
//	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
//
//	}
//
//	@Override
//	public void contextInitialized(ServletContextEvent event) {
//		String path = event.getServletContext().getContextPath();
//		path = StringUtils.isEmpty(path) ? "ROOT" : path;
//		System.setProperty("context.path", path);
//
//		PropertyConfigurator.configure(ContextPathInitializer.class
//				.getResourceAsStream("/log4j.propertiesex"));
//	}
//
//}
