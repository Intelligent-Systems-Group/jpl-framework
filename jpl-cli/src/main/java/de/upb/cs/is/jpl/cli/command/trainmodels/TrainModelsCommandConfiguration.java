package de.upb.cs.is.jpl.cli.command.trainmodels;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This class encapsulates all necessary and/or possible combination of parameters for
 * {@link TrainModelsCommand}. The parameters are defined for easy handling by {@link JCommander}.
 * Currently it is not use for this command as it is not required but it should exist to retain the
 * consistency in command pattern.
 *
 * @author Sebastian Gottschalk
 *
 */
@Parameters(commandDescription = "Trains all selected algorithms on the current datasets.")
public class TrainModelsCommandConfiguration extends ACommandConfiguration {

   @Override
   public void resetFields() {
      // Not used
   }


   @Override
   public ICommandConfiguration getCopy() {
      // Not used
      return this;
   }

}
