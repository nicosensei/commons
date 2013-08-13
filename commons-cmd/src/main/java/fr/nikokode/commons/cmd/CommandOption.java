/**
 * 
 */
package fr.nikokode.commons.cmd;

import org.apache.commons.cli.Option;


/**
 * @author ngiraud
 *
 */
public class CommandOption extends Option {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1138008311854506717L;

	public CommandOption(
			String optSwitch, 
			boolean hasArgs,
			boolean required,
			String description) {
		super(optSwitch, hasArgs, description);;
		setRequired(required);
	}
	
}
