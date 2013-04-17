/**
 *
 */
package fr.nikokode.textbatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import fr.nikokode.textbatch.job.JobController;
import fr.nikokode.textbatch.job.ResultLogger;


/**
 *
 * Allows access to a tool basic functions.
 * @author ngiraud
 *
 */
public class Tool {

    private class ToolSettings extends Properties {

        private static final long serialVersionUID = 4321353542724244541L;

        private ToolSettings(String filePath) throws InvalidPropertiesFormatException, IOException {
            FileInputStream fis = new FileInputStream(filePath);
            loadFromXML(fis);
        }

    }

    private class ToolLogger {

        private static final String LOG_FOLDER = "Tool.log.folder";
        private static final String DFT_DATE_LAYOUT = "yyyy-MM-dd_HH:mm";
        private static final String DFT_LOG_PATTERN = "%m%n";

        public final String dateLayout;
//      public static final String TIME_LAYOUT = "HH:mm:ss:SSS";
//		public static final String LOG_PATTERN = "%d{" + TIME_LAYOUT
//				+ "} %-5p [%t]: %m%n";
        public final String logPattern;

        private String errorFile;
        private Logger errorLogger;
        private ResultLogger errorSummaryLogger;

        private String logFile;
        private Logger logger;

        private final File logFolder;

        private ToolLogger(
                ToolSettings settings,
                String name,
                String dateLayout,
                String logPattern) throws IOException {

            boolean defaultedDateLayout = false;
            if (dateLayout == null || dateLayout.isEmpty()) {
                this.dateLayout = DFT_DATE_LAYOUT;
                defaultedDateLayout = true;
            } else {
                this.dateLayout = dateLayout;
            }

            boolean defaultedLogPattern = false;
            if (logPattern == null || logPattern.isEmpty()) {
                this.logPattern = DFT_LOG_PATTERN;
                defaultedLogPattern = true;
            } else {
                this.logPattern = logPattern;
            }

            this.logFolder = new File(settings.getProperty(LOG_FOLDER));

            if (! this.logFolder.exists()) {
                this.logFolder.mkdir();
            } else if (! this.logFolder.isDirectory()
                    || ! this.logFolder.canWrite()) {
                System.err.println("Can't initialize log folder!");
                System.exit(-1);
            }

            Logger rootLogger = Logger.getRootLogger();
            if (! rootLogger.getAllAppenders().hasMoreElements()) {
                rootLogger.addAppender(new ConsoleAppender(
                        new PatternLayout(this.logPattern)));
            }
            rootLogger.setLevel(Level.ERROR);
            rootLogger.setAdditivity(false);

            logger = Logger.getLogger(name);
            logger.addAppender(
                    new ConsoleAppender(new PatternLayout(this.logPattern)));
            logFile =
                    logFolder.getAbsolutePath()
                        + File.separator + name
                        + "_" + new SimpleDateFormat(DFT_DATE_LAYOUT).format(new Date())
                        + ".out";
            logger.addAppender(
                    new FileAppender(new PatternLayout(this.logPattern), logFile));
            logger.setLevel(Level.DEBUG);
            logger.setAdditivity(false);
            logger.info("Log folder is: " + logFolder.getAbsolutePath());
            logger.info("Console output logged to: " + logFile);

            errorLogger = Logger.getLogger(name + ".err");
            errorFile = logFolder.getAbsolutePath() + File.separator
                    + name + ".err.log";

            errorLogger.addAppender(
                    new DailyRollingFileAppender(new PatternLayout(
                            this.logPattern), errorFile, "."
                            + this.dateLayout));
            errorLogger.setLevel(Level.ERROR);
            errorLogger.setAdditivity(false);

            if (defaultedDateLayout) {
                logger.warn("Rolling log date pattern set to default.");
            }
            if (defaultedLogPattern) {
                logger.warn("Log entry pattern set to default.");
            }
        }

        public void setErrorSummaryLogger(ResultLogger logger) {
            this.errorSummaryLogger = logger;
        }

    }

    private static Tool instance;

    private ToolSettings settings;
    private ToolLogger logger;
    private List<File> filesToClean = new ArrayList<File>();

    private Tool(String settingsFilePath, String name)
    throws InvalidPropertiesFormatException, IOException {
        settings = new ToolSettings(settingsFilePath);
        logger = new ToolLogger(
                settings,
                name,
                getProperty(Tool.class, "log.dateLayout"),
                getProperty(Tool.class, "log.pattern"));
    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Missing mandatory arguments:");
            System.out.println("\t0: <settings file path>");
            System.out.println("\t1: <job controller class>");
            System.exit(0);
        }

        String settingsFile = args[0];
        String jobCtrlClass = args[1];

        long initTime = System.currentTimeMillis();
        try {
            Tool.init(settingsFile, jobCtrlClass);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.err.println("Failed to initialize tool!");
            System.exit(-1);
        }

        try {
            Class<?> jcClass = Class.forName(jobCtrlClass);
            JobController<?, ?> jc =
                (JobController<?, ?>) jcClass.newInstance();

            jc.initialize(subArgs(args, 2));
            jc.launch();

        } catch (ToolException e) {
            if (Level.FATAL.equals(e.getCriticity())) {
                Tool.getInstance().logInfo("Fatal error encountered!");
                System.exit(-1);
            }
        } catch (Exception e) {
            instance.logInfo("Could not instanciate class "+ jobCtrlClass
                    + "!");
            instance.logError(e);
            System.exit(-1);
        } finally {
            cleanup(initTime);
        }

    }


    public static void init(String settingsFilePath, String name)
    throws InvalidPropertiesFormatException, IOException {
        if (instance == null) {
            instance = new Tool(settingsFilePath, name);
        }
    }

    public static Tool getInstance() {
        return instance;
    }

    public void registerFileForCleanup(File f) {
        filesToClean.add(f);
    }

    public void registerFileForCleanup(String f) {
        filesToClean.add(new File(f));
    }

    public synchronized void logDebug(String msg) {
        logger.logger.debug(msg);
    }

    public synchronized void logInfo(String msg) {
        logger.logger.info(msg);
    }

    public synchronized void logError(Throwable t) {
        String msg = t.getLocalizedMessage();
        logger.logger.error(msg, t);
        logger.errorLogger.error(msg, t);

        if (logger.errorSummaryLogger != null) {
            if (t instanceof ToolException) {
                logger.errorSummaryLogger.logResult(
                        ((ToolException) t).getRootCause()
                        .getLocalizedMessage());
            }
        }
    }

    public synchronized void logError(BaseException e) {
        String msg = e.getLocalizedMessage();
        if (e.isVerbose()) {
            logger.logger.error(msg, e);
        }
        logger.errorLogger.error(msg, e);

        if (logger.errorSummaryLogger != null) {
            if (e instanceof ToolException) {
                logger.errorSummaryLogger.logResult(
                        ((ToolException) e).getRootCause()
                        .getLocalizedMessage());
            }
        }
    }

    public File getLogFolder() {
        return logger.logFolder;
    }

    public String getProperty(Class<?> owner, String key) {
        return settings.getProperty(getPropertyKey(owner, key));
    }

    public int getIntProperty(Class<?> owner, String key) {
        return Integer.parseInt(settings.getProperty(getPropertyKey(owner, key)));
    }

    public int getIntProperty(Class<?> owner, String key, int defaultValue) {
        String value = settings.getProperty(getPropertyKey(owner, key));
        return (value == null ? defaultValue : Integer.parseInt(value));
    }

    public long getLongProperty(Class<?> owner, String key) {
        return Long.parseLong(settings.getProperty(getPropertyKey(owner, key)));
    }

    public long getLongProperty(Class<?> owner, String key, long defaultValue) {
        String value = settings.getProperty(getPropertyKey(owner, key));
        return (value == null ? defaultValue : Long.parseLong(value));
    }

    public boolean getBoolProperty(Class<?> owner, String key) {
        return Boolean.parseBoolean(
                settings.getProperty(getPropertyKey(owner, key)));
    }

    public boolean getBoolProperty(
            Class<?> owner, String key, boolean defaultValue) {
        String value = settings.getProperty(getPropertyKey(owner, key));
        return (value == null ? defaultValue : Boolean.parseBoolean(value));
    }

    public static String formatDuration(long lMs) {
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

    public static void cleanup(long initTime) {
        // Register error log for cleanup if it's empty
        File errorLog = new File(instance.logger.errorFile);
        if (errorLog.exists() && (errorLog.length() == 0)) {
            instance.registerFileForCleanup(errorLog);
        }

        // Clean registered files
        for (File f : instance.filesToClean) {
            deleteFileRec(f);
        }

        long timeElapsed = System.currentTimeMillis() - initTime;
        instance.logInfo("Time elapsed: " + formatDuration(timeElapsed));
        LogManager.shutdown();
    }

    public void setErrorSummaryLogger(ResultLogger logger) {
        this.logger.setErrorSummaryLogger(logger);
    }

    private static String[] subArgs(String[] args, int beginIndex) {
        LinkedList<String> subArgs =
            new LinkedList<String>(Arrays.asList(args));

        for (int i = 0; i < beginIndex; i++) {
            subArgs.removeFirst();
        }

        return (String[]) subArgs.toArray(new String[subArgs.size()]);
    }

    private String getPropertyKey(Class<?> owner, String name) {
        return owner.getName().substring(
                Tool.class.getPackage().getName().length() + 1) + "." + name;
    }

    private static void deleteFileRec(File f) {
        if (f.isDirectory()) {
            for (File child : f.listFiles()) {
                deleteFileRec(child);
            }
        }
        if (!f.delete()) {
            f.deleteOnExit();
        }
    }

}
