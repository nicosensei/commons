/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import fr.nikokode.textbatch.ToolException;



/**
 * @author ngiraud
 *
 */
public abstract class ResultFileJob<L extends InputLine> extends Job<L> {

    private List<String> outputBuffer = new LinkedList<String>();

    /**
     * @param input
     * @param progress
     */
    protected ResultFileJob(
            AbstractInputFileReader<L> input, JobProgress progress) {
        super(input, progress);
    }

    @Override
    protected void sectionComplete() throws ToolException {

        if (outputBuffer.isEmpty()) {
            return;
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        for (String line : outputBuffer) {
            pw.println(line);
        }

        ((ResultFileJobProgress) getProgress()).logResult(sw.toString());
        pw.close();
        outputBuffer.clear();
    }

    protected void addResult(String res) {
        outputBuffer.add(res);
    }

    @Override
    protected abstract void processLine(L line) throws ToolException;

}
