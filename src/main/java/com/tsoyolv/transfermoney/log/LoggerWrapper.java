package com.tsoyolv.transfermoney.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LoggingException;

/**
 * Wrapper created for logging flexibility.
 * If extra logging will be needed, this class would be the only place to change
 */
public class LoggerWrapper {

    public static final String JDBC_LOGGER_NAME = "jdbc.logger";
    public static final String REQUESTS_LOGGER_NAME = "request.logger";

    private Logger logger;

    private LoggerWrapper(Logger logger) {
        this.logger = logger;
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void info(String message, Object ... params) {
        logger.info(message, params);
    }

    public void debug(String message, Object ... params) {
        logger.debug(message, params);
    }

    public void warn(String message, Object ... params) {
        logger.warn(message, params);
    }

    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    public void error(String message, Object ... params) {
        logger.error(message, params);
    }

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static LoggerWrapper getLogger(String name) {
        Logger logger = LogManager.getLogger(name);
        if (logger == null) {
            throw new LoggingException("There is no Logger with name: " + name);
        }
        return new LoggerWrapper(logger);
    }

    public static LoggerWrapper getLogger(Class clazz) {
        Logger logger = LogManager.getLogger(clazz);
        if (logger == null) {
            String className = clazz == null ? "null" : clazz.getCanonicalName();
            throw new LoggingException("There is no Logger for class: " + className);
        }
        return new LoggerWrapper(logger);
    }
}
