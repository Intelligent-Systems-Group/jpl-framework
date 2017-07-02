package de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms;


import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link DetermineApplicableAlgorithmsCommand}. The Parameters are defined for easy handling by
 * JCommander.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
@Parameters(commandDescription = "Outputs all algorithms which can be used on the current datasets.")
public class DetermineApplicableAlgorithmsCommandConfiguration extends ACommandConfiguration {

   @Override
   public void resetFields() {
      // Nothing to do
   }


   @Override
   public ICommandConfiguration getCopy() {
      return this;
   }

}
