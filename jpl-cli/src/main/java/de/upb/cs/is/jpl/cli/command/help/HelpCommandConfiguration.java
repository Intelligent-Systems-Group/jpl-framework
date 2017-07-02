package de.upb.cs.is.jpl.cli.command.help;


import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link HelpCommand}. The Parameters are defined for easy handling by JCommander.
 * 
 * @author Tanja Tornede
 *
 */
@Parameters(separators = "=", commandDescription = "Prints help messages for all commands and their parameters.")
public class HelpCommandConfiguration extends ACommandConfiguration {


   @Override
   public void resetFields() {
      // not needed as this configuration has no fields
   }


   @Override
   public ICommandConfiguration getCopy() {
      // we can return this, as this configuration has no fields
      return this;
   }
}
