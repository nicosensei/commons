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
package fr.nikokode.commons.exceptions;

import java.text.MessageFormat;

import org.apache.log4j.Level;

/**
 * Base class for all exceptions considered handleable, i.e. that should be caught because
 * they are expected to occur and can be recovered from.
 *
 * @author ngiraud
 *
 */
public abstract class Handleable extends Exception {

    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 6707873389019447919L;

    private final String code;
    private final Level criticity;

    /**
     *
     * @param code
     * @param messageFormat
     * @param params
     * @param criticity
     * @param verbose
     */
    protected Handleable(
            String code,
            String messageFormat,
            String[] params,
            Level criticity) {
        super(new MessageFormat(messageFormat).format(params));
        this.code = code;
        this.criticity = criticity;
    }

    /**
     *
     * @param code
     * @param messageFormat
     * @param params
     * @param criticity
     * @param cause
     */
    protected Handleable(
            String code,
            String messageFormat,
            String[] params,
            Level criticity,
            Throwable cause) {
        super(new MessageFormat(messageFormat).format(params), cause);
        this.code = code;
        this.criticity = criticity;
    }

    public Level getCriticity() {
        return criticity;
    }

    public String getCode() {
        return code;
    }

    public Throwable getRootCause() {
        Throwable rootCause = this;
        Throwable cause = getCause();
        while (cause != null) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }

}
