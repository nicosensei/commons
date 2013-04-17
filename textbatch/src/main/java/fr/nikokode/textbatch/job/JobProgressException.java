/**
 *
 */
package fr.nikokode.textbatch.job;

import org.apache.log4j.Level;

import fr.nikokode.textbatch.ToolException;



/**
 * @author ngiraud
 *
 */
public class JobProgressException extends ToolException {

    private static final long serialVersionUID = 2067714794562082336L;

    private JobProgressException(
            String code,
            String messageFormat,
            String[] params, Level criticity,
            Throwable cause) {
        super(code, messageFormat, params, criticity, cause);
    }

    public static JobProgressException openResultFileFailed(
            String filePath, Exception cause) {
        return new JobProgressException(
                "JOBPROGRESS_OPEN_RESULT_FILE_FAILED",
                "Failed to open result file {0}",
                new String[] { filePath },
                Level.FATAL,
                cause);
    }

    public static JobProgressException initFailed(Throwable t) {
        return new JobProgressException(
                "JOBPROGRESS_INIT_FAILED",
                "Failed to initialize progress: {0}",
                new String[] { t.getLocalizedMessage() },
                Level.FATAL,
                t);
    }

    public static JobProgressException copyFailed(Throwable t) {
        return new JobProgressException(
                "JOBPROGRESS_COPY_FAILED",
                "Failed to copy result file: {0}",
                new String[] { t.getLocalizedMessage() },
                Level.ERROR,
                t);
    }

}
