/**
 *
 */
package fr.nikokode.textbatch.job;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.nikokode.textbatch.Tool;
import fr.nikokode.textbatch.ToolException;



/**
 * @author ngiraud
 *
 */
public abstract class JobController<L extends InputLine, J extends Job<L> > {

    private class ProgressDisplayer implements Runnable {

        private final JobProgress progress;

        public ProgressDisplayer(JobProgress progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            progress.logStatus();
        }

    }

    private InputFileReader<L> inputFile;
    private JobProgress progress;

    private List<J> jobs = new LinkedList<J>();

    public final void initialize(String[] args) throws ToolException {
        init(args);
        this.inputFile = inputFileReaderFactory();
        this.progress = jobProgressFactory();
    }

    public void launch() throws ToolException {

        int threadCount = getThreadCount();
        Tool tool = Tool.getInstance();

        for (int i = 0; i < threadCount; i++) {
            J job = jobFactory();
            jobs.add(job);
            job.start();
        }

        tool.logInfo("Started " + threadCount + " worker thread"
                + (threadCount > 1 ? "s." : "."));

        int controlLoopSleepTime = Tool.getInstance().getIntProperty(
                JobController.class, "sleepInSeconds", 1);

        // Launch progress display thread
        int delay = Tool.getInstance().getIntProperty(
                JobProgress.class, "delayInSeconds");
        ScheduledThreadPoolExecutor progressDisplay =
            new ScheduledThreadPoolExecutor(1);
        progressDisplay.scheduleAtFixedRate(
                new ProgressDisplayer(progress),
                0, delay, TimeUnit.SECONDS);

        while (true) {
            boolean allDone = true;
            for (Job<L> j : jobs) {
                allDone &= ! j.isAlive();
            }
            if (allDone) {
                break;
            }

            try {
                Thread.sleep(controlLoopSleepTime);
            } catch (InterruptedException e) {
            }

        }

        progressDisplay.shutdown();
        this.inputFile.close();

        progress.logStatus();
        onComplete();

    }

    public InputFileReader<L> getInputFile() {
        return inputFile;
    }

    protected abstract J jobFactory() throws ToolException;

    protected abstract JobProgress jobProgressFactory() throws ToolException;

    protected abstract InputFileReader<L> inputFileReaderFactory()
    throws InputFileException;

    protected abstract void init(String[] args) throws ToolException;

    protected abstract void onComplete() throws ToolException;

    public JobProgress getProgress() {
        return progress;
    }

    protected int getThreadCount() {
        return Integer.parseInt(
                Tool.getInstance().getProperty(getClass(), "threadCount"));
    }

    protected int getSectionSize() {
        return Integer.parseInt(
                Tool.getInstance().getProperty(getClass(), "sectionSize"));
    }

}
