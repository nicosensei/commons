/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import fr.nikokode.textbatch.Tool;


/**
 * @author ngiraud
 *
 */
public class ResultLogger {

    public static final String FOLDER =
            Tool.getInstance().getProperty(ResultLogger.class, "folder");

    private static final String EXT =
            Tool.getInstance().getProperty(ResultLogger.class, "ext");

    private String resultFile;
    private Logger resultLogger;

    public final static String DEFAULT_LOG_PATTERN = "%m";

    public static final String TIMESTAMP_PATTERN = "yyyy_MM_dd_HHmmss";

    public ResultLogger(String logId, String baseName, String logPattern)
    throws JobProgressException {

        resultLogger = Logger.getLogger(logId);
        resultLogger.setAdditivity(false);
        this.resultFile = initResultFile(baseName);

        try {
            resultLogger.addAppender(new FileAppender(
                    new PatternLayout(logPattern),
                    this.resultFile,
                    false));
        } catch (IOException e) {
            throw JobProgressException.openResultFileFailed(resultFile, e);
        }
        resultLogger.setLevel(Level.INFO);
    }

    public synchronized void logResult(String resultMessage) {
        resultLogger.info(resultMessage);
    }

    public synchronized void logResult(String resultMessage, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        resultLogger.info(resultMessage + "\n" + sw.toString());
        try {
            sw.close();
        } catch (IOException e1) {

        }
    }

    public String getResultFilePath() {
        return resultFile;
    }

    private String initResultFile(String baseName) {

        SimpleDateFormat df = new SimpleDateFormat(TIMESTAMP_PATTERN);
        String resultFile =
            FOLDER
            + (FOLDER.endsWith(File.separator) ? "" : File.separator)
            + baseName
            + "." + df.format(Calendar.getInstance().getTime());

        File f = new File(resultFile);
        while (f.exists()) {
            resultFile =
                    FOLDER
                    + (FOLDER.endsWith(File.separator) ? "" : File.separator)
                    + baseName
                    + "." + df.format(Calendar.getInstance().getTime());

            f = new File(resultFile);
        }

        resultFile += EXT;

        Tool.getInstance().logInfo("Result file " + resultFile + " created.");

        return resultFile;
    }

}

