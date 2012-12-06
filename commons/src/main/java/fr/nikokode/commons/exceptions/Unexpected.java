/**
 *
 */
package fr.nikokode.commons.exceptions;


/**
 * Base class for all exceptions considered unexpected, i.e. that should not be handled,
 * and/or cannot be recovered from. Basically allows to wrap a {@link RuntimeException}
 * so it is labelled as being thrown from within the application code, and not dependency
 * code.
 *
 * @author ngiraud
 *
 */
public abstract class Unexpected extends RuntimeException {

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
