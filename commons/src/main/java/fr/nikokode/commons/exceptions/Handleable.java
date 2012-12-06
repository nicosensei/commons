/**
 *
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
