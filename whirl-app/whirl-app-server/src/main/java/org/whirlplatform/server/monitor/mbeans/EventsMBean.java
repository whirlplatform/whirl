package org.whirlplatform.server.monitor.mbeans;

public interface EventsMBean {

    public static final String OBJECT_NAME = "Events";

    String[] getActiveDBMethods();

    void stopActiveDBMethod(String methodId);

    String[] getActiveJavaMethods();

    //    public void stopActiveJavaMethod(String methodId);

    String[] getActiveFormRequests();

    //    public void stopActiveFormRequest(String requestId);

    String[] getActiveGridRequests();

    void stopActiveGridRequest(String requestId);
}
