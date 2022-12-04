//package org.whirlplatform.rpc.server;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.logging.Level;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.glassfish.jersey.servlet.ServletContainer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import DataService;
//
//public class JerseyServletContainer extends ServletContainer {
//
//    Logger logger = LoggerFactory.getLogger(JerseyServletContainer.class);
//
//    private DataService service;
//    private ServletConfig conf;
//
//    @Override
//    public void init() throws ServletException {
//        java.util.logging.Logger.getGlobal().setLevel(Level.FINEST);
//        super.init();
//    }
//
//    @Override
//    public String getInitParameter(String name) {
//        return "javax.ws.rs.Application".equals(name) ? TestConfig.class.getName() : super.getInitParameter(name);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        for (String att = ""; req.getAttributeNames().hasMoreElements(); att = req.getAttributeNames().nextElement()) {
//            System.out.println(req.getAttribute(att));
//        }
//        super.doPost(req, resp);
//    }
//
//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
//        Map<String, String[]> parameterMap = req.getParameterMap();
//        for (Entry<String, String[]> e : parameterMap.entrySet()) {
//            System.out.println(e.getKey() + " - " + e.getValue()[0]);
//        }
//        super.service(req, response);
//    }
//}
