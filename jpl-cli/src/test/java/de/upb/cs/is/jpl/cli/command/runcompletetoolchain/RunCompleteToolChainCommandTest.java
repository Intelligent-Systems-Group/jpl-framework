package de.upb.cs.is.jpl.cli.command.runcompletetoolchain;


import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommand;
import de.upb.cs.is.jpl.cli.core.SystemStatus;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * This class is responsible for testing the {@link RunCompleteToolChainCommand} .
 * 
 * @author Tanja Tornede
 *
 */
public class RunCompleteToolChainCommandTest extends ACommandUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "runcompletetoolchain" + File.separator;

   private static final String PARAMETER_CONFIG = "--config=";

   private static final String CORRECT_SYSTEM_CONFIG_JSON = "correctSystemConfiguration.json";
   private static final String WRONG_SYSTEM_CONFIG_JSON = "wrongSystemConfiguration.json";


   /**
    * Creates a new unit test for the {@link DetermineApplicableAlgorithmsCommand} without any
    * additional path to the resources.
    */
   public RunCompleteToolChainCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Initializes and resets the system configuration.
    */
   @Override
   @Before
   public void setupTest() {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
   }


   /**
    * Tests the run complete tool chain command.
    * 
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work+
    */
   @Test
   public void testRunCompleteToolChainCommand()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String command[] = { ECommand.RUN_COMPLETE_TOOLCHAIN.getCommandIdentifier(),
            PARAMETER_CONFIG + getTestRessourcePathFor(CORRECT_SYSTEM_CONFIG_JSON) };

      TestUtils.simulateCommandLineInput(command);
      assertCorrectCommandResultAsLatestInCommandHistory();
   }


   /**
    * Checks if the latest pair of {@link ICommand} and {@link CommandResult} in the command history
    * of the {@link SystemStatus} is a {@link RunCompleteToolChainCommand}, if it is executed
    * successfully and if the result of the command result is a list containing pairs of
    * {@link ECommand} and {@link Boolean}. It also checks the types of the {@link ECommand}'s and
    * if they executed successfully.
    * 
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectCommandResultAsLatestInCommandHistory()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInCommandHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();

      TestUtils.assertCorrectCommandType(latestPairInCommandHistory.getFirst(), RunCompleteToolChainCommand.class);
      TestUtils.assertCorrectSuccessfulCommandResultWhichIsNotEmpty(latestPairInCommandHistory.getSecond(), List.class);

      boolean[] isExecutedSuccessfully = { true, true, true };
      assertCorrectListOfCommandResultsAsResult(latestPairInCommandHistory.getSecond(), isExecutedSuccessfully);
   }


   /**
    * Checks if the given {@link CommandResult} is a list of pairs of {@link ECommand} and
    * {@link Boolean}, if the size of the list is equal to 3 and if it contains the
    * {@link ECommand#READ_SYSTEM_CONFIGURATION}, {@link ECommand#LOAD_LEARNING_ALGORITHMS} and
    * {@link ECommand#EVALUATE_ALGORITHMS}, in this order. It also checks if this commands executed
    * successfully.
    * 
    * @param commandResult the {@link CommandResult} to check
    */
   private void assertCorrectListOfCommandResultsAsResult(CommandResult commandResult, boolean[] isExecutedSuccessfully) {
      @SuppressWarnings("unchecked")
      List<Pair<ECommand, Boolean>> commandBooleanPairList = (List<Pair<ECommand, Boolean>>) commandResult.getResult();
      Assert.assertEquals(commandBooleanPairList.size(), 3);

      ECommand[] commandOrder = { ECommand.READ_SYSTEM_CONFIGURATION, ECommand.LOAD_LEARNING_ALGORITHMS, ECommand.EVALUATE_ALGORITHMS };
      for (int i = 0; i < commandOrder.length; i++) {
         Pair<ECommand, Boolean> commandBooleanPair = commandBooleanPairList.get(i);
         Assert.assertEquals(commandOrder[i], commandBooleanPair.getFirst());
         Assert.assertEquals(isExecutedSuccessfully[i], commandBooleanPair.getSecond());
      }
   }


   /**
    * Tests the run complete tool chain command.
    * 
    * @throws IllegalArgumentException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   @Test
   public void testIncorrectRunCompleteToolChainCommand()
         throws SecurityException,
            IllegalArgumentException,
            NoSuchFieldException,
            IllegalAccessException {
      String command[] = { ECommand.RUN_COMPLETE_TOOLCHAIN.getCommandIdentifier(),
            PARAMETER_CONFIG + getTestRessourcePathFor(WRONG_SYSTEM_CONFIG_JSON) };

      TestUtils.simulateCommandLineInput(command);

      assertFailedCommandResultAsLatestInCommandHistory();
   }


   /**
    * Checks if the latest pair of {@link ICommand} and {@link CommandResult} in the command history
    * of the {@link SystemStatus} is a {@link RunCompleteToolChainCommand}, if its execution failed
    * and if the result of the command result is a list containing pairs of {@link ECommand} and
    * {@link Boolean}. It also checks the types of the {@link ECommand}'s and if their execution
    * failed.
    * 
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertFailedCommandResultAsLatestInCommandHistory()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInCommandHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();

      TestUtils.assertCorrectCommandType(latestPairInCommandHistory.getFirst(), RunCompleteToolChainCommand.class);
      TestUtils.assertCorrectFailedEmptyCommandResult(latestPairInCommandHistory.getSecond(), CommandCannotBeExecutedException.class);
   }
}
