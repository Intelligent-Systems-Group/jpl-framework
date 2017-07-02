package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This command Handler is responsible for handling given
 * {@link AddLearningAlgorithmCommandConfiguration} and creating an
 * {@link AddLearningAlgorithmCommand}. It holds the according command configuration before it is
 * passed to the command. Additionally it is responsible for interpreting the {@link CommandResult}
 * given by the command at the end of execution.
 *
 * @author Alexander Hetzer
 * @author Sebastian Gottschalk
 */
public class AddLearningAlgorithmCommandHandler extends ACommandHandler {

   private static final String ERROR_UNKNOWN_PROBLEM_OCCURRED = "Could not add learning algorithm to the system configuration due to an unexpected problem.";
   private static final String ERROR_COULD_NOT_ADD_LEARNING_ALGORITHM = "Could not add learning algorithm to the system configuration due to the following problem: %s";
   private static final String SUCCESS_ADDED_LEARNING_ALGORITHM = "Successfully added learning algorithm '%s' to the system configuration.";


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      AddLearningAlgorithmCommandConfiguration castedCommandConfiguration = (AddLearningAlgorithmCommandConfiguration) commandConfiguration;
      return new AddLearningAlgorithmCommand(castedCommandConfiguration);
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      return getMessageFromCommandResult(commandResult);
   }


   @Override
   public void init() {
      // not used, as there are no member variables in this class
   }


   /**
    * Returns the message to be displayed to the user to inform him about the given command result.
    * 
    * @param commandResult the command result to create the message for
    * 
    * @return the message to be displayed to the user to inform him about the given command result
    */
   private String getMessageFromCommandResult(CommandResult commandResult) {
      String message;
      if (commandResult.isExecutedSuccessfully()) {
         message = getMessageForSuccessCommandResult(commandResult);
      } else {
         message = getMessageForFailureCommandResult(commandResult);
      }
      return message;
   }


   /**
    * Returns the message to be displayed to the user for a success command result.
    * 
    * @param commandResult the command result to create the message for
    * 
    * @return the message to be displayed to the user for a success command result
    */
   private String getMessageForSuccessCommandResult(CommandResult commandResult) {
      String message;
      ILearningAlgorithm learningAlgorithm = (ILearningAlgorithm) commandResult.getResult();
      message = String.format(SUCCESS_ADDED_LEARNING_ALGORITHM, learningAlgorithm);
      return message;
   }


   /**
    * Returns the message to be displayed to the user for a failure command result.
    * 
    * @param commandResult the command result to create the message for
    * 
    * @return the message to be displayed to the user for a failure command result
    */
   private String getMessageForFailureCommandResult(CommandResult commandResult) {
      String message;
      if (commandResult.isFailureReasonKnown()) {
         Exception exception = commandResult.getException();
         message = String.format(ERROR_COULD_NOT_ADD_LEARNING_ALGORITHM, exception.getMessage());
      } else {
         message = ERROR_UNKNOWN_PROBLEM_OCCURRED;
      }
      return message;
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new AddLearningAlgorithmCommandConfiguration();
   }
}
