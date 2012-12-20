/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA
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
     * Logs a message from a sender object, with a given priority.
     * @param level the message level
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void log(Level level, Object sender, Object message) {
        log(level, sender.getClass(), message);
    }

    /**
     * Logs a message from a sender object, with a given priority.
     * @param level the message level
     * @param senderClass the sender object's class
     * @param message the message to log
     */
    public static final void log(Level level, Class<?> senderClass, Object message) {
        Logger l = Logger.getLogger(senderClass);
        if (l.isEnabledFor(level)) {
            l.log(level, message);
        }
    }

    /**
     * Guard-logs a message from a sender object, with a given priority.
     * The message is built only if the level is activated for the sender class.
     * @param level the message level
     * @param sender the sender object
     * @param messageKey the key identifying the message
     * @param messageParams the parameters necessary to build the message
     */
    public static final void log(
            Level level,
            LogMessageBuilder sender,
            String messageKey,
            Object... messageParams) {
        Logger l = Logger.getLogger(sender.getClass());
        if (l.isEnabledFor(level)) {
            l.log(level, sender.buildMessage(messageKey, messageParams));
        }
    }

    /**
     * Logs a debug message.
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void debug(Object sender, Object message) {
        log(Level.DEBUG, sender, message);
    }

    /**
     * Logs a debug message.
     * @param senderClass the sender object's class
     * @param message the message to log
     */
    public static final void debug(Class<?> senderClass, Object message) {
        log(Level.DEBUG, senderClass, message);
    }

    /**
     * Guard-logs a debug message from a sender object, with a given priority.
     * The message is built only if the level is activated for the sender class.
     * @param level the message level
     * @param sender the sender object
     * @param messageKey the key identifying the message
     * @param messageParams the parameters necessary to build the message
     */
    public static final void debug(
            Level level,
            LogMessageBuilder sender,
            String messageKey,
            Object... messageParams) {
        log(Level.DEBUG, sender, messageKey, messageParams);
    }

    /**
     * Logs an information message.
     * @param sender the sender object
     * @param message the message to log
     */
    public static final void info(Object sender, Object message) {
        log(Level.INFO, sender, message);
    }

    /**
     * Logs an information message.
     * @param senderClass the sender object's class
     * @param message the message to log
     */
    public static final void info(Class<?> senderClass, Object message) {
        log(Level.INFO, senderClass, message);
    }

    /**
     * Guard-logs an informmation message from a sender object, with a given priority.
     * The message is built only if the level is activated for the sender class.
     * @param level the message level
     * @param sender the sender object
     * @param messageKey the key identifying the message
     * @param messageParams the parameters necessary to build the message
     */
    public static final void info(
            Level level,
            LogMessageBuilder sender,
            String messageKey,
            Object... messageParams) {
        log(Level.INFO, sender, messageKey, messageParams);
    }

    /**
     * Logs an error.
     * @param t the error
     * @param sender the sender object
     * @param message the message to log (if null the error's
     * localized message is used instead).
     */
    public static final void error(Object sender, Throwable t, Object message) {
        error(sender.getClass(), t, message);
    }

    /**
     * Logs an error.
     * @param t the error
     * @param senderClass the sender object's class
     * @param message the message to log (if null the error's
     * localized message is used instead).
     */
    public static final void error(Class<?> senderClass, Throwable t, Object message) {
        Logger l = Logger.getLogger(senderClass);
        l.error((message == null ? t.getLocalizedMessage() : message), t);
    }

    /**
     * Logs an error.
     * @param t the error
     * @param sender the sender object
     * @param message the message to log (if null the error's
     * localized message is used instead).
     */
    public static final void error(Object sender, Throwable t) {
        error(sender.getClass(), t, t.getLocalizedMessage());
    }

    /**
     * Logs an error.
     * @param t the error
     * @param senderClass the sender object's class
     */
    public static final void error(Class<?> senderClass, Throwable t) {
        error(senderClass, t, t.getLocalizedMessage());
    }

}


