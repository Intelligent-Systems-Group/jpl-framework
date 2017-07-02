package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.exception.command.addlearningalgorithm.ParameterParsingFailedException;


/**
 * This commands adds a learning algorithm given via the command line to the system configuration.
 * Therefore it transforms the input into an json object and calls the
 * {@link AddLearningAlgorithmFromJsonCommand}.
 * 
 * @author Alexander Hetzer
 *
 */
public class AddLearningAlgorithmCommand extends ACommand {

   private static final String PARAMETER_DEFINITION_SEPARATOR = ",";

   private static final String LEARNING_ALGORITHM_NAME_IDENTIFIER = "name";
   private static final String LEARNING_ALGORITHM_PARAMETER_IDENTIFIER = "parameters";

   private static final Logger logger = LoggerFactory.getLogger(AddLearningAlgorithmCommand.class);

   private AddLearningAlgorithmCommandConfiguration commandConfiguration;
   private AddLearningAlgorithmFromJsonCommand baseAddLearningAlgorithmCommand;
   private String failureReason = StringUtils.EMPTY_STRING;


   /**
    * Creates an {@link AddLearningAlgorithmCommand} based on the given command configuration.
    * 
    * @param commandConfiguration the command configuration this command should be based on
    */
   public AddLearningAlgorithmCommand(AddLearningAlgorithmCommandConfiguration commandConfiguration) {
      super(ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier());
      this.commandConfiguration = commandConfiguration;
   }


   @Override
   public boolean canBeExecuted() {
      try {
         JsonObject learningAlgorithmAsJsonObjectFromCommandLine = createLearningAlgorithmAsJsonObjectFromCommandLineInput();
         AddLearningAlgorithmFromJsonCommand addLearningAlgorithmFromJsonCommand = new AddLearningAlgorithmFromJsonCommand(
               learningAlgorithmAsJsonObjectFromCommandLine.toString());
         boolean canBeExecuted = addLearningAlgorithmFromJsonCommand.canBeExecuted();
         failureReason = addLearningAlgorithmFromJsonCommand.getFailureReason();
         return canBeExecuted;
      } catch (ParameterParsingFailedException parameterParsingFailedException) {
         failureReason = parameterParsingFailedException.getMessage();
         logger.error(failureReason, parameterParsingFailedException);
      }
      return false;
   }


   @Override
   public CommandResult executeCommand() {
      JsonObject learningAlgorithmAsJsonObjectFromCommandLine;
      try {
         learningAlgorithmAsJsonObjectFromCommandLine = createLearningAlgorithmAsJsonObjectFromCommandLineInput();
         baseAddLearningAlgorithmCommand = new AddLearningAlgorithmFromJsonCommand(learningAlgorithmAsJsonObjectFromCommandLine.toString());
         return baseAddLearningAlgorithmCommand.executeCommand();
      } catch (ParameterParsingFailedException parameterParsingFailedException) {
         return CommandResult.createFailureCommandResult(parameterParsingFailedException);
      }

   }


   /**
    * Creates the json representation of the learning algorithm specified by the command line input
    * of the user stored inside the command configuration.
    * 
    * @return a json representation of the learning algorithm specified by the command line input
    *         stored inside the command configuration
    * 
    * @throws ParameterParsingFailedException if any of the learning algorithm parameters given by
    *            the user cannot be parsed correctly
    */
   private JsonObject createLearningAlgorithmAsJsonObjectFromCommandLineInput() throws ParameterParsingFailedException {
      JsonObject learningAlgorithmJsonObject = new JsonObject();

      learningAlgorithmJsonObject.addProperty(LEARNING_ALGORITHM_NAME_IDENTIFIER, commandConfiguration.getLearningAlgorithmName());
      if (commandConfiguration.containsParameterDefinitions()) {
         JsonObject parametersJsonObject = createLearningAlgorithmParametersAsJsonObject();
         learningAlgorithmJsonObject.add(LEARNING_ALGORITHM_PARAMETER_IDENTIFIER, parametersJsonObject);
      }
      return learningAlgorithmJsonObject;
   }


   /**
    * Creates the json representation of the learning algorithm parameters specified by the command
    * line input of the user stored inside the command configuration.
    * 
    * @return a json representation of the learning algorithm parameters specified by the command
    *         line input stored inside the command configuration
    * 
    * @throws ParameterParsingFailedException if any of the learning algorithm parameters given by
    *            the user cannot be parsed correctly
    */
   private JsonObject createLearningAlgorithmParametersAsJsonObject() throws ParameterParsingFailedException {
      JsonObject parametersJsonObject = new JsonObject();

      List<String> separatedParameterDefinitions = seperateParameterDefinitions();

      for (String parameterDefinition : separatedParameterDefinitions) {
         Parameter parameter = Parameter.createParameterFromParameterDefinitionString(parameterDefinition);
         parametersJsonObject.addProperty(parameter.getParameterName(), parameter.getParameterValue());
      }
      return parametersJsonObject;
   }


   /**
    * Separates the parameter definitions given in the command configuration and returns a list in
    * which each element represents a single parameter definition.
    * 
    * @return a list containing one element per parameter definition
    */
   private List<String> seperateParameterDefinitions() {
      String parameterDefinitions = commandConfiguration.getParameterDefinitions().trim();
      List<String> seperatedParameterDefinitions = new ArrayList<>();
      Collections.addAll(seperatedParameterDefinitions, parameterDefinitions.split(PARAMETER_DEFINITION_SEPARATOR));
      return seperatedParameterDefinitions;
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      baseAddLearningAlgorithmCommand.undo();
   }

}
