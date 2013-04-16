/**
 *
 */
package fr.nikokode.textbatch;

import org.apache.log4j.Level;


/**
 * @author ngiraud
 *
 */
public class UnexpectedException extends ToolException {

    private static final long serialVersionUID = -759671085807827637L;

    /**
     *
     * @param unexpected
     * @param criticity
     */
    public UnexpectedException(Throwable unexpected, Level criticity) {
        super(
            "UNEXPECTED",
            "An unexpected exception of class {0} occured.",
            new String[] { unexpected.getClass().getName() },
            criticity);
    }

}
