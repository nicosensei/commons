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
package com.github.nicosensei.commons.utils.datatype;

/**
 * Helper methods related to time.
 *
 * @author ngiraud
 *
 */
public class TimeFormatter {

    public final static String formatDuration(long lMs) {
        // Validate
        if (lMs > 0L) {
            // -- Declare variables
            String strDays = "";
            String strHours = "";
            String strMinutes = "";
            String strSeconds = "";
            String strMillisecs = "";
            String strReturn = "";
            long lRest;

            // -- Find values
            // -- -- Days
            strDays = String.valueOf(lMs / 86400000L);
            lRest = lMs % 86400000L;
            // -- -- Hours
            strHours = String.valueOf(lRest / 3600000L);
            lRest %= 3600000L;
            // -- -- Minutes
            strMinutes = String.valueOf(lRest / 60000L);
            lRest %= 60000L;
            // -- -- Seconds
            strSeconds = String.valueOf(lRest / 1000L);
            lRest %= 1000L;
            // -- -- Milliseconds
            strMillisecs = String.valueOf(lRest);

            // -- Format return
            // -- -- Days
            if (new Integer(strDays).intValue() != 0) {
                strReturn += strDays + "day ";
            }
            // -- -- Hours
            if (new Integer(strHours).intValue() != 0) {
                strReturn += strHours + "hr ";
            }
            // -- -- Minutes
            if (new Integer(strMinutes).intValue() != 0) {
                strReturn += strMinutes + "min ";
            }
            // -- -- Seconds
            if (new Integer(strSeconds).intValue() != 0) {
                strReturn += strSeconds + "sec ";
            }
            // -- -- Milliseconds
            if (new Integer(strMillisecs).intValue() != 0) {
                strReturn += strMillisecs + "ms";
            }

            return strReturn;
        } else if (lMs == 0L) {

            return "0ms";
        } else {
            return "-1";
        }
    }

}
