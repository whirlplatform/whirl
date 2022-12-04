package org.whirlplatform.server.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.login.ApplicationUser;

public class SessionManager implements Serializable {

    private static final String PROPERTY_WHIRL_TOKEN_SESSION_TIME = "whirlTokenSessionTime";

    private static final long serialVersionUID = -7276165947238646337L;

    private static final String TOKEN_MANAGER = "TOKEN_MANAGER";

    private static final long SESSION_TIME =
            Long.parseLong(System.getProperty(PROPERTY_WHIRL_TOKEN_SESSION_TIME, "120000"));

    private static final long UNREGISTER_TIME = 6000;
    // глобальное хранилище сессий. Добавляются и удаляются листенером
    // SessionListener
    private final static Set<HttpSession> sessions = Collections
            .newSetFromMap(new ConcurrentHashMap<HttpSession, Boolean>());
    private final Map<SessionToken, Date> touch = new ConcurrentHashMap<SessionToken, Date>();
    private final Map<SessionToken, ApplicationUser> userToken =
            new ConcurrentHashMap<SessionToken, ApplicationUser>();
    private boolean changed = false;

    private SessionManager() {

    }

    // вызывается из SessionListener
    public static void attachSession(HttpSession session) {
        sessions.add(session);
    }

    // вызывается из SessionListener
    public static void detachSession(HttpSession session) {
        sessions.remove(session);
    }

    /**
     * Доступ к классу {@link SessionManager} должен осуществляться синхронизированно.
     *
     * @return екземпляр {@link SessionManager}
     */
    public static SessionManager get(HttpSession httpSession) {
        if (httpSession.getAttribute(TOKEN_MANAGER) == null) {
            SessionManager manager = new SessionManager();
            httpSession.setAttribute(TOKEN_MANAGER, manager);
        }
        // SessionManager на этом этапе определён
        // проверим, есть ли в карте сессий текущая сессия.
        // если есть, то вернём SessionManager для неё.
        // иначе нужно бросить исключение.

        // возможно, сессия меняется. Тогда метод contains не обнаружит сессию.
        // но теоретически должна срабатывать регистрация в листенере.
        // так что, полагаю, тут всё хорошо.
        SessionManager sessionManager = (SessionManager) httpSession
                .getAttribute(TOKEN_MANAGER);
        if (sessionManager.isChanged()) {
            httpSession.setAttribute(TOKEN_MANAGER, sessionManager); // для
            // DeltaManager
            sessionManager.setUnchanged();
        }
        return sessionManager;
    }

    /*
     * Цикл по всем доступным сессиям. Из каждой сессии получаю sessionManager.
     * И с его помощью извлекаю всех пользователей этой сессии. Накапливаю
     * пользователей в отдельном множестве и возвращаю его. Аналог функции-члену
     * класса SessionManager getAllUsers, однако, не требует создания экземпляра
     * SessionManager
     */
    public static Set<ApplicationUser> getUsersFromAllSessions() {
        Iterator<HttpSession> sesIter = sessions.iterator();
        Set<ApplicationUser> allUsers = new HashSet<ApplicationUser>();
        while (sesIter.hasNext()) {
            HttpSession ses = sesIter.next();
            SessionManager manager = SessionManager.get(ses);
            allUsers.addAll(manager.getAllUsers());
        }
        return allUsers;
    }

    public static void tryKillUnusedSessions() {
        Iterator<HttpSession> sesIter = sessions.iterator();
        while (sesIter.hasNext()) {
            HttpSession ses = sesIter.next();
            SessionManager manager = SessionManager.get(ses);
            manager.kill();
        }
    }

    public static void unregisterUser(ApplicationUser user) {
        Iterator<HttpSession> sesIter = sessions.iterator();
        while (sesIter.hasNext()) {
            HttpSession ses = sesIter.next();
            SessionManager manager = SessionManager.get(ses);

            Iterator<SessionToken> tokenIter = manager.userToken.keySet()
                    .iterator();
            while (tokenIter.hasNext()) {
                SessionToken t = tokenIter.next();
                ApplicationUser u = manager.userToken.get(t);
                if (u.equals(user)) {
                    // Нужна синхронизация по manager?
                    manager.unregisterToken(t);
                    manager.setChanged();
                }
            }
        }
    }

    public static void unregisterUser(String login) {
        if (login == null || login.isEmpty()) {
            return;
        }

        Iterator<HttpSession> sesIter = sessions.iterator();
        while (sesIter.hasNext()) {
            HttpSession ses = sesIter.next();
            SessionManager manager = SessionManager.get(ses);

            Iterator<SessionToken> tokenIter = manager.userToken.keySet()
                    .iterator();
            while (tokenIter.hasNext()) {
                SessionToken t = tokenIter.next();
                ApplicationUser u = manager.userToken.get(t);
                if (u.getLogin().equalsIgnoreCase(login)) {
                    // Нужна синхронизация по manager?
                    manager.unregisterToken(t);
                    manager.setChanged();
                }
            }
        }
    }

    /* Эта процедура нужна лишь для тестовых целей. В продакшн не добавлять. */
    public static boolean checkTokenId(String tokenId) { // удалить. нельзя
        // оставлять такую
        // дыру

        Iterator<HttpSession> siter = sessions.iterator();
        while (siter.hasNext()) {
            HttpSession ses = siter.next();
            SessionManager mgr = SessionManager.get(ses);
            Iterator<SessionToken> iter = mgr.userToken.keySet().iterator();
            while (iter.hasNext()) {
                SessionToken t = iter.next();
                String tid = t.getTokenId().trim();

                if (null != tokenId && tokenId.trim().equalsIgnoreCase(tid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setUnchanged() {
        this.changed = false;
    }

    public void setChanged() {
        this.changed = true;
    }

    // думаю, этот метод лучше назвать unregisterToken
    public void unregisterSession(HttpSession session) {
        // _log.info("+++++ Unregister session: " + session.getId());
        synchronized (this) {
            String sessionId = session.getId();

            Iterator<SessionToken> tokensIter = userToken.keySet().iterator();
            while (tokensIter.hasNext()) {
                SessionToken t = tokensIter.next();

                // удаляем наш сессионный ключ
                if (sessionId != null && t.getSessionId().equals(sessionId)) {
                    unregisterToken(t);
                }
            }
            this.setChanged();
        }
    }

    public void registerToken(SessionToken token) {
        synchronized (this) {
            touch.put(token, new Date());
            userToken.put(token, new ApplicationUser()); // dummy applictionuser
            this.setChanged();
        }
    }

    public void unregisterToken(SessionToken token) {
        synchronized (this) {
            touch.remove(token);
            userToken.remove(token);
            this.setChanged();
        }
    }

    public void registerUser(SessionToken token, ApplicationUser user) {
        // _log.info("Register user: " + token + " user: " + user);
        synchronized (this) {
            checkToken(token);
            userToken.put(token, user);
            this.setChanged();
        }
    }

    public void unregisterUser(SessionToken token) {
        synchronized (this) {
            if (!userToken.containsKey(token)) {
                return;
            }
            userToken.remove(token);
            this.setChanged();
        }
    }

    private void checkToken(SessionToken token) {
        synchronized (this) {
            if (!userToken.containsKey(token)) {
                throw new RuntimeException("Token not registered: " + token);
            }
        }
    }

    /**
     * Если пользователь не найден, выбрасывает исключение с параметром SessionExpired
     *
     * @param token
     * @return
     * @throws CustomException
     */
    public ApplicationUser getUser(SessionToken token) throws CustomException {
        // _log.info("Getting user from session: " + token);
        synchronized (this) {
            ApplicationUser user = userToken.get(token);
            if (user == null) {
                throw new CustomException(
                        I18NMessage.getMessage(I18NMessage.getRequestLocale())
                                .alert_sessionExpired(), true);
            }
            return user;
        }
    }

    private Set<SessionToken> findUserTokens(ApplicationUser user) {
        Set<SessionToken> result = new HashSet<SessionToken>();
        for (Entry<SessionToken, ApplicationUser> e : userToken.entrySet()) {
            SessionToken t = e.getKey();
            ApplicationUser u = e.getValue();
            if (u != null && u.getId().equals(user.getId())) {
                result.add(t);
            }
        }
        return result;
    }

    public boolean hasAnotherUserToken(SessionToken token,
                                       ApplicationUser user) {
        synchronized (this) {
            Set<SessionToken> set = findUserTokens(user);
            if (set != null) {
                for (SessionToken t : set) {
                    if (t.getSessionId().equals(token.getSessionId())
                            && !t.getTokenId().equals(token.getTokenId())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean hasAnotherUserSession(SessionToken token,
                                         ApplicationUser user) {
        synchronized (this) {
            Set<SessionToken> set = findUserTokens(user);
            if (set != null) {
                Iterator<SessionToken> iter = set.iterator();
                while (iter.hasNext()) {
                    SessionToken t = iter.next();
                    // если в списке токенов нет нашего токена, то удаляем его
                    // удаляем если токен не был активен в течении SESSION_TIME секунд
                    Date date = touch.get(t);
                    Date timeToKillBefore = new Date(
                            System.currentTimeMillis() - SESSION_TIME);
                    if (!userToken.containsKey(t)
                            || (date == null || date.before(timeToKillBefore))) {
                        unregisterToken(token);
                        setChanged();
                    } else if (!t.getSessionId().equals(token.getSessionId())
                            && !t.getTokenId().equals(token.getTokenId())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void touch(SessionToken token) {
        synchronized (this) {
            Date now = new Date();
            if (!"session".equals(token.getSessionId())) {
                touch.put(token, now);
                setChanged();
            }
        }
    }

    public void markForUnregister(final SessionToken token) {
        synchronized (this) {
            Date newTime = new Date(System.currentTimeMillis() - SESSION_TIME
                    + UNREGISTER_TIME);
            touch.put(token, newTime);
            setChanged();
        }
    }

    public Set<ApplicationUser> getAllUsers() {
        synchronized (this) {
            Map<String, ApplicationUser> result = new HashMap<>();
            for (ApplicationUser u : userToken.values()) {
                result.put(u.getId(), u);
            }
            return Collections.unmodifiableSet(new HashSet<>(result.values()));
        }
    }

    public void kill() {
        synchronized (this) {
            Iterator<SessionToken> iter = userToken.keySet().iterator();
            while (iter.hasNext()) {
                SessionToken t = iter.next();
                Date date = touch.get(t);
                Date now = new Date(System.currentTimeMillis() - SESSION_TIME);
                if (date == null || date.before(now)) {
                    iter.remove();
                    userToken.remove(t);
                    touch.remove(t);
                    setChanged();
                }
            }
        }
    }

    public void unregisterApplicationUsers(String appCode) {
        if (appCode == null || appCode.isEmpty()) {
            return;
        }
        for (Entry<SessionToken, ApplicationUser> e : userToken.entrySet()) {
            if (appCode.equalsIgnoreCase(
                    e.getValue().getApplication().getCode())) {
                userToken.remove(e.getKey());
            }
        }
    }

    /*
     * Эта процедура нужна для тестовых целей. В продакш не обязательно
     * добавлять.
     */
    @Override
    public String toString() {
        String s = "SessionManager hash code: " + this.hashCode() + ", class: "
                + this.getClass() + ": ";
        s += ", touch: [";
        Iterator<SessionToken> touchIter = this.touch.keySet().iterator();
        while (touchIter.hasNext()) {
            SessionToken st = touchIter.next();
            s += "[key: " + st + ", value: " + this.touch.get(st) + "] ";
        }
        s += "]";
        // touch SessionToken, Date
        s += "userToken: [";
        Iterator<SessionToken> userTokenIter = this.userToken.keySet()
                .iterator();
        while (userTokenIter.hasNext()) {
            SessionToken st = userTokenIter.next();
            s += "[key: " + st + ", value: " + this.userToken.get(st) + "] ";
        }
        s += "]";
        return s;
    }
}
