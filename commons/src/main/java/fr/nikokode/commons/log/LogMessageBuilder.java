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

/**
 * This interface should be implemented by classes that
 * wish to perform strict guard-logging. It allows to build messages only
 * if the log operation will actually be conducted.
 *
 * @author ngiraud
 *
 */
public interface LogMessageBuilder {

    /**
     * Build a log message.
     * @param messageKey a unique key identifying the message
     * @param params the parameters needed to build the message.
     * @return the message
     */
    String buildMessage(String messageKey, Object... params);

}
