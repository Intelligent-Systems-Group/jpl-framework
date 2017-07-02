package de.upb.cs.is.jpl.cli.command.runcompletetoolchain;


import java.util.List;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ECommand;


/**
 * This output generator is responsible for generating nicely formatted output for the
 * {@link RunCompleteToolChainCommandHandler}, which is used in the command result interpretation
 * process. Note that this class can only be used inside its package.
 * 
 * @author Alexander Hetzer
 *
 */
public class RunCompleteToolChainCommandOutputGenerator {

   private static final String INFO_COMMANDS_EXECUTED_SUCCESSFULLY = "%d/%d commands were executed successfully. ";

   /* Strings used for creating the result overview table. */
   private static final String OVERVIEW_FAILED = "failed";
   private static final String OVERVIEW_SUCCESSFUL = "successful";
   private static final String OVERVIEW_COMMAND = "Command";
   private static final String OVERVIEW_EXECUTION_RESULT = "Execution Result";
   private static final String OVERVIEW_EXECUTION_DETAILS = "Execution details: ";

   private static final String RESULT_MESSAGE_ALIGNEMENT = "%-30s";


   /**
    * Generates the result message based on the execution results of the commands. Note that this
    * method is package private in order to ensure that it is only called inside the package.
    * 
    * @param commandExecutionSuccessPairs a list containing the execution results
    * 
    * @return the final result message
    */
   String generateResultMessage(List<Pair<ECommand, Boolean>> commandExecutionSuccessPairs) {
      int numberOfSuccessfullExecutions = countNumberOfSuccessfullyExecutedCommands(commandExecutionSuccessPairs);
      String resultMessagePrefix = createResultMessagePrefix(commandExecutionSuccessPairs.size(), numberOfSuccessfullExecutions);
      StringBuilder stringBuilder = new StringBuilder(resultMessagePrefix);
      stringBuilder.append(createResultOverviewTableHeader());
      stringBuilder.append(createResultOverviewTableBody(commandExecutionSuccessPairs));
      return stringBuilder.toString().trim();
   }


   /**
    * Creates the prefix of the result message.
    * 
    * @param numberOfExecutionsScheduled the number of executions scheduled
    * @param numberOfSuccessfullExecutions the number of successful executions
    * 
    * @return the prefix of the result message
    */
   private String createResultMessagePrefix(int numberOfExecutionsScheduled, int numberOfSuccessfullExecutions) {
      String summarizedExecutionMessage = String.format(INFO_COMMANDS_EXECUTED_SUCCESSFULLY, numberOfSuccessfullExecutions,
            numberOfExecutionsScheduled);
      StringBuilder resultMessageBuilder = new StringBuilder(summarizedExecutionMessage);
      resultMessageBuilder.append(OVERVIEW_EXECUTION_DETAILS);
      resultMessageBuilder.append(StringUtils.LINE_BREAK);
      return resultMessageBuilder.toString();
   }


   /**
    * Creates the result overview table body.
    * 
    * @param commandExecutionSuccessPairs a list of pairs of a command and a boolean, where the
    *           boolean gives information if the command was executed successfully or not
    * 
    * @return the body of the result overview table
    */
   private String createResultOverviewTableBody(List<Pair<ECommand, Boolean>> commandExecutionSuccessPairs) {
      StringBuilder stringBuilder = new StringBuilder();
      for (Pair<ECommand, Boolean> commandSuccessPair : commandExecutionSuccessPairs) {
         stringBuilder.append(String.format(RESULT_MESSAGE_ALIGNEMENT, commandSuccessPair.getFirst().getCommandIdentifier()));
         stringBuilder.append(StringUtils.COLON_WITH_SINGLE_WHITESPACE_BEHIND);
         stringBuilder.append(getMeaningStringOfBoolean(commandSuccessPair.getSecond()));
         stringBuilder.append(StringUtils.LINE_BREAK);
      }
      return stringBuilder.toString();
   }


   /**
    * Creates the table header of the result overview table, displayed as a result of the command to
    * the user.
    * 
    * @return the table header of the result overview table displayed to the user
    */
   private String createResultOverviewTableHeader() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format(RESULT_MESSAGE_ALIGNEMENT, OVERVIEW_COMMAND));
      stringBuilder.append(StringUtils.COLON_WITH_SINGLE_WHITESPACE_BEHIND);
      stringBuilder.append(OVERVIEW_EXECUTION_RESULT);
      stringBuilder.append(StringUtils.LINE_BREAK);
      stringBuilder.append(StringUtils.repeat(StringUtils.EQUALS_SIGN, 32 + OVERVIEW_EXECUTION_RESULT.length()));
      stringBuilder.append(StringUtils.LINE_BREAK);
      return stringBuilder.toString();
   }


   /**
    * Returns 'successful', if the given boolean is {@code true}, returns 'failed', if the given
    * boolean is {@code false}.
    * 
    * @param executedSuccessfully the boolean to generate the meaning string for
    * 
    * @return 'successful', if the given boolean is {@code true}, returns 'failed', if the given
    *         boolean is {@code false}
    */
   private String getMeaningStringOfBoolean(boolean executedSuccessfully) {
      if (executedSuccessfully) {
         return OVERVIEW_SUCCESSFUL;
      }
      return OVERVIEW_FAILED;
   }


   /**
    * Counts the number of successfully executed commands in the given list of command execution
    * success pairs.
    * 
    * @param commandExecutionSuccessPairs a list of command execution success pairs
    * 
    * @return the number of successfully executed commands
    */
   private int countNumberOfSuccessfullyExecutedCommands(List<Pair<ECommand, Boolean>> commandExecutionSuccessPairs) {
      int numberOfSuccessfullExecutions = 0;
      for (Pair<ECommand, Boolean> commandSuccessPair : commandExecutionSuccessPairs) {
         if (commandSuccessPair.getSecond()) {
            numberOfSuccessfullExecutions++;
         }
      }
      return numberOfSuccessfullExecutions;
   }
}
