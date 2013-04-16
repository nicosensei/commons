/**
 *
 */
package fr.nikokode.textbatch.job;

import org.apache.log4j.Level;

import fr.nikokode.textbatch.Tool;
import fr.nikokode.textbatch.ToolException;


/**
 * @author ngiraud
 *
 */
public abstract class Job<L extends InputLine> extends Thread {


    private InputFileReader<L> input;
    private JobProgress progress;

    protected Tool tool;

    private boolean alive = true;

    protected Job(InputFileReader<L> input, JobProgress progress) {
        this.input = input;
        this.progress = progress;
        this.tool = Tool.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        while (alive) {

            // Read a section from the input file
            InputFileSection<L> section = null;
            try {
                section = input.readSection();
            } catch (InputFileException e) {
                tool.logError(e);
                break;
            }

            try {
                // Process it
                for (L line : section.getLines()) {
                    processLine(line);
                    progress.notifyLineProcessed();
                }

                sectionComplete();
            } catch (ToolException e) {
                handleToolException(e);
            }

            // Stop if there's no more input available
            if (section.noMoreInput()) {
                break;
            }
        }
        if (alive) {
            try {
                jobComplete();
            } catch (ToolException e) {
                handleToolException(e);
            }
        }
    }

    public JobProgress getProgress() {
        return progress;
    }

    protected abstract void processLine(L line) throws ToolException;
    protected abstract void sectionComplete() throws ToolException;
    protected abstract void jobComplete() throws ToolException;

    protected void handleToolException(ToolException e) {
        progress.notifyError(e);
        if (Level.FATAL.equals(e.getCriticity())) {
            Tool.getInstance().logInfo(
                    "Fatal error "
                    + e.getClass().getSimpleName());
            this.alive = false;
        }
    }

}
