package de.upb.cs.is.jpl.cli.command.loadlearningalgorithms;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * This command Handler is responsible for handling given
 * {@link LoadLearningAlgorithmsCommandConfiguration} and creating an
 * {@link LoadLearningAlgorithmsCommand}. It holds the according command configuration before it is
 * passed to the command. Additionally it is responsible for interpreting the {@link CommandResult}
 * given by the command at the end of execution.
 *
 * @author Tanja Tornede
 * @author Sebastian Gottschalk
 */
public class LoadLearningAlgorithmsCommandHandler extends ACommandHandler {

   private static final String ADDED_ALGORITHM = "Added algorithm %s successfully.";
   private static final String AMOUNT_OF_FAILED_ALGORITHMS = "Adding of %s algorithm(s) failed:";
   private static final String ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN = "Adding algorithm to the system configuration failed as of unknown reason.";
   private static final String ERROR_ADDED_LEARNING_ALGORITHM_FAILED = "Adding algorithm to the system configuration failed: {}.";


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      return new LoadLearningAlgorithmsCommand(SystemConfiguration.getSystemConfiguration().getLearningAlgorithmsConfigurationJsonArray());
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      return getOutput(commandResult);
   }


   /**
    * Returns the output string after interpreting the given {@code commandResult} for
    * {@link LoadLearningAlgorithmsCommand}.
    * 
    * @param commandResult the {@link CommandResult} which will be interpreted
    * @return the interpreted output string for the given {@code commandResult}
    */
   private String getOutput(final CommandResult commandResult) {
      if (commandResult.isExecutedSuccessfully()) {
         return getOutputForSuccessfullyExecutedCommand(commandResult);
      }
      return getOutputForFailedExecutedCommand(commandResult);
   }


   /**
    * Returns the output string if the according command could be executed successfully.
    * 
    * @param commandResult the {@link CommandResult} of the successfully executed command
    * @return the interpreted output string for the given {@code commandResult}
    */
   @SuppressWarnings("unchecked")
   private String getOutputForSuccessfullyExecutedCommand(CommandResult commandResult) {
      List<CommandResult> listOfFailedCommandResults = new ArrayList<>();
      StringBuilder builder = new StringBuilder();
      for (CommandResult addingLearningAlgorithmCommandResult : (List<CommandResult>) commandResult.getResult()) {
         if (addingLearningAlgorithmCommandResult.isExecutedSuccessfully()) {
            builder.append(String.format(ADDED_ALGORITHM, addingLearningAlgorithmCommandResult.getResult().toString()));
         } else {
            listOfFailedCommandResults.add(addingLearningAlgorithmCommandResult);
         }
      }

      if (!listOfFailedCommandResults.isEmpty()) {
         builder.append(String.format(AMOUNT_OF_FAILED_ALGORITHMS, listOfFailedCommandResults.size()));
         builder.append(StringUtils.LINE_BREAK);
         for (CommandResult addingLearningAlgorithmCommandResult : listOfFailedCommandResults) {
            if (addingLearningAlgorithmCommandResult.isFailureReasonKnown()) {
               builder.append(ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN);
            } else {
               builder.append(String.format(ERROR_ADDED_LEARNING_ALGORITHM_FAILED,
                     addingLearningAlgorithmCommandResult.getException().getMessage()));
            }
            builder.append(StringUtils.LINE_BREAK);
         }
      }

      return builder.toString();
   }


   /**
    * Returns the output string if the according command executed with failure.
    * 
    * @param commandResult the {@link CommandResult} of the command executed with failure
    * @return the interpreted output string with failure reason for the given {@code commandResult}
    */
   @SuppressWarnings("unchecked")
   private String getOutputForFailedExecutedCommand(CommandResult commandResult) {
      StringBuilder builder = new StringBuilder();
      builder.append(commandResult.getException().getMessage());
      for (CommandResult addingLearningAlgorithmCommandResult : (List<CommandResult>) commandResult.getResult()) {
         builder.append(addingLearningAlgorithmCommandResult.getException().getMessage());
      }
      return builder.toString();
   }


   @Override
   public void init() {
      // Nothing to do here.
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new LoadLearningAlgorithmsCommandConfiguration();
   }

}
