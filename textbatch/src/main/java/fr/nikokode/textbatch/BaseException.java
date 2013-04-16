/**
 *
 */
package fr.nikokode.textbatch;

import java.text.MessageFormat;

import org.apache.log4j.Level;

/**
 * Base class for all exception generated by the tool.
 *
 * @author ngiraud
 *
 */
public abstract class BaseException extends Exception {

    private static final long serialVersionUID = 8116761541462247081L;

    private final String code;
    private final Level criticity;
    private final boolean verbose;

    /**
     *
     * @param code
     * @param messageFormat
     * @param params
     * @param criticity
     * @param verbose
     */
    protected BaseException(
            String code,
            String messageFormat,
            String[] params,
            Level criticity,
            boolean verbose) {
        super(new MessageFormat(messageFormat).format(params));
        this.code = code;
        this.criticity = criticity;
        this.verbose = verbose;
    }

    /**
     *
     * @param code
     * @param messageFormat
     * @param params
     * @param criticity
     * @param cause
     * @param verbose
     */
    protected BaseException(
            String code,
            String messageFormat,
            String[] params,
            Level criticity,
            Throwable cause,
            boolean verbose) {
        super(new MessageFormat(messageFormat).format(params), cause);
        this.code = code;
        this.criticity = criticity;
        this.verbose = verbose;
    }

    protected BaseException(
            String code,
            String messageFormat,
            String[] params,
            Level criticity) {
        this(code, messageFormat, params, criticity, true);
    }

    protected BaseException(
            String code,
            String messageFormat,
            String[] params,
            Level criticity,
            Throwable cause) {
        this(code, messageFormat, params, criticity, cause, true);
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

    public boolean isVerbose() {
        return verbose;
    }

}
