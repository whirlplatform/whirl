//package org.whirlplatform.server.log;
//
//import org.apache.commons.lang.StringUtils;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
////import org.apache.log4j.PropertyConfigurator;
//
//public class ContextPathInitializer implements ServletContextListener {
//
//    @Override
//    public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
//
//    }
//
//    @Override
//    public void contextInitialized(ServletContextEvent event) {
//        try {
//            String path = event.getServletContext().getContextPath();
//            path = StringUtils.isEmpty(path) ? "ROOT" : path;
//            System.setProperty("context.path", path.replaceFirst("/", ""));
//
////            PropertyConfigurator.configure(ContextPathInitializer.class
////                    .getResourceAsStream("/log4j.properties"));
//        } catch (Exception e) {
//        }
//    }
//
//}
