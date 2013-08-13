/**
 * 
 */
package fr.nikokode.commons.cmd;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author ngiraud
 *
 */
public class CommandOptions {
	
				
	/** The newline character as a string.*/
	private static final String NEWLINE = "\n";
	
	private static final String USAGE = "Usage:";
	
	private static final String DASH = "-";
	
	/** The space character as a string.*/
    private static final String SPACE = " ";

	/** Options object for parameters.*/
	private Options options = new Options();

	/** Parser for parsing the command line arguments.*/
	private CommandLineParser parser = new PosixParser();

	/**
	 * Parsing the input arguments.
	 * 
	 * @param args The input arguments.
	 * @return Whether it parsed correctly or not.
	 * @throws ParseException 
	 */
	public CommandLine parseParameters(String[] args) throws ParseException {
		return parser.parse(options, args);
	}

	/**
	 * Get the list of possible arguments with their description.
	 * 
	 * @return The list describing the possible arguments.
	 */
	public String usage(String commandName) {
		StringBuilder res = new StringBuilder();
		res.append(NEWLINE);
		res.append(USAGE);
		res.append(SPACE);
		res.append(commandName);
		res.append(NEWLINE);
		// add options
		for (Object o: options.getOptions()) {
			Option op = (Option) o;
			res.append(NEWLINE);
			res.append(DASH);
			res.append(op.getOpt());
			res.append(SPACE);
			res.append(op.getDescription());
		}
		return res.toString();
	}

	/**
	 * For retrieving the options.
	 * 
	 * @return The options.
	 */
	public Options getOptions() {
		return options;
	}
	
	public void addOption(CommandOption opt) {
		options.addOption(opt);
	}
	
}

