package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandHandler;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This command Handler is responsible for handling given
 * {@link EvaluateAlgorithmsCommandConfiguration} and creating an {@link EvaluateAlgorithmsCommand}.
 * It holds the according command configuration before it is passed to the command. Additionally it
 * is responsible for interpreting the {@link CommandResult} given by the command at the end of
 * execution.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluateAlgorithmsCommandHandler extends ACommandHandler {

   private String evaluationIdentifier;
   private List<String> metricIdentifiers;
   private static final String EVALUATION_CONFIGURATION_NOTSET_WARNING_MESSAGE = "The EvaluationConfigurations were not set in the CommandLineParser";
   private static final String COMMAND_RESULT_EXECUTION_ERROR_MESSAGE = "The evaluation command failed to execute.";

   private static final String EXCEPTIONS_OCCURED_MESSAGE = "The following exception(s) occurred while executing the command:";

   private static final String EVALUATION_IDENTIFIER_EMPTY_WARNING_MESSAGE = " The evaluation identifier is empty in the command configuration.";
   private static final String METRIC_IDENTIFIERS_EMPTY_WARNING_MESSAGE = " The metric identifiers are empty in the command configuration.";

   private static final Logger logger = LoggerFactory.getLogger(EvaluateAlgorithmsCommandHandler.class);


   /**
    * 
    * {@inheritDoc} It parses the parameters assigned via the {@link JCommander} command for
    * {@literal evaluationIdentifier} and {@literal evaluationMetricIdentifier}.
    */
   @Override
   public ICommand handleUserCommand(final ICommandConfiguration commandConfiguration) {
      init();
      EvaluateAlgorithmsCommandConfiguration castedConfiguration = (EvaluateAlgorithmsCommandConfiguration) commandConfiguration;
      if (castedConfiguration != null) {
         if (!castedConfiguration.getEvaluationIdentifier().isEmpty()) {
            evaluationIdentifier = castedConfiguration.getEvaluationIdentifier();
         } else {
            logger.warn(EVALUATION_IDENTIFIER_EMPTY_WARNING_MESSAGE);
         }
         if (!castedConfiguration.getMetricIdentifier().isEmpty()) {
            metricIdentifiers = castedConfiguration.getMetricIdentifier();
         } else {
            logger.warn(METRIC_IDENTIFIERS_EMPTY_WARNING_MESSAGE);
         }
      } else {
         logger.warn(EVALUATION_CONFIGURATION_NOTSET_WARNING_MESSAGE);
      }
      return new EvaluateAlgorithmsCommand(evaluationIdentifier, metricIdentifiers);
   }


   /**
    * {@inheritDoc} Get the necessary output after execution of the {@link ICommand}. Sets the
    * changes {@literal evaluationMetricIdentifier} the {@code Observable.setChanged()} is used as
    * indication or flag for changing the state. If it true the notifyObservers() can run and update
    * all the observers. If it false the notifyObservers() is called without calling the
    * {@code Observable.setChanged()} and the observers will not be notified.
    * 
    * @param command the command to be interpreted
    * @param commandResult the {@link CommandResult} for the command
    */
   @Override
   public String getCommandResultInterpretationOutput(ICommand command, CommandResult commandResult) {
      String userOutput = getOutput(commandResult);
      EvaluateAlgorithmsCommand castedCommand = (EvaluateAlgorithmsCommand) command;
      String evaluationOutput = getEvaluationOutput(castedCommand, commandResult);
      if (!evaluationOutput.equals(StringUtils.EMPTY_STRING)) {
         userOutput = combineOutputs(userOutput, evaluationOutput);
      }
      return userOutput;
   }


   @Override
   public void init() {
      evaluationIdentifier = StringUtils.EMPTY_STRING;
      metricIdentifiers = new ArrayList<>();
   }


   @Override
   protected ICommandConfiguration createDefaultCommandConfiguration() {
      return new EvaluateAlgorithmsCommandConfiguration();
   }


   /**
    * To combine the evaluation result and command result.
    * 
    * @param userOutput the interpreted command result {@link String}
    * @param evaluationOutput the interpreted evaluation result {@link String}
    * @return the combined output of the command result and evaluation result
    */
   public String combineOutputs(String userOutput, String evaluationOutput) {
      StringBuilder builder = new StringBuilder();
      builder.append(userOutput);
      builder.append(StringUtils.LINE_BREAK);

      builder.append(evaluationOutput);
      return builder.toString();
   }


   /**
    * Interpret and create the evaluation result from the command.
    * 
    * @param command the {@link ICommand} object to get the evaluation result in form of string
    * @param commandResult the {@link CommandResult} to check if the command was executed
    *           successfully
    * @return the interpreted evaluation result of type {@link String}
    */
   private String getEvaluationOutput(EvaluateAlgorithmsCommand command, CommandResult commandResult) {
      String evaluationOutput = StringUtils.EMPTY_STRING;
      if (commandResult.isExecutedSuccessfully()) {
         evaluationOutput = command.getEvaluation().interpretEvaluationResult();
      }
      return evaluationOutput;
   }


   /**
    * Returns the concatenation of the evaluation output and the exception message, if one occurred.
    * 
    * @param commandResult the {@link CommandResult} of the executed command
    * @return the concatenated evaluation command additional information and exception message if
    *         command is not executed successfully, else get the command result to be printed for
    *         console
    * 
    */
   private String getOutput(CommandResult commandResult) {
      if (!commandResult.isExecutedSuccessfully()) {
         return getOutputForExecutedCommandWithException(commandResult);
      }
      return getOutputForExecutedCommand(commandResult);
   }


   /**
    * For {@link EvaluateAlgorithmsCommand} we can get the results as loss of the particular
    * algorithm or comparison result of different algorithms based on matrix. Returns the output
    * string of the given {@code commandResult}.
    * 
    * @param commandResult the {@link CommandResult} of the executed command
    * @return the output string of the given {@code commandResult}
    */
   private String getOutputForExecutedCommand(CommandResult commandResult) {
      return commandResult.getResult().toString();
   }


   /**
    * Returns the output string of the given {@code commandResult} which includes the information of
    * the exception of the given {@code commandResult}.
    * 
    * @param commandResult the {@link CommandResult} of the command executed with failures
    * @return the output {@link String}, includes the information of the exception in the
    *         {@link CommandResult}
    */
   private String getOutputForExecutedCommandWithException(CommandResult commandResult) {
      StringBuilder builder = new StringBuilder();
      builder.append(COMMAND_RESULT_EXECUTION_ERROR_MESSAGE);
      builder.append(StringUtils.LINE_BREAK);
      builder.append(EXCEPTIONS_OCCURED_MESSAGE);
      builder.append(StringUtils.LINE_BREAK);
      builder.append(commandResult.getException().getMessage());
      return builder.toString();
   }
}
