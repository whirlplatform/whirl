package org.whirlplatform.server.log;

import org.whirlplatform.server.log.impl.LoggerFactoryImpl;

public abstract class LoggerFactory {

    private static LoggerFactory _instance;

    private static LoggerFactory get() {
        if (_instance == null) {
            _instance = new LoggerFactoryImpl();
        }
        return _instance;
    }

    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return get().getLogger((String) null);
        }
        return get().getLogger(clazz.getSimpleName());
    }
    public abstract Logger getLogger(String name);

}
