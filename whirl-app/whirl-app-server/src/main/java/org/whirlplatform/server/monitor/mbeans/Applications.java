package org.whirlplatform.server.monitor.mbeans;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.session.SessionManager;
import org.whirlplatform.server.utils.ContextUtil;

public class Applications implements ApplicationsMBean {

    private static final Set<String> allowedApps;
    private static final Set<String> blockedApps;

    static {
        allowedApps = new HashSet<String>();
        blockedApps = new HashSet<String>();

        String allowedStr = ContextUtil.lookup("Whirl/application/allowed");
        if (allowedStr != null) {
            for (String code : allowedStr.replaceAll(" ", "").split(",")) {
                allowedApps.add(code);
            }
        }

        String forbiddenStr = ContextUtil.lookup("Whirl/application/forbidden");
        if (forbiddenStr != null) {
            for (String code : forbiddenStr.replaceAll(" ", "").split(",")) {
                blockedApps.add(code);
            }
        }
    }

    public static Set<String> getAllowedApps() {
        return Collections.unmodifiableSet(allowedApps);
    }

    public static Set<String> getBlockedApps() {
        return Collections.unmodifiableSet(blockedApps);
    }

    @Override
    public String[] getActiveApplications() {
        Set<ApplicationUser> users = SessionManager.getUsersFromAllSessions();
        Set<String> apps = new HashSet<String>();

        StringBuilder builder = new StringBuilder();

        for (ApplicationUser u : users) {
            builder.setLength(0);
            ApplicationElement app = u.getApplication();
            if (app != null) {
                builder.append("{\"id\": \"").append(app.getId())
                        .append("\", \"code\": \"").append(app.getCode())
                        .append("\", \"name\": \"").append(app.getName())
                        .append("\"},");
                apps.add(builder.toString());
            }
        }

        // Добакдение обрамляющих скобок в первый и последний эл-ты
        String[] result = apps.toArray(new String[0]);
        if (result.length > 0) {
            result[0] = "{\"apps\": [" + result[0];
            String lastEl = result[result.length - 1];
            result[result.length - 1] = lastEl.substring(0, lastEl.length() - 1) + "]}";
        }
        return result;
    }

    @Override
    public String[] getCurrentBlocks() {
        return blockedApps.toArray(new String[0]);
    }

    @Override
    public synchronized void addBlock(String appCode) {
        blockedApps.add(appCode);
    }

    @Override
    public synchronized void removeBlock(String appCode) {
        if (appCode == null) {
            throw new IllegalArgumentException("Application code should not be null");
        }
        blockedApps.remove(appCode);

        Set<ApplicationUser> users =
                new HashSet<ApplicationUser>(SessionManager.getUsersFromAllSessions());
        for (ApplicationUser u : users) {
            if (u.getApplication() != null && appCode.equals(u.getApplication().getCode())) {
                SessionManager.unregisterUser(u);
            }
        }
    }

}
