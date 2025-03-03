package com.phasmidsoftware.dsaipg.huskySort.util;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class LazyLogger extends Logger {

    public LazyLogger(final Class<?> clazz) {
        super("LazyLogger");
        logger = Logger.getLogger(clazz);
    }

    public void trace(final Supplier<String> fMessage) {
        if (logger.isTraceEnabled())
            logger.trace(fMessage.get());
    }

    public void trace(final Supplier<String> fMessage, final Throwable t) {
        if (logger.isTraceEnabled())
            logger.trace(fMessage.get(), t);
    }

    public void debug(final Supplier<String> fMessage) {
        if (logger.isDebugEnabled())
            logger.debug(fMessage.get());
    }

    public void debug(final Supplier<String> fMessage, final Throwable t) {
        if (logger.isDebugEnabled())
            logger.debug(fMessage.get(), t);
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void trace(final Object message) {
        logger.trace("NOT lazy: " + message);
    }

    public void trace(final Object message, final Throwable t) {
        logger.trace("NOT lazy: " + message, t);
    }

    public void debug(final Object message) {
        logger.debug("NOT lazy: " + message);
    }

    public void debug(final Object message, final Throwable t) {
        logger.debug("NOT lazy: " + message, t);
    }

    public static Logger getLogger(final String name) {
        return Logger.getLogger(name);
    }

    public static Logger getLogger(final Class clazz) {
        return Logger.getLogger(clazz);
    }

    public static Logger getRootLogger() {
        return Logger.getRootLogger();
    }

    public static Logger getLogger(final String name, final LoggerFactory factory) {
        return Logger.getLogger(name, factory);
    }

    public void addAppender(final Appender newAppender) {
        logger.addAppender(newAppender);
    }

    public void assertLog(final boolean assertion, final String msg) {
        logger.assertLog(assertion, msg);
    }

    public void callAppenders(final LoggingEvent event) {
        logger.callAppenders(event);
    }

    public void error(final Object message) {
        logger.error(message);
    }

    public void error(final Object message, final Throwable t) {
        logger.error(message, t);
    }

    public void fatal(final Object message) {
        logger.fatal(message);
    }

    public void fatal(final Object message, final Throwable t) {
        logger.fatal(message, t);
    }

    public boolean getAdditivity() {
        return logger.getAdditivity();
    }

    public Enumeration getAllAppenders() {
        return logger.getAllAppenders();
    }

    public Appender getAppender(final String name) {
        return logger.getAppender(name);
    }

    public Level getEffectiveLevel() {
        return logger.getEffectiveLevel();
    }

    public LoggerRepository getLoggerRepository() {
        return logger.getLoggerRepository();
    }

    public ResourceBundle getResourceBundle() {
        return logger.getResourceBundle();
    }

    public void info(final Object message) {
        logger.info(message);
    }

    public void info(final Object message, final Throwable t) {
        logger.info(message, t);
    }

    public boolean isAttached(final Appender appender) {
        return logger.isAttached(appender);
    }

    public boolean isEnabledFor(final Priority level) {
        return logger.isEnabledFor(level);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void l7dlog(final Priority priority, final String key, final Throwable t) {
        logger.l7dlog(priority, key, t);
    }

    public void l7dlog(final Priority priority, final String key, final Object[] params, final Throwable t) {
        logger.l7dlog(priority, key, params, t);
    }

    public void log(final Priority priority, final Object message, final Throwable t) {
        logger.log(priority, message, t);
    }

    public void log(final Priority priority, final Object message) {
        logger.log(priority, message);
    }

    public void log(final String callerFQCN, final Priority level, final Object message, final Throwable t) {
        logger.log(callerFQCN, level, message, t);
    }

    public void removeAllAppenders() {
        logger.removeAllAppenders();
    }

    public void removeAppender(final Appender appender) {
        logger.removeAppender(appender);
    }

    public void removeAppender(final String name) {
        logger.removeAppender(name);
    }

    public void setAdditivity(final boolean additive) {
        logger.setAdditivity(additive);
    }

    public void setLevel(final Level level) {
        logger.setLevel(level);
    }

    public void setResourceBundle(final ResourceBundle bundle) {
        logger.setResourceBundle(bundle);
    }

    public void warn(final Object message) {
        logger.warn(message);
    }

    public void warn(final Object message, final Throwable t) {
        logger.warn(message, t);
    }

    private final Logger logger;
}