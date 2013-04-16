/**
 *
 */
package fr.nikokode.textbatch.job;

import java.io.File;

import fr.nikokode.textbatch.Tool;
import fr.nikokode.textbatch.ToolException;


/**
 * @author ngiraud
 *
 */
public abstract class ResultFileJobController<
L extends InputLine,
J extends ResultFileJob<L>>
extends JobController<L, J> {

    @Override
    public ResultFileJobProgress getProgress() {
        return (ResultFileJobProgress) super.getProgress();
    }

    @Override
    protected void onComplete() throws ToolException {
        File resultFile = new File(getProgress().getResultFilePath());
        if (resultFile.exists() && resultFile.length() == 0) {
            Tool.getInstance().registerFileForCleanup(resultFile);
        }

    }



}
