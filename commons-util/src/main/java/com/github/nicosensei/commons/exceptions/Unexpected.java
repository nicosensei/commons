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
package com.github.nicosensei.commons.exceptions;


/**
 * Base class for all exceptions considered unexpected, i.e. that should not be handled,
 * and/or cannot be recovered from. Basically allows to wrap a {@link RuntimeException}
 * so it is labelled as being thrown from within the application code, and not dependency
 * code.
 *
 * @author ngiraud
 *
 */
public class Unexpected extends RuntimeException {

    /**
     * Serialization UID.
     */
    private static final long serialVersionUID = 6582984308105628980L;

    public Unexpected(String message, Throwable cause) {
        super(message, cause);
    }

    public Unexpected(String message) {
        super(message);
    }

    public Unexpected(Throwable cause) {
        super(cause);
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
