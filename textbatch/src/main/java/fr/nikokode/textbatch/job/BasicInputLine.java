/**
 *
 */
package fr.nikokode.textbatch.job;

/**
 * Base abstract class for input lines.
 * @author ngiraud
 *
 */
public abstract class BasicInputLine implements InputLine {

    public static final String DEFAULT_SEP = "\\s+";

    private final String fieldSeparator;
    private final String[] fields;

    protected BasicInputLine(String line, String fieldSeparator) {
    	if (fieldSeparator.equals(DEFAULT_SEP)
    			|| fieldSeparator.matches(DEFAULT_SEP)) {
    		this.fieldSeparator = " ";
    	} else {
    		this.fieldSeparator = fieldSeparator;
    	}
        this.fields = line.split(fieldSeparator);
    }

    @Override
    public String[] getFields() {
        return this.fields;
    }

    @Override
    public String getLine() {
        String l = "";
        for (String f : this.fields) {
            l += f + fieldSeparator;
        }
        return l.substring(0, l.lastIndexOf(fieldSeparator));
    }

    @Override
    public String getSeparator() {
        return this.fieldSeparator;
    }

}
