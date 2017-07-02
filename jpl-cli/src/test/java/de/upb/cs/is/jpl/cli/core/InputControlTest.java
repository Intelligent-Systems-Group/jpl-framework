package de.upb.cs.is.jpl.cli.core;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.ICommandHandler;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommandHandler;
import de.upb.cs.is.jpl.cli.command.setlearningproblem.SetLearningProblemCommand;
import de.upb.cs.is.jpl.cli.command.setlearningproblem.SetLearningProblemCommandHandler;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link InputControl}.
 * 
 * @author Alexander Hetzer
 *
 */
public class InputControlTest {

   private static final String REFLECTION_INPUTCONTROL_VARIABLE_COMMAND_IDENTIFIER_TO_COMMAND_HANDLER_MAP = "commandIdentifierToCommandHandlerMap";
   private static final String FIELD_SYSTEM_STATUS_COMMAND_HISTORY = "commandHistory";
   private static final String FIELD_INPUT_CONTROL_JCOMMANDER = "jCommander";
   private static final String FIELD_SET_LEARNING_PROBLEM_COMMAND_HANDLER_OUTPUT = "OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND";

   private static final String REGEX_AT_LEAST_ONE_WHITESPACE = "(  )+";

   private static final String OPTION_LEARNING_PROBLEM = "-lp=";
   private static final String OPTION_ERROR_BLA = "--bla";

   private static final String COMMAND_ERROR = "bla";

   private static final String TAG_INFO = "[INFO]";

   private static final String OUTPUT_ERROR_UNKNOWN_OPTION = "[ERROR]A problem with the given parameters occured: Unknown option: --bla";
   private static final String OUTPUT_WRONG_EXPECTED = "[ERROR]Unknown command: bla";

   private InputControl inputControl;
   private SystemStatus systemStatus;


   /**
    * Sets up this test.
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   @Before
   public void setupTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      inputControl = InputControl.getInputControl();
      systemStatus = SystemStatus.getSystemStatus();
      resetCommandHistoryOfSystemStatus();

   }


   /**
    * Resets the command history of the {@link SystemStatus}.
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   private void resetCommandHistoryOfSystemStatus()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Field f = systemStatus.getClass().getDeclaredField(FIELD_SYSTEM_STATUS_COMMAND_HISTORY);
      f.setAccessible(true);

      @SuppressWarnings("unchecked")
      List<Pair<ICommand, CommandResult>> commandHistory = (List<Pair<ICommand, CommandResult>>) f.get(systemStatus);
      commandHistory.clear();
   }


   /**
    * Test if all required command handlers are registered correctly.
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   @SuppressWarnings("unchecked")
   @Test
   public void testRegisterProcessOfCommandHandlers()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Field f = inputControl.getClass().getDeclaredField(REFLECTION_INPUTCONTROL_VARIABLE_COMMAND_IDENTIFIER_TO_COMMAND_HANDLER_MAP);
      f.setAccessible(true);
      Map<String, ICommandHandler> identifierToHandlerMap = (Map<String, ICommandHandler>) f.get(inputControl);

      List<ECommand> supportedCommands = ECommand.getECommands();
      for (ECommand command : supportedCommands) {
         // check if each handler is registered
         String commandIdentifier = command.getCommandIdentifier();
         assertTrue(identifierToHandlerMap.containsKey(commandIdentifier));

         // check if the correct handler is registered
         ICommandHandler commandHandler = command.createCommandHandler();
         assertSame(commandHandler.getClass(), identifierToHandlerMap.get(commandIdentifier).getClass());
      }
   }


   /**
    * Test if the update call fails with a wrong Observable.
    */
   @Test(expected = UnsupportedOperationException.class)
   public void testUpdateFailureWithWrongObservable() {
      inputControl.update(new DetermineApplicableAlgorithmsCommandHandler(), StringUtils.EMPTY_STRING);
   }


   /**
    * Test if the update call fails with a wrong argument.
    */
   @Test(expected = UnsupportedOperationException.class)
   public void testUpdateFailureWithWrongArgument() {
      inputControl.update(CommandLineParserView.getCommandLineParserView(), new Exception());
   }


   /**
    * Test if a command gets passed correctly to the {@link SystemStatus}.
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   @Test
   public void testIfCommandsArePassedCorrectlyToSystemStatus()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String[] commandArray = { ECommand.SET_LEARNING_PROBLEM.getCommandIdentifier(),
            OPTION_LEARNING_PROBLEM + ELearningProblem.MULTILABEL_CLASSIFICATION.getLearningProblemIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(commandArray);

      assertCorrectCommandInSystemStatus();
      assertCorrectSetLearningProblemCommandOutput(consoleOutput);
   }


   /**
    * Asserts that the given output generated by the {@link SetLearningProblemCommand} is the
    * correct one.
    * 
    * @param actualOutput the output to check
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   private void assertCorrectSetLearningProblemCommandOutput(String actualOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String expectedOutput = getExpectedOutputOfSetLearningProblemCommand();
      assertEquals(expectedOutput, actualOutput.trim());
   }


   /**
    * Returns the expected output of the {@link SetLearningProblemCommand}.
    * 
    * @return the expected output of the {@link SetLearningProblemCommand}
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   private String getExpectedOutputOfSetLearningProblemCommand()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      SetLearningProblemCommandHandler setLearningProblemCommandHandler = new SetLearningProblemCommandHandler();
      Field correctOutputField = setLearningProblemCommandHandler.getClass()
            .getDeclaredField(FIELD_SET_LEARNING_PROBLEM_COMMAND_HANDLER_OUTPUT);
      correctOutputField.setAccessible(true);

      String correctOutput = TestUtils.getInfoStartingCommandExecution(ECommand.SET_LEARNING_PROBLEM);
      correctOutput += String.format((String) correctOutputField.get(setLearningProblemCommandHandler),
            ELearningProblem.MULTILABEL_CLASSIFICATION.getLearningProblemIdentifier());
      return correctOutput;
   }


   /**
    * Asserts that only one and the correct command is in the {@link SystemStatus}.
    * 
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    */
   private void assertCorrectCommandInSystemStatus()
         throws IllegalArgumentException,
            IllegalAccessException,
            NoSuchFieldException,
            SecurityException {

      List<Pair<ICommand, CommandResult>> commandHistory = TestUtils.getCommandHistoryOfSystemStatus();
      assertOnlyOneCommandInCommandHistory(commandHistory);
      assertCorrectCommandInCommandHistory(commandHistory);
   }


   /**
    * Asserts that the correct command is in the command history of the {@link SystemStatus}.
    * 
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    */
   private void assertCorrectCommandInCommandHistory(List<Pair<ICommand, CommandResult>> commandHistory) {
      Pair<ICommand, CommandResult> historyEntry = commandHistory.get(0);
      assertSame(historyEntry.getFirst().getClass(),
            SetLearningProblemCommand
                  .createLearningProblemCommandByIdentifier(ELearningProblem.MULTILABEL_CLASSIFICATION.getLearningProblemIdentifier())
                  .getClass());
   }


   /**
    * Asserts that only one command is in the command history.
    * 
    * @param commandHistory the command history to check
    */
   private void assertOnlyOneCommandInCommandHistory(List<Pair<ICommand, CommandResult>> commandHistory) {
      assertEquals(1, commandHistory.size());
   }


   /**
    * Tests if unknown parameters are treated correctly. A warning should be shown to the user.
    */
   @Test
   public void testIfWrongParamtersAreCaught() {
      String[] commandArray = { ECommand.SET_LEARNING_PROBLEM.getCommandIdentifier(), OPTION_ERROR_BLA };
      String expectedResult = OUTPUT_ERROR_UNKNOWN_OPTION;
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(commandArray);

      assertEquals(expectedResult, consoleOutput.toString().trim());
   }


   /**
    * Tests if unknown commands are treated correctly. A warning should be shown to the user.
    * 
    * @throws NoSuchFieldException if the use of reflection fails
    * @throws SecurityException if the use of reflection fails
    * @throws IllegalArgumentException if the use of reflection fails
    * @throws IllegalAccessException if the use of reflection fails
    */
   @Test
   public void testIfWrongCommandsAreCaughtCorrectly()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String[] commandArray = { COMMAND_ERROR };
      String expectedResult = OUTPUT_WRONG_EXPECTED + StringUtils.LINE_BREAK;
      String jCommanderUsageOutput = getJCommanderUsageOutput();
      expectedResult += TAG_INFO + jCommanderUsageOutput;

      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(commandArray);

      assertEquals(expectedResult.trim().replaceAll(REGEX_AT_LEAST_ONE_WHITESPACE, StringUtils.EMPTY_STRING),
            consoleOutput.toString().trim());
   }


   /**
    * Returns the usage output generated by JCommander.
    * 
    * @return the usage output generated by JCommander
    * 
    * @throws NoSuchFieldException if the reflection fails
    * @throws SecurityException if the reflection fails
    * @throws IllegalArgumentException if the reflection fails
    * @throws IllegalAccessException if the reflection fails
    */
   private String getJCommanderUsageOutput()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Field jCommanderField = inputControl.getClass().getDeclaredField(FIELD_INPUT_CONTROL_JCOMMANDER);
      jCommanderField.setAccessible(true);
      StringBuilder stringBuilder = new StringBuilder();
      ((JCommander) jCommanderField.get(inputControl)).usage(stringBuilder);
      String jCommanderUsageOutput = stringBuilder.toString();
      return jCommanderUsageOutput;
   }

}
