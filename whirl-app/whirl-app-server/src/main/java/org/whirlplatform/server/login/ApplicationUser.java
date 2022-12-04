package org.whirlplatform.server.login;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.server.compiler.CompilationData;
import org.whirlplatform.server.driver.multibase.Encryptor;
import org.whirlplatform.server.utils.ApplicationReference;

/**
 * Пользователь
 */
public class ApplicationUser implements Serializable {
    private static final long serialVersionUID = 1808419282462455596L;

    /**
     * Идентификатор пользователя
     */
    private String id;

    /**
     * ФИО
     */
    private String name;

    private boolean guest;

    /**
     * Логин
     */
    private String login;

    /**
     * Текущее приложение
     */
    private transient AtomicReference<ApplicationReference> application;

    private String ip;
    private String hostname;

    private Locale locale;
    private TimeZone timeZone;

    private Set<String> groups = new HashSet<String>();
    private Set<String> allowedApps = new HashSet<>();

    private Map<String, Object> javaObjects = new HashMap<String, Object>();


    private Encryptor encryptor;

    public ApplicationUser() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationElement getApplication() {
        if (application == null) {
            return null;
        }
        return application.get().getApplication();
    }

    /*
     * Метод используется только в логике управления текущим приложением.
     * Использовать для смены приложения программно нельзя.
     */
    public void setApplication(AtomicReference<ApplicationReference> application) {
        this.application = application;
    }

    public CompilationData getCompilationData() {
        if (application == null) {
            return null;
        }
        return application.get().getCompilationData();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public LocaleElement getLocaleElement() {
        return new LocaleElement(locale.getLanguage(), locale.getCountry());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ClientUser toClientUser() {
        ClientUser clientUser = new ClientUser();
        clientUser.setLogin(getLogin());
        clientUser.setGuest(isGuest());

        clientUser.setName(getName());

        if (getTimeZone() != null) {
            clientUser.setTimeZone(getTimeZone().getDisplayName(getLocale()));
        }

        return clientUser;
    }

    @Override
    public String toString() {
        return "[USER: " + id + " ROLE: " + application + "]";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void addGroup(String group) {
        if (group == null || group.trim().isEmpty()) {
            return;
        }
        groups.add(group.trim());
    }

    public void addGroups(Collection<String> groups) {
        for (String g : groups) {
            addGroup(g);
        }
    }

    public void removeGroup(String group) {
        if (group == null || group.trim().isEmpty()) {
            return;
        }
        groups.remove(group.trim());
    }

    public void clearGroups() {
        groups.clear();
    }

    public boolean hasGroups() {
        return !groups.isEmpty();
    }

    public boolean hasGroup(String group) {
        if (group == null || group.trim().isEmpty()) {
            return false;
        }
        return groups.contains(group.trim());
    }

    public void addAllowedApp(String code) {
        if (code == null || code.trim().isEmpty()) {
            return;
        }
        allowedApps.add(code.trim());
    }

    public void addAllowedApps(Collection<String> codes) {
        for (String c : codes) {
            addAllowedApp(c);
        }
    }

    public void removeAllowedApp(String code) {
        if (code == null || code.trim().isEmpty()) {
            return;
        }
        allowedApps.remove(code.trim());
    }

    public void clearAllowedApps() {
        allowedApps.clear();
    }

    public boolean hasAllowedApps() {
        return !allowedApps.isEmpty();
    }

    public boolean hasAllowedApp(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return allowedApps.contains(code.trim());
    }

    public Collection<String> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public boolean hasJavaObject(String name) {
        return javaObjects.containsKey(name);
    }

    public void saveJavaObject(String name, Object object) {
        javaObjects.put(name, object);
    }

    public Object loadJavaObject(String name) {
        return javaObjects.get(name);
    }

    public Object removeJavaObject(String name) {
        return javaObjects.remove(name);
    }

    public Encryptor getEncryptor() {
        return encryptor;
    }

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public ApplicationUser copy() {
        ApplicationUser user = new ApplicationUser();
        user.setId(getId());
        user.setName(getName());
        user.setGuest(isGuest());
        user.setLogin(getLogin());
        user.setIp(user.getIp());
        user.setHostname(getHostname());
        user.setLocale(getLocale());
        user.setTimeZone(getTimeZone());

        Set<String> newGroups = new HashSet<>();
        newGroups.addAll(groups);
        user.addGroups(newGroups);

        user.setEncryptor(getEncryptor());
        return user;
    }
}
