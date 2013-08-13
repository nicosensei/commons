package fr.nikokode.commons.utils.datatype;

/**
 * Handles the formatting of byte size data.
 *
 * @author ngiraud
 *
 */
public class ByteCountFormatter {

    /**
     * Outputs a human readable byte size.
     *
     * @param bytes the byte size
     * @param si use SI units if true, binary units otherwise
     * @return a human readable string
     *
     * @see http://stackoverflow.com/a/3758880 (original source code)
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Outputs a human readable byte size in SI units.
     *
     * @param bytes the byte size
     * @return a human readable string
     */
    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, true);
    }

}
