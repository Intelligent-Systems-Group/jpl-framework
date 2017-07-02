package de.upb.cs.is.jpl.cli.command.loadlearningalgorithms;


import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link LoadLearningAlgorithmsCommand}. The Parameters are defined for easy handling by
 * JCommander.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
@Parameters(commandDescription = "Loads and initializes all learning algorithms given by the user via the system configuration.")
public class LoadLearningAlgorithmsCommandConfiguration extends ACommandConfiguration {

   @Override
   public void resetFields() {
      // Nothing to do here.
   }


   @Override
   public ICommandConfiguration getCopy() {
      return this;
   }

}
