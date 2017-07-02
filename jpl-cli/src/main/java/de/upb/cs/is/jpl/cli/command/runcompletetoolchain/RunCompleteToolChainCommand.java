package de.upb.cs.is.jpl.cli.command.runcompletetoolchain;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.evaluatealgorithms.EvaluateAlgorithmsCommand;
import de.upb.cs.is.jpl.cli.command.loadlearningalgorithms.LoadLearningAlgorithmsCommand;
import de.upb.cs.is.jpl.cli.command.readsystemconfiguration.ReadSystemConfigurationCommand;
import de.upb.cs.is.jpl.cli.core.InputControl;
import de.upb.cs.is.jpl.cli.core.SystemStatus;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * This command can be seen as the main meta command, which is used to run the whole tool chain
 * based on a system configuration json file. It simulates the user input of a
 * {@link ReadSystemConfigurationCommand}, {@link LoadLearningAlgorithmsCommand} and a
 * {@link EvaluateAlgorithmsCommand} with input from the {@link SystemConfiguration}.
 * 
 * @author Alexander Hetzer
 *
 */
public class RunCompleteToolChainCommand extends ACommand {

   private RunCompleteToolChainCommandConfiguration commandConfiguration;

   private InputControl inputControl;
   private SystemStatus systemStatus;

   private List<String[]> toolChainCommands;

   private List<Pair<ECommand, Boolean>> commandExecutionSuccessPairs;

   private String failureReason = StringUtils.EMPTY_STRING;


   /**
    * Creates a {@link RunCompleteToolChainCommand} instance and initializes it with the given
    * command configuration.
    * 
    * @param commandConfiguration the command configuration to initialize this instance with
    */
   public RunCompleteToolChainCommand(RunCompleteToolChainCommandConfiguration commandConfiguration) {
      super(ECommand.RUN_COMPLETE_TOOLCHAIN.getCommandIdentifier());
      this.commandConfiguration = commandConfiguration;
      toolChainCommands = new ArrayList<>();
      commandExecutionSuccessPairs = new ArrayList<>();
      inputControl = InputControl.getInputControl();
      systemStatus = SystemStatus.getSystemStatus();
   }


   @Override
   public boolean canBeExecuted() {
      /*
       * We do only check if the system configuration command can be executed, as the rest of the
       * chain cannot be checked efficiently without starting the process.
       */
      ReadSystemConfigurationCommand readSystemConfigurationCommand = new ReadSystemConfigurationCommand(
            commandConfiguration.getConfigurationFilePath());
      boolean canBeExecuted = readSystemConfigurationCommand.canBeExecuted();
      failureReason = readSystemConfigurationCommand.getFailureReason();
      return canBeExecuted;
   }


   @Override
   public CommandResult executeCommand() {
      prepareReadSystemConfigurationCommand();
      prepareLoadLearningAlgorithmsCommand();
      prepareEvaluateAlgorithmsCommand();
      executeCommandsInToolChain();
      return CommandResult.createSuccessCommandResult(commandExecutionSuccessPairs);
   }


   /**
    * Executes all commands in {@link #toolChainCommands}.
    */
   private void executeCommandsInToolChain() {
      Iterator<String[]> commandIterator = toolChainCommands.iterator();
      boolean lastCommandExecutedSuccessfully = true;
      int numberOfCommandsExecutedSuccessufully = 0;
      while (commandIterator.hasNext() && lastCommandExecutedSuccessfully) {
         String[] command = commandIterator.next();
         executeCommand(command);
         lastCommandExecutedSuccessfully = systemStatus.wasLastCommandExecutedSuccessfully();
         commandExecutionSuccessPairs.get(numberOfCommandsExecutedSuccessufully).setSecond(lastCommandExecutedSuccessfully);
         numberOfCommandsExecutedSuccessufully++;
      }

   }


   /**
    * Simulates the user input of the given command string on the console.
    * 
    * @param command the command as entered by the user, split at whitespace
    */
   private void executeCommand(String[] command) {
      inputControl.simulateUserInput(command);
   }


   /**
    * Prepares the input for the {@link ReadSystemConfigurationCommand} and stores it inside the
    * {@link #toolChainCommands} and {@link #commandExecutionSuccessPairs}.
    */
   private void prepareReadSystemConfigurationCommand() {
      String[] readSystemConfigurationCommand = { ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier(),
            "-p=" + commandConfiguration.getConfigurationFilePath() };
      toolChainCommands.add(readSystemConfigurationCommand);
      commandExecutionSuccessPairs.add(Pair.of(ECommand.READ_SYSTEM_CONFIGURATION, false));
   }


   /**
    * Prepares the input for the {@link LoadLearningAlgorithmsCommand} and stores it inside the
    * {@link #toolChainCommands} and {@link #commandExecutionSuccessPairs}.
    */
   private void prepareLoadLearningAlgorithmsCommand() {
      String[] setMultipleLearningAlgorithmsCommand = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      toolChainCommands.add(setMultipleLearningAlgorithmsCommand);
      commandExecutionSuccessPairs.add(Pair.of(ECommand.LOAD_LEARNING_ALGORITHMS, false));
   }


   /**
    * Prepares the input for the {@link EvaluateAlgorithmsCommand} and stores it inside the
    * {@link #toolChainCommands} and {@link #commandExecutionSuccessPairs}.
    */
   private void prepareEvaluateAlgorithmsCommand() {
      String[] evaluateAlgorithmsCommand = { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      toolChainCommands.add(evaluateAlgorithmsCommand);
      commandExecutionSuccessPairs.add(Pair.of(ECommand.EVALUATE_ALGORITHMS, false));
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      // not needed
   }

}
