/**
 *
 */
package fr.nikokode.textbatch.job;

import java.text.DecimalFormat;
import java.util.LinkedList;

import fr.nikokode.textbatch.Tool;
import fr.nikokode.textbatch.ToolException;


/**
 * @author ngiraud
 *
 */
public abstract class BasicJobProgress implements JobProgress {

	private final Integer linesToProcess;
    private Integer linesProcessed;

    private LinkedList<ToolException> errors = new LinkedList<ToolException>();

    protected static final DecimalFormat PERCENTAGE =
        new DecimalFormat("###.##");

    protected BasicJobProgress(int linesToProcess) {
        super();
        this.linesToProcess = new Integer(linesToProcess);
        this.linesProcessed = new Integer(0);
    }

    @Override
    public synchronized double getCompletionPercentage() {
        return (100 * linesProcessed.doubleValue())
        / linesToProcess.doubleValue();
    }

    public int getLinesToProcess() {
        return linesToProcess.intValue();
    }

    public int getLinesProcessed() {
        return linesProcessed.intValue();
    }

    public synchronized void notifyLineProcessed() {
        linesProcessed++;
    }

    @Override
    public void logStatus() {
    	Tool.getInstance().logInfo(linesProcessed + "/" + linesToProcess
        + " lines processed ("
        + PERCENTAGE.format(getCompletionPercentage()) + "%).");

    }

    @Override
    public ToolException[] getErrors() {
        return errors.toArray(new ToolException[errors.size()]);
    }

    @Override
    public synchronized void notifyError(ToolException e) {
        errors.add(e);
    }

}
