package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.exception.JplException;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.addlearningalgorithm.UnknownLearningAlgorithmIdentifierException;


/**
 * This command is responsible for adding learning algorithms defined by a json string to the system
 * configuration. It parses the given json string, creates the according learning algorithm with the
 * defined configuration and adds it to the system configuration. If any part of this process fails,
 * it will return an according failure command result.
 * 
 * @author Alexander Hetzer
 * @author Sebastian Gottschalk
 */
public class AddLearningAlgorithmFromJsonCommand extends ACommand {

   private static final Logger logger = LoggerFactory.getLogger(AddLearningAlgorithmFromJsonCommand.class);

   private static final String COMMAND_IDENTIFIER_ADD_LEARNING_ALGORITHM_FROM_JSON_COMMAND = "add_learning_algorithm_from_json_command";

   private static final String ALGORITHM_PARAMETER_IDENTIFIER = "parameters";
   private static final String ALGORITHM_NAME_IDENTIFIER = "name";

   private static final String ERROR_UNKNOWN_LEARNING_ALGORITHM_IDENTIFIER = "Unknown learning algorithm identifier: %s";
   private static final String ERROR_UNKNOWN_PROBLEM_OCCURRED = "Unknown problem occured while adding the algorithm %s to the configuration.";
   private static final String ERROR_CANNOT_PARSE_EMPTY_JSON_STRING = "Cannot parse an empty JSON algorithm string.";
   private static final String ERROR_CANNOT_PARSE_NULL_JSON_STRING = "Cannot parse an 'null' JSON algorithm string.";
   private static final String ERROR_COULD_NOT_PARSE_JSON = "Failed to parse JSON algorithm string.";
   private static final String ERROR_COULD_NOT_PARSE_JSON_AS_OF_ERROR = "Failed to parse JSON algorithm configuration due to the following error: %s";
   private static final String ERROR_PARAMETER_PROBLEM = "The following parameter error occurred: ";
   private static final String ERROR_NO_ALGORITHM_SPECIFIED = "No learning algorithm has been supplied.";

   private static final String SUCCESS_ADDED_LEARNING_ALGORITHM = "Added learning algorithm %s to the system configuration.";

   private String learningAlgorithmAsJsonString;
   private JsonObject learningAlgorithmAsJsonObject;
   private ILearningAlgorithm learningAlgorithm;

   private Exception caughtException = null;
   private String failureReason = StringUtils.EMPTY_STRING;


   /**
    * Creates a new {@link AddLearningAlgorithmCommand}, which can be used to add the algorithm
    * specified in the given json string to the system configuration.
    *
    * @param learningAlgorithmAsJsonString the learning algorithm, which should be added to the
    *           system configuration, as a json string
    */
   public AddLearningAlgorithmFromJsonCommand(String learningAlgorithmAsJsonString) {
      super(COMMAND_IDENTIFIER_ADD_LEARNING_ALGORITHM_FROM_JSON_COMMAND);
      this.learningAlgorithmAsJsonString = learningAlgorithmAsJsonString;
   }


   @Override
   public boolean canBeExecuted() {
      if (isAlgorithmConfigurationJsonStringNull()) {
         failureReason = ERROR_COULD_NOT_PARSE_JSON;
         return false;
      }
      return isAlgorithmConfigurationJsonStringValid();
   }


   /**
    * Checks whether the json algorithm configuration string is valid json.
    * 
    * @return {@code true} if the json algorithm configuration string is valid json, otherwise
    *         {@code false}
    * 
    */
   private boolean isAlgorithmConfigurationJsonStringValid() {
      try {
         parseAlgorithmConfigurationJsonStringIntoObject();
         return true;
      } catch (JsonParsingFailedException jsonParsingFailedException) {
         failureReason = String.format(ERROR_COULD_NOT_PARSE_JSON_AS_OF_ERROR, jsonParsingFailedException.getMessage());
         logger.error(failureReason, jsonParsingFailedException);
         return false;
      }
   }


   /**
    * Checks whether the json algorithm configuration string is {@code null} or empty.
    * 
    * @return {@code true}, if the json algorithm configuration string is {@code null} or empty,
    *         otherwise {@code false}
    */
   private boolean isAlgorithmConfigurationJsonStringNull() {
      if (learningAlgorithmAsJsonString == null) {
         failureReason = ERROR_CANNOT_PARSE_NULL_JSON_STRING;
         return true;
      }
      if (learningAlgorithmAsJsonString.isEmpty()) {
         failureReason = ERROR_CANNOT_PARSE_EMPTY_JSON_STRING;
         return true;
      }
      return false;
   }


   @Override
   public CommandResult executeCommand() {
      initializeLearningAlgorithmFromJsonStringAndAddItToSystemConfiguration();
      return createCommandResultBasedOnCommandExecutionResults();
   }


   /**
    * Creates the command result according to the results of the command execution.
    * 
    * @return the command result filled with information according to the results of the command
    *         execution
    */
   private CommandResult createCommandResultBasedOnCommandExecutionResults() {
      if (hasErrorOccuredDuringCommandExecution()) {
         if (hasCaughtException()) {
            return CommandResult.createFailureCommandResult(caughtException);
         }
         JplException unknownProblemOccuredException = new JplException(
               String.format(ERROR_UNKNOWN_PROBLEM_OCCURRED, learningAlgorithmAsJsonString));
         return CommandResult.createFailureCommandResult(unknownProblemOccuredException);
      }
      return CommandResult.createSuccessCommandResult(learningAlgorithm);
   }


   /**
    * Checks if an error has occurred during the command execution.
    * 
    * @return {@code true}, if an error occurred during the command execution, otherwise
    *         {@code false}
    */
   private boolean hasErrorOccuredDuringCommandExecution() {
      return learningAlgorithm == null || caughtException != null;
   }


   /**
    * Checks if an exception has been caught during the command execution.
    * 
    * @return {@code true}, if an exception occurred during the command execution, otherwise
    *         {@code false}
    */
   private boolean hasCaughtException() {
      return caughtException != null;
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   /**
    * Tries to initialize the learning algorithm specified by the local
    * {@link #learningAlgorithmAsJsonString} and adds it to the system configuration, if successful.
    */
   private void initializeLearningAlgorithmFromJsonStringAndAddItToSystemConfiguration() {

      try {
         parseAlgorithmConfigurationJsonStringIntoObject();
         createLearningAlgorithmFromJsonConfiguration();
         setLearningAlgorithmParameters();
         logAlgorithmToSystemConfigurationAddition(learningAlgorithm.toString());
         SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();
         systemConfiguration.addLearningAlgorithm(learningAlgorithm);
      } catch (UnknownLearningAlgorithmIdentifierException unknownLearningAlgorithmIdentifierException) {
         caughtException = new ParameterValidationFailedException(unknownLearningAlgorithmIdentifierException.getMessage());
         logger.error(unknownLearningAlgorithmIdentifierException.getMessage(), unknownLearningAlgorithmIdentifierException);
      } catch (ParameterValidationFailedException parameterValidationFailedException) {
         caughtException = parameterValidationFailedException;
         logger.error(ERROR_PARAMETER_PROBLEM, parameterValidationFailedException);
      } catch (JsonParsingFailedException jsonParsingFailedException) {
         caughtException = jsonParsingFailedException;
         logger.error(ERROR_COULD_NOT_PARSE_JSON, jsonParsingFailedException);
      } catch (NullPointerException nullPointerException) {
         caughtException = new JsonParsingFailedException(ERROR_NO_ALGORITHM_SPECIFIED);
         logger.error(ERROR_NO_ALGORITHM_SPECIFIED, nullPointerException);
      } catch (ClassCastException classCastException) {
         caughtException = new JsonParsingFailedException(
               String.format(ERROR_COULD_NOT_PARSE_JSON_AS_OF_ERROR, classCastException.getMessage()));
         logger.error(caughtException.getMessage(), classCastException);
      }

   }


   /**
    * Sets the parameters of the learning algorithm stored inside {@link #learningAlgorithm} based
    * on the local {@link #learningAlgorithmAsJsonObject}.
    * 
    * @throws ParameterValidationFailedException if the validation of the parameters failed
    * @throws ClassCastException if the value of the key {@link #ALGORITHM_PARAMETER_IDENTIFIER} is
    *            not of the type {@link JsonObject} in the {@link #learningAlgorithmAsJsonObject}
    */
   private void setLearningAlgorithmParameters() throws ParameterValidationFailedException {
      JsonObject parametersJsonObject = learningAlgorithmAsJsonObject.getAsJsonObject(ALGORITHM_PARAMETER_IDENTIFIER);
      if (parametersJsonObject != null) {
         learningAlgorithm.setParameters(parametersJsonObject);
      }
   }


   /**
    * Creates the learning algorithm specified by the local {@link #learningAlgorithmAsJsonObject}
    * and stores it inside the {@link #learningAlgorithm} variable.
    * 
    * @throws NullPointerException if the json object does not have an attribute
    *            {@link #ALGORITHM_NAME_IDENTIFIER}.
    * @throws UnknownLearningAlgorithmIdentifierException if the learning algorithm identifier
    *            extracted from the learning algorithm json object is unknown
    */
   private void createLearningAlgorithmFromJsonConfiguration() throws UnknownLearningAlgorithmIdentifierException {
      String learningAlgorithmIdentifier = learningAlgorithmAsJsonObject.get(ALGORITHM_NAME_IDENTIFIER).getAsString();
      ELearningAlgorithm eLearningAlgorithm = ELearningAlgorithm.getELearningAlgorithmByIdentifier(learningAlgorithmIdentifier);
      if (eLearningAlgorithm == null) {
         throw new UnknownLearningAlgorithmIdentifierException(
               String.format(ERROR_UNKNOWN_LEARNING_ALGORITHM_IDENTIFIER, learningAlgorithmIdentifier));
      }
      learningAlgorithm = eLearningAlgorithm.createLearningAlgorithm();
   }


   /**
    * Tries to parse the local {@link #learningAlgorithmAsJsonString} into a json object.
    * 
    * @throws JsonParsingFailedException if the parsing process throws any exception
    */
   private void parseAlgorithmConfigurationJsonStringIntoObject() throws JsonParsingFailedException {
      learningAlgorithmAsJsonObject = JsonUtils.createJsonObjectFromString(learningAlgorithmAsJsonString);
   }


   /**
    * Logs that the algorithm with the given name was added to the system configuration.
    * 
    * @param algorithmName the name of the algorithm which was added to the system configuration
    * 
    */
   private void logAlgorithmToSystemConfigurationAddition(String algorithmName) {
      logger.info(String.format(SUCCESS_ADDED_LEARNING_ALGORITHM, algorithmName));
   }


   @Override
   public void undo() {
      SystemConfiguration.getSystemConfiguration().resetLearningAlgorithmsList();

   }

}
