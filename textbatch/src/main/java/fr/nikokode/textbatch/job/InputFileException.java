/**
 *
 */
package fr.nikokode.textbatch.job;

import org.apache.log4j.Level;

import fr.nikokode.textbatch.ToolException;


/**
 * Generates exceptions related to input file operations.
 * @author ngiraud
 *
 */
public class InputFileException extends ToolException {

    private enum CODE {
        INPUT_FILE_NOT_FOUND("The input file {0} was not found."),
        INPUT_FILE_CLOSE_FAILED("Failed to close input file {0}: {1}"),
        INPUT_FILE_READ_ERROR("Error while reading input file {0}: {1}"),
        INPUT_FILE_IO_ERROR("IO error occured when processing {0}: {1}");

        private String fmt;
        CODE(String fmt) {
            this.fmt = fmt;
        }
    }

    private static final long serialVersionUID = -6525579233265232930L;

    protected InputFileException(
            String code, String messageFormat,
            String[] params, Level criticity, Throwable cause) {
        super(code, messageFormat, params, criticity, cause);
    }

    protected InputFileException(String code, String messageFormat,
            String[] params, Level criticity) {
        super(code, messageFormat, params, criticity);
    }

    private InputFileException(
            CODE code,
            String[] params,
            Level criticity,
            Throwable cause) {
        super(code.name(), code.fmt, params, criticity, cause);
    }

    private InputFileException(
            CODE code, String[] params, Level criticity) {
        super(code.name(), code.fmt, params, criticity);
    }

    public static final InputFileException fileNotFound(String filePath) {
        return new InputFileException(
                CODE.INPUT_FILE_NOT_FOUND,
                new String[] { filePath },
                Level.FATAL);
    }

    public static final InputFileException closeFailed(
            String filePath, Exception cause) {
        return new InputFileException(
                CODE.INPUT_FILE_CLOSE_FAILED,
                new String[] { filePath, cause.getClass().getSimpleName() },
                Level.ERROR,
                cause);
    }

    public static final InputFileException readError(
            String filePath, Exception cause) {
        return new InputFileException(
                CODE.INPUT_FILE_READ_ERROR,
                new String[] { filePath, cause.getClass().getSimpleName() },
                Level.ERROR,
                cause);
    }

    public static final InputFileException ioError(
            String filePath, Exception cause) {
        return new InputFileException(
                CODE.INPUT_FILE_IO_ERROR,
                new String[] { filePath, cause.getClass().getSimpleName() },
                Level.ERROR,
                cause);
    }

}
