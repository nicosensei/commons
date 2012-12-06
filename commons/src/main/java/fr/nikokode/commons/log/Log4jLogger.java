/**
 *
 */
package fr.nikokode.commons.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Helper class that offers static logging methods that simplify
 * logging (notably, handles guarded logging in one place).
 * Log4j-based, provide a log4j.xml config file in the classpath.
 *
 * @author ngiraud
 *
 */
public class Log4jLogger {

    /**
     * Guard-logs a message from a sender object, with a given priority.
     * @param level the message level
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void log(Level level, Object sender, Object message) {
        Logger l = Logger.getLogger(sender.getClass());
        if (l.isEnabledFor(level)) {
            l.log(level, message);
        }
    }

    /**
     * Guard-logs a debug message.
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void debug(Object sender, Object message) {
        log(Level.DEBUG, sender, message);
    }

    /**
     * Guard-logs an information message.
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void info(Object sender, Object message) {
        log(Level.INFO, sender, message);
    }

    /**
     * Guard-logs a fatal message.
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void fatal(Object sender, Object message) {
        log(Level.FATAL, sender, message);
    }

    /**
     * Logs an error.
     * @param t the error
     * @param sender the sender object
     * @param message the message to log (if null the error's
     * localized message is used instead).
     */
    public static final void error(Object sender, Throwable t, Object message) {
        Logger l = Logger.getLogger(sender.getClass());
        l.error((message == null ? t.getLocalizedMessage() : message), t);
    }

}


