package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class LoggerFactoryImpl extends LoggerFactory {

    @Override
    public Logger getLogger(String name) {
        return new LoggerImpl(org.apache.logging.log4j.LogManager.getLogger(name));
    }

    private static class LoggerImpl implements Logger {

        private org.apache.logging.log4j.Logger log;

        private LoggerImpl(org.apache.logging.log4j.Logger log) {
            this.log = log;
        }

        @Override
        public void info(Object message) {
            log.info(String.valueOf(message));
        }

        @Override
        public void info(Object message, Throwable throwable) {
            log.info(String.valueOf(message), throwable);
        }

        @Override
        public void warn(Object message) {
            log.warn(String.valueOf(message));
        }

        @Override
        public void warn(Object message, Throwable throwable) {
            log.warn(String.valueOf(message), throwable);
        }

        @Override
        public void error(Object message) {
            if (message == null) {
                log.error("Empty error object", new Exception());
                return;
            }

            String messageStr = message.toString();
            log.error(messageStr);
        }

        @Override
        public void error(Object message, Throwable throwable) {
            String messageStr = String.valueOf(message);
            log.error(messageStr, throwable);
        }

        @Override
        public void debug(Object message) {
            log.debug(String.valueOf(message));
        }

        @Override
        public void debug(Object message, Throwable throwable) {
            log.debug(String.valueOf(message), throwable);
        }

    }

}
