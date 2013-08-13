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
package fr.nikokode.commons.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import fr.nikokode.commons.exceptions.Unexpected;

/**
 * @author ngiraud
 *
 */
public class ReflectionUtils {

    private final static Logger LOGGER = Logger.getLogger(ReflectionUtils.class);

    public static final Object newInstance(
            String className,
            Class<?>[] ctrParamTypes,
            Object[] ctrParamValues) {
        try {
            Class<?> objectClass = Class.forName(className);
            Constructor<?> ctr = objectClass.getDeclaredConstructor(ctrParamTypes);
            return ctr.newInstance(ctrParamValues);
        } catch (ClassNotFoundException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (SecurityException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NumberFormatException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InstantiationException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InvocationTargetException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        }
    }

    public static final Object newInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (SecurityException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (NumberFormatException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (InstantiationException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(ReflectionUtils.class, e);
            throw new Unexpected(e);
        }
    }

}
