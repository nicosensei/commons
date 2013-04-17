/**
 *
 */
package fr.nikokode.textbatch.job;

import fr.nikokode.textbatch.job.BasicInputLine;
import junit.framework.TestCase;

/**
 * @author ngiraud
 *
 */
public class BasicInputLineTest extends TestCase {

	private static class TestInputLine extends BasicInputLine {
		protected TestInputLine(String line, String fieldSeparator) {
			super(line, fieldSeparator);
		}
	}

	public static final void testSeparatorDisplay() {

		BasicInputLine l = new TestInputLine("blah blah", " ");
		assertEquals(" ", l.getSeparator());
		assertEquals("blah blah", l.getLine());

		l = new TestInputLine("blah     blah", "\\s+");
		assertEquals(" ", l.getSeparator());
		assertEquals("blah blah", l.getLine());

		l = new TestInputLine("blah\tblah", "\t");
		assertEquals(" ", l.getSeparator());
		assertEquals("blah blah", l.getLine());
	}

}
