package org.whirlplatform.server.monitor.mbeans;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.whirlplatform.server.monitor.RunningEvent;

public class Events implements EventsMBean {

    // Синхронизация вообще нужна?
    private static final Map<String, RunningEvent> dbEvents = Collections
            .synchronizedMap(new HashMap<String, RunningEvent>());
    private static final Map<String, RunningEvent> javaEvents = Collections
            .synchronizedMap(new HashMap<String, RunningEvent>());
    private static final Map<String, RunningEvent> formRequests = Collections
            .synchronizedMap(new HashMap<String, RunningEvent>());
    private static final Map<String, RunningEvent> gridRequests = Collections
            .synchronizedMap(new HashMap<String, RunningEvent>());

    public static void addEvent(RunningEvent event) {
        switch (event.getType()) {
            case DBEVENT:
                dbEvents.put(event.getEventGUID(), event);
                break;
            case FORMREQUEST:
                formRequests.put(event.getEventGUID(), event);
                break;
            case JAVAEVENT:
                javaEvents.put(event.getEventGUID(), event);
                break;
            case GRIDREQUEST:
                gridRequests.put(event.getEventGUID(), event);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
    }

    public static void removeEvent(RunningEvent event) {
        switch (event.getType()) {
            case DBEVENT:
                dbEvents.remove(event.getEventGUID());
                break;
            case FORMREQUEST:
                formRequests.remove(event.getEventGUID());
                break;
            case JAVAEVENT:
                javaEvents.remove(event.getEventGUID());
                break;
            case GRIDREQUEST:
                gridRequests.remove(event.getEventGUID());
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
    }

    private String[] getMethodsByType(RunningEvent.Type type) {
        Map<String, RunningEvent> cur;
        switch (type) {
            case DBEVENT:
                cur = dbEvents;
                break;
            case FORMREQUEST:
                cur = formRequests;
                break;
            case JAVAEVENT:
                cur = javaEvents;
                break;
            case GRIDREQUEST:
                cur = gridRequests;
                break;
            default:
                cur = null; // чтобы компилятор не ругался
                break;
        }

        Set<String> eventStrings = new HashSet<String>();

        StringBuilder builder = new StringBuilder();

        for (RunningEvent e : cur.values()) {
            builder.setLength(0);
            builder.append("{\"guid\": \"").append(e.getEventGUID())
                    .append("\", \"login\": \"").append(e.getUserLogin())
                    .append("\", \"code\": \"").append(e.getCode())
                    .append("\", \"sql\": \"")
                    .append(e.getSql() == null ? "" : e.getSql().replaceAll("\"", "\\\""))
                    .append("\"},");
            // eventStrings.add(e.getEventGUID() + ": " + e.getUserLogin() +
            // ": " + e.getSql());
            eventStrings.add(builder.toString());
        }

        String[] result = eventStrings.toArray(new String[0]);
        if (result.length > 0) {
            result[0] = "{\"events\": [" + result[0];
            String lastEl = result[result.length - 1];
            result[result.length - 1] = lastEl
                    .substring(0, lastEl.length() - 1) + "]}";
        }
        return result;
    }

    @Override
    public String[] getActiveDBMethods() {
        return getMethodsByType(RunningEvent.Type.DBEVENT);
    }

    @Override
    public void stopActiveDBMethod(String methodId) {
        RunningEvent re = dbEvents.get(methodId);
        if (re != null) {
            re.onStop();
        }
    }

    @Override
    public String[] getActiveJavaMethods() {
        return getMethodsByType(RunningEvent.Type.JAVAEVENT);
    }

    //    @Override
    //    public void stopActiveJavaMethod(String methodId) {
    //        // TODO Auto-generated method stub
    //
    //    }

    @Override
    public String[] getActiveFormRequests() {
        return getMethodsByType(RunningEvent.Type.FORMREQUEST);
    }

    //    @Override
    //    public void stopActiveFormRequest(String requestId) {
    //        RunningEvent re = formRequests.get(requestId);
    //        if (re != null) {
    //            re.onStop();
    //        }
    //    }

    @Override
    public String[] getActiveGridRequests() {
        return getMethodsByType(RunningEvent.Type.GRIDREQUEST);
    }

    @Override
    public void stopActiveGridRequest(String requestId) {
        RunningEvent re = gridRequests.get(requestId);
        if (re != null) {
            re.onStop();
        }
    }

}
