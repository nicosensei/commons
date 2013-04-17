/**
 *
 */
package fr.nikokode.textbatch.job;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.log4j.Level;

import fr.nikokode.textbatch.ToolException;


/**
 * @author ngiraud
 *
 */
public class IncorrectParametersException extends ToolException {

    private static final long serialVersionUID = -7443393639409230907L;

    /**
     *
     * @param expectedParams
     * @param actualParams
     */
    public IncorrectParametersException(
            String[] expectedParams,
            String[] actualParams) {
        super(
            "INCORRECT_PARAMETERS",
            generateMessageFormat(expectedParams, actualParams),
            generateMessageParams(expectedParams, actualParams),
            Level.FATAL);
    }

    private static String generateMessageFormat(
            String[] expectedParams,
            String[] actualParams) {

        String fmt = "Incorrect parameters provided:"
            + "\nExpected ";

        int index = 0;
        for (@SuppressWarnings("unused") String p : expectedParams) {
            fmt += "\n\t{" + index + "}";
            index++;
        }

        fmt += "\n but got";
        for (@SuppressWarnings("unused") String p : actualParams) {
            fmt += "\n\t{" + index + "}";
            index++;
        }

        return fmt;
    }

    private static String[] generateMessageParams(
            String[] expectedParams,
            String[] actualParams) {

        LinkedList<String> allParams = new LinkedList<String>();
        allParams.addAll(Arrays.asList(expectedParams));
        allParams.addAll(Arrays.asList(actualParams));

        return (String[]) allParams.toArray(new String[allParams.size()]);
    }

}
