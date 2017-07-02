package de.upb.cs.is.jpl.cli.command.loadlearningalgorithms;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.addlearningalgorithm.AddLearningAlgorithmFromJsonCommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.loadlearningalgorithms.LoadLearningAlgorithmsFailedException;


/**
 * This command is responsible for loading all learning algorithm defined in the given json file
 * into the {@link SystemConfiguration}.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
public class LoadLearningAlgorithmsCommand extends ACommand {

   private static final Logger logger = LoggerFactory.getLogger(LoadLearningAlgorithmsCommand.class);

   private static final String ERROR_ADDING_ALGORITHM_FAILED = "Adding the following algorithm failed: %s.";
   private static final String ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN = "Adding algorithm to the system configuration failed as of unknown reason.";
   private static final String ERROR_ADDED_LEARNING_ALGORITHM_FAILED = "Adding algorithm to the system configuration failed: {}.";
   private static final String INFO_ADDED_LEARNING_ALGORITHM = "Added algorithm {} to the system configuration.";
   private static final String ERROR_FAILED_TO_LOAD_ANY_ALGORITHM = "Failed to load any algorithm. ";

   private final JsonArray multipleLearningAlgorithmsAsJsonArray;
   private String failureReason = StringUtils.EMPTY_STRING;
   private CommandResult commandResult = null;


   /**
    * Constructs a {@link LoadLearningAlgorithmsCommand} instance and initializes it with the given
    * json array containing all necessary information of the algorithms to add.
    * 
    * @param multipleLearningAlgorithmsAsJsonArray the json array containing all necessary
    *           information of the algorithms to add
    */
   public LoadLearningAlgorithmsCommand(JsonArray multipleLearningAlgorithmsAsJsonArray) {
      super(ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier());
      this.multipleLearningAlgorithmsAsJsonArray = multipleLearningAlgorithmsAsJsonArray;
   }


   @Override
   public boolean canBeExecuted() {
      return true;
   }


   @Override
   public CommandResult executeCommand() {
      loadAlgorithmsOfJsonFile();
      return commandResult;
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   /**
    * Loads the algorithms defined in the JSON file given in the constructor of this class.
    */
   private void loadAlgorithmsOfJsonFile() {
      List<CommandResult> listOfCommandResults = new ArrayList<>();
      for (JsonElement algorithm : multipleLearningAlgorithmsAsJsonArray) {
         AddLearningAlgorithmFromJsonCommand addAlgorithmCommand = new AddLearningAlgorithmFromJsonCommand(algorithm.toString());
         if (addAlgorithmCommand.canBeExecuted()) {
            listOfCommandResults.add(addAlgorithmCommand.executeCommand());
         } else {
            listOfCommandResults.add(CommandResult.createFailureCommandResult(new LoadLearningAlgorithmsFailedException(
                  String.format(ERROR_ADDING_ALGORITHM_FAILED, addAlgorithmCommand.getFailureReason()))));
         }
      }

      commandResult = CommandResult.createSuccessCommandResult(listOfCommandResults);
      checkIfAllAlgorithmsFailedToLoad(listOfCommandResults);
   }


   /**
    * Checks if all algorithms of the json file failed to load. If so a
    * {@link LoadLearningAlgorithmsFailedException} will be added to the {@code commandResult} of
    * this instance.
    * 
    * @param listOfCommandResults the list of {@link CommandResult}'s generated for every single
    *           algorithm while executing this command
    */
   private void checkIfAllAlgorithmsFailedToLoad(List<CommandResult> listOfCommandResults) {
      int amountOfFailedCommands = 0;
      for (CommandResult commandResultOfList : listOfCommandResults) {
         if (!commandResultOfList.isExecutedSuccessfully()) {
            amountOfFailedCommands++;
            if (!commandResultOfList.isFailureReasonKnown()) {
               logger.info(ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN);
            } else {
               logger.info(ERROR_ADDED_LEARNING_ALGORITHM_FAILED, commandResultOfList.getException().getMessage());
            }
         } else {
            logger.info(INFO_ADDED_LEARNING_ALGORITHM, ((ILearningAlgorithm) commandResultOfList.getResult()).getClass().getName());
         }
      }
      if (amountOfFailedCommands == listOfCommandResults.size()) {
         commandResult.setException(new LoadLearningAlgorithmsFailedException(ERROR_FAILED_TO_LOAD_ANY_ALGORITHM));
      }
   }


   @Override
   public void undo() {
      SystemConfiguration.getSystemConfiguration().resetLearningAlgorithmsList();
   }

}
