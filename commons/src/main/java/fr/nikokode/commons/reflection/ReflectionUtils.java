/**
 *
 */
package fr.nikokode.commons.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fr.nikokode.commons.exceptions.Unexpected;
import fr.nikokode.commons.log.Log4jLogger;

/**
 * @author ngiraud
 *
 */
public class ReflectionUtils {

    public static final Object newInstance(
            String className,
            Class<?>[] ctrParamTypes,
            Object[] ctrParamValues) {
        try {
            Class<?> objectClass = Class.forName(className);
            Constructor<?> ctr = objectClass.getDeclaredConstructor(ctrParamTypes);
            return ctr.newInstance(ctrParamValues);
        } catch (ClassNotFoundException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (SecurityException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NoSuchMethodException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NumberFormatException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalArgumentException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InstantiationException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalAccessException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InvocationTargetException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        }
    }

    public static final Object newInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (SecurityException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NumberFormatException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalArgumentException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InstantiationException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalAccessException e) {
            Log4jLogger.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        }
    }

}
