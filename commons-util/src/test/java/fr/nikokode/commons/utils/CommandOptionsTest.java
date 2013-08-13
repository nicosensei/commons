/**
 * 
 */
package fr.nikokode.commons.utils;

import junit.framework.TestCase;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import fr.nikokode.commons.cmd.CommandOption;
import fr.nikokode.commons.cmd.CommandOptions;

/**
 * @author ngiraud
 *
 */
public class CommandOptionsTest extends TestCase {

	@Test
	public final void testOptionTypes() throws ParseException {

		// Test an optional flag
		CommandOptions cmdOpts = new CommandOptions();
		cmdOpts.addOption(new CommandOption("t", false, false, "Optional flag"));
		CommandLine cl = cmdOpts.parseParameters(new String[] { "-t" });
		assertTrue(cl.hasOption("t"));
		cl = cmdOpts.parseParameters(new String[0]);
		assertFalse(cl.hasOption("t"));
		cl = cmdOpts.parseParameters(new String[] { "p" });
		assertFalse(cl.hasOption("t"));

		// Test a required flag
		cmdOpts = new CommandOptions();
		cmdOpts.addOption(new CommandOption("t", false, true, "Required flag"));
		cl = cmdOpts.parseParameters(new String[] { "-t" });
		assertTrue(cl.hasOption("t"));
		try {
			cmdOpts.parseParameters(new String[0]);
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
			
		}

		// Test a required option
		cmdOpts = new CommandOptions();
		cmdOpts.addOption(new CommandOption("t", true, true, "Required parameter"));
		cl = cmdOpts.parseParameters(new String[] { "-t 10" });
		assertEquals("10", cl.getOptionValue("t").trim());
		cl = cmdOpts.parseParameters(new String[] { "-t blah" });
		assertEquals("blah", cl.getOptionValue("t").trim());
		try {
			cmdOpts.parseParameters(new String[] { "-t" });
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
			
		}
		
		try {
			cmdOpts.parseParameters(new String[0]);
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
			
		}
		
		try {
			cmdOpts.parseParameters(new String[] { "p" });
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
			
		}

		// Test an optional option
		cmdOpts = new CommandOptions();
		cmdOpts.addOption(new CommandOption("t", true, false, "Optional parameter"));
		cmdOpts.addOption(new CommandOption("p", true, false, "Another parameter"));
		cl = cmdOpts.parseParameters(new String[] { "-t 10" });
		assertEquals("10", cl.getOptionValue("t").trim());
		cl = cmdOpts.parseParameters(new String[] { "-t blah" });
		assertEquals("blah", cl.getOptionValue("t").trim());
		cl = cmdOpts.parseParameters(new String[0]);
		assertNull(cl.getOptionValue("t"));
		cl = cmdOpts.parseParameters(new String[] { "-p 10" });
		assertNull(cl.getOptionValue("t"));

	}

}
