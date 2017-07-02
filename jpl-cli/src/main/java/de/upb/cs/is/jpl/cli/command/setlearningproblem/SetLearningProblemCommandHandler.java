package de.upb.cs.is.jpl.cli.command.setlearningproblem;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.setlearningproblem.WrongAmountOfParametersException;


/**
 * This command Handler is responsible for handling given
 * {@link SetLearningProblemCommandConfiguration} and creating an {@link SetLearningProblemCommand}.
 * It holds the according command configuration before it is passed to the command. Additionally it
 * is responsible for interpreting the {@link CommandResult} given by the command at the end of
 * execution.
 * 
 * @author Tanja Tornede
 *
 */
public class SetLearningProblemCommandHandler extends ACommandHandler {

   private static final Logger logger = LoggerFactory.getLogger(SetLearningProblemCommandHandler.class);

   private static final String ERROR_WRONG_AMOUNT_OF_PARAMETERS = "The amount of given parameters for set_learning_problem is wrong!";
   private static final String OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND = "The learning problem is successfully set to %s.";

   private String learningProblemIdentifier;
   private SetLearningProblemCommand setLearningProblemCommand;
   private SetLearningProblemCommandConfiguration setLearningProblemCommandConfiguration;


   @Override
   public void init() {
      // nothing to do here
   }


   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      parseCommandConfiguration(commandConfiguration);
      return setLearningProblemCommand;
   }


   /**
    * Parses the given command configuration and creates a set learning problem command, initialized
    * from the given command configuration.
    * 
    * @param commandConfiguration the configuration to initialize the set learning problem command
    *           from
    */
   public void parseCommandConfiguration(final ICommandConfiguration commandConfiguration) {
      this.setLearningProblemCommandConfiguration = (SetLearningProblemCommandConfiguration) commandConfiguration;
      if (this.setLearningProblemCommandConfiguration.getLearningProblem().isEmpty()) {
         logger.error(ERROR_WRONG_AMOUNT_OF_PARAMETERS);
         this.setLearningProblemCommand = SetLearningProblemCommand.createLearningProblemCommandWithWrongAmountOfParameters(
               new WrongAmountOfParametersException(ERROR_WRONG_AMOUNT_OF_PARAMETERS));
      } else {
         learningProblemIdentifier = this.setLearningProblemCommandConfiguration.getLearningProblem();
         this.setLearningProblemCommand = SetLearningProblemCommand.createLearningProblemCommandByIdentifier(learningProblemIdentifier);
      }
   }


   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      return getOutput(commandResult);
   }


   /**
    * Returns the output string of the given {@code commandResult}.
    * 
    * @param commandResult the {@link CommandResult} of a specific {@link SetLearningProblemCommand}
    *           which will be interpreted
    * @return the output string of the given {@code commandResult}
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
    * @return the output string of the given {@code commandResult}
    */
   private String getOutputForSuccessfullyExecutedCommand(CommandResult commandResult) {
      return String.format(OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND,
            ((ELearningProblem) commandResult.getResult()).getLearningProblemIdentifier());
   }


   /**
    * Returns the output string if the according command executed with failure.
    * 
    * @param commandResult the {@link CommandResult} of the command executed with failure
    * @return the output string of the given {@code commandResult}
    */
   private String getOutputForFailedExecutedCommand(CommandResult commandResult) {
      return commandResult.getException().getMessage();
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new SetLearningProblemCommandConfiguration();
   }

}
