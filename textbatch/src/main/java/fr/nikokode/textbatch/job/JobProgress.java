/**
 *
 */
package fr.nikokode.textbatch.job;

import fr.nikokode.textbatch.ToolException;


/**
 * @author ngiraud
 *
 */
public interface JobProgress {

    void notifyLineProcessed();

    int getLinesProcessed();
    int getLinesToProcess();

    double getCompletionPercentage();

    void logStatus();

    void notifyError(ToolException e);
    ToolException[] getErrors();

}
