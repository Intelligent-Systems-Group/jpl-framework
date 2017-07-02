package de.upb.cs.is.jpl.cli.core;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandHandler;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;


/**
 * The {@link SystemStatus} is responsible for giving information about the status of the overall
 * system. It gives an overview over the list of commands executed during the current session and
 * their results. In order to be able to share these information, it offers convenience methods,
 * which allow other system parts to easily grab the information the need. Apart from that it is
 * responsible for invoking the actual command execution call for all commands.
 * 
 * @author Alexander Hetzer
 *
 */
public class SystemStatus extends Observable {

   private static final String INFO_STARTING_COMMAND_EXECUTION = "Starting execution of command: %s";
   private static final String WARNING_COMMAND_COULD_NOT_BE_EXECUTED = "Command {} could not be executed. Reason: \n {}";
   private static final String WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER = "Command %s could not be executed. Reason: \n %s";

   private static final Logger logger = LoggerFactory.getLogger(SystemStatus.class);

   private List<Pair<ICommand, CommandResult>> commandHistory;

   private static SystemStatus systemStatus;


   /**
    * Private constructor, as the singleton {@link SystemStatus} should only be created once.
    */
   private SystemStatus() {
   }


   /**
    * Get singleton instance of the {@link SystemStatus}.
    * 
    * @return the singleton instance of the {@link SystemStatus}
    */
   public static SystemStatus getSystemStatus() {
      if (systemStatus == null) {
         systemStatus = new SystemStatus();
         systemStatus.init();
      }

      return systemStatus;
   }


   /**
    * Initializes the {@link SystemStatus}.
    */
   private void init() {
      commandHistory = new ArrayList<>();
      addObserver(CommandLineParserView.getCommandLineParserView());
   }


   /**
    * Executes the given {@link ICommand} and hands the result over to the given
    * {@link ICommandHandler}.
    * 
    * @param command the command to be executed
    * @param creatingCommandHandler the command handler which created the command
    */
   public void pushCommand(final ICommand command, final ICommandHandler creatingCommandHandler) {
      CommandResult commandResult;
      if (command.canBeExecuted()) {
         reportCommandExecutionStart(command);
         commandResult = initiateCommandExecution(command);
         initiateCommandResultInterpretation(creatingCommandHandler, command, commandResult);
      } else {
         commandResult = createExecutionFailedCommandResult(command.getFailureReason());
         reportCommandCannotBeExecuted(command);
      }
      addCommandAndResultToHistory(command, commandResult);
   }


   /**
    * Posts an information to the user that the execution of the given {@link ICommand} started.
    * 
    * @param command the command to report on
    */
   private void reportCommandExecutionStart(ICommand command) {
      postToUser(String.format(INFO_STARTING_COMMAND_EXECUTION, command));
   }


   /**
    * Adds the given {@link ICommand} and {@link CommandResult} as a pair to the history.
    * 
    * @param command the command to add
    * @param commandResult the command result of the given command
    */
   private void addCommandAndResultToHistory(final ICommand command, final CommandResult commandResult) {
      Pair<ICommand, CommandResult> commandAndResultPair = Pair.of(command, commandResult);
      commandHistory.add(commandAndResultPair);
   }


   /**
    * Informs all observers that the given {@link ICommand} could not be executed.
    * 
    * @param command the command which could not be executed
    */
   private void reportCommandCannotBeExecuted(final ICommand command) {
      String failureReason = command.getFailureReason();
      postToUser(String.format(WARNING_COMMAND_COULD_NOT_BE_EXECUTED_FOR_USER, command, failureReason));
      logger.warn(WARNING_COMMAND_COULD_NOT_BE_EXECUTED, command, failureReason);
   }


   /**
    * Posts the given message to the user.
    * 
    * @param message the message to be posted to the user
    */
   private void postToUser(String message) {
      setChanged();
      notifyObservers(message);
   }


   /**
    * Creates a failure {@link CommandResult} with the given failure reason and a
    * {@link CommandCannotBeExecutedException}.
    * 
    * @param failureReason the reason why the command could not be executed
    * 
    * @return a failure command result with the given failure reason and a
    *         {@link CommandCannotBeExecutedException}
    */
   private CommandResult createExecutionFailedCommandResult(String failureReason) {
      return CommandResult.createFailureCommandResult(new CommandCannotBeExecutedException(failureReason));
   }


   /**
    * Initiates the command result interpretation of the given {@link ICommand} and its
    * {@link CommandResult} on the given {@link ICommandHandler}.
    * 
    * @param commandHandler the handler to do the interpretation
    * @param command the command to be interpreted
    * @param commandResult the result to be interpreted
    */
   private void initiateCommandResultInterpretation(ICommandHandler commandHandler, ICommand command, CommandResult commandResult) {
      commandHandler.interpretCommandResult(command, commandResult);
   }


   /**
    * Executes the given {@link ICommand} and returns the {@link CommandResult}.
    * 
    * @param command the command to execute
    * 
    * @return the result of the execution
    */
   private CommandResult initiateCommandExecution(ICommand command) {
      return command.executeCommand();
   }


   /**
    * Checks whether the last command added to the {@link SystemStatus} was executed successfully or
    * not.
    * 
    * @return {@code true}, if the last command was executed successfully, otherwise {@code false}
    */
   public boolean wasLastCommandExecutedSuccessfully() {
      if (commandHistory.isEmpty()) {
         return true;
      }
      Pair<ICommand, CommandResult> lastExecutedCommandAndResult = commandHistory.get(commandHistory.size() - 1);
      return lastExecutedCommandAndResult.getSecond().isExecutedSuccessfully();
   }
}
