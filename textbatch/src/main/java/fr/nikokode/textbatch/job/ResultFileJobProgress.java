/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.IOException;

import fr.nikokode.textbatch.Tool;
import fr.nikokode.textbatch.utils.CommonUtils;



/**
 * @author ngiraud
 *
 */
public abstract class ResultFileJobProgress extends BasicJobProgress {

    private ResultLogger logger;

    /**
     * @param linesToProcess
     * @throws JobProgressException
     */
    protected ResultFileJobProgress(
            int linesToProcess,
            String logId,
            String logPattern)
    throws JobProgressException {

        super(linesToProcess);

        logger = new ResultLogger(
                logId,
                getResultsFilePath(logId),
                logPattern);
    }

    public synchronized void logResult(String resultMessage) {
        logger.logResult(resultMessage);
    }

    public void copyResultsTo(String outputFile) throws JobProgressException {
        try {
            CommonUtils.copyFile(getResultFilePath(), outputFile);
        } catch (final IOException ioe) {
            throw JobProgressException.copyFailed(ioe);
        }
        Tool.getInstance().logInfo("Copied result file to " + outputFile);
    }

    protected String getResultFilePath() {
        return logger.getResultFilePath();
    }

    /**
     * By default return the given logId parameter.
     * Override if another behavior is desired.
     * @param logId the log ID
     * @return the result file base name
     */
    protected String getResultsFilePath(String logId) {
        return logId;
    }

}
