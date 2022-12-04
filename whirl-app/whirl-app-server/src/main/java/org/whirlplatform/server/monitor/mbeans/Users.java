package org.whirlplatform.server.monitor.mbeans;

import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.session.SessionManager;

public class Users implements UsersMBean {

    @Override
    public String[] getActiveUsers() {
        Set<ApplicationUser> users = SessionManager.getUsersFromAllSessions();
        Set<String> userStrings = new HashSet<String>();

        StringBuilder builder = new StringBuilder();

        for (ApplicationUser u : users) {
            builder.setLength(0);
            builder.append("{\"id\": \"").append(u.getId())
                    .append("\", \"login\": \"").append(u.getLogin())
                    .append("\", \"name\": \"").append(u.getName())
                    .append("\"},");
            userStrings.add(builder.toString());
        }

        // Добакдение обрамляющих скобок в первый и последний эл-ты
        String[] result = userStrings.toArray(new String[0]);
        if (result.length > 0) {
            result[0] = "{" + result[0];
            String lastEl = result[result.length - 1];
            result[result.length - 1] = lastEl.substring(0, lastEl.length() - 1) + "}";
        }
        return result;
    }

    @Override
    public void disconnectUser(String login) {
        SessionManager.unregisterUser(login);
    }

    @Override
    public String[] getApplicationUsers(String appCode) {
        Set<ApplicationUser> users = SessionManager.getUsersFromAllSessions();
        Set<String> userStrings = new HashSet<String>();

        StringBuilder builder = new StringBuilder();

        for (ApplicationUser u : users) {
            if (u.getApplication().getCode().equalsIgnoreCase(appCode)) {
                builder.setLength(0);
                builder.append("{\"id\": \"").append(u.getId())
                        .append("\", \"login\": \"").append(u.getLogin())
                        .append("\", \"name\": \"").append(u.getName())
                        .append("\"},");
                userStrings.add(builder.toString());
            }
        }

        // Добакдение обрамляющих скобок в первый и последний эл-ты
        String[] result = userStrings.toArray(new String[0]);
        if (result.length > 0) {
            result[0] = "{\"users\": [" + result[0];
            String lastEl = result[result.length - 1];
            result[result.length - 1] = lastEl.substring(0, lastEl.length() - 1) + "]}";
        }
        return result;
    }

    @Override
    public void disconnectApplicationUsers(String appCode) {
        if (appCode == null) {
            throw new IllegalArgumentException("Application code should not be null");
        }

        Set<ApplicationUser> users =
                new HashSet<ApplicationUser>(SessionManager.getUsersFromAllSessions());
        for (ApplicationUser u : users) {
            if (u.getApplication() != null && appCode.equals(u.getApplication().getCode())) {
                SessionManager.unregisterUser(u);
            }
        }
    }

}
