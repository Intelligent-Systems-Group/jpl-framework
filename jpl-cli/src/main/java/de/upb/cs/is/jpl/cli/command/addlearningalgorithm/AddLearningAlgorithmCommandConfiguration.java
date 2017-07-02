package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link AddLearningAlgorithmFromJsonCommand}. The parameters are defined for easy handling by
 * JCommander.
 *
 * @author Alexander Hetzer
 * @author Sebastian Gottschalk
 */
@Parameters(separators = "=", commandDescription = "Adds the given learning algorithm to the system configuration.")
public class AddLearningAlgorithmCommandConfiguration extends ACommandConfiguration {

   @Parameter(names = { "--name",
         "-n" }, description = "Specifies the identifier of the learning algorithm to be added to the system configuration.", required = true)
   private String learningAlgorithmName = StringUtils.EMPTY_STRING;

   @Parameter(names = { "--parameters",
         "-p" }, description = "Specifies the parameters of the learning algorithm, separated by a comma. E.G.: --parameters=test:1,test2:3")
   private String parameterDefinitions = StringUtils.EMPTY_STRING;


   @Override
   public void resetFields() {
      this.learningAlgorithmName = StringUtils.EMPTY_STRING;
      this.parameterDefinitions = StringUtils.EMPTY_STRING;

   }


   @Override
   public ICommandConfiguration getCopy() {
      AddLearningAlgorithmCommandConfiguration configuration = new AddLearningAlgorithmCommandConfiguration();
      configuration.learningAlgorithmName = this.learningAlgorithmName;
      configuration.parameterDefinitions = this.parameterDefinitions;

      return configuration;
   }


   /**
    * Checks if this command configuration contains parameter definitions for a learning algorithm.
    * 
    * @return {@code true}, if this command configuration contains parameter definitions,
    *         {@code false} if not
    */
   public boolean containsParameterDefinitions() {
      return !parameterDefinitions.isEmpty();
   }


   /**
    * Returns the name of the algorithm which should be added to the system configuration.
    * 
    * @return the name of the learning algorithm which should be added to the system configuration
    */
   public String getLearningAlgorithmName() {
      return learningAlgorithmName;
   }


   /**
    * Returns the parameters definitions string given by the user.
    * 
    * @return the parameter definitions string given by the user
    */
   public String getParameterDefinitions() {
      return parameterDefinitions;
   }

}
