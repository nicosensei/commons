/**
 *
 */
package fr.nikokode.textbatch.job;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ngiraud
 *
 */
public class InputFileSection<L extends InputLine> {

    private boolean noMoreInput = false;
    private final List<L> lines;

    public InputFileSection(List<L> lines, boolean noMoreInput) {
        this.lines = new LinkedList<L>(lines);
        this.noMoreInput = noMoreInput;
    }

    public boolean noMoreInput() {
        return noMoreInput;
    }

    public List<L> getLines() {
        return lines;
    }

}
