package de.upb.cs.is.jpl.cli.command.setlearningproblem;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.setlearningproblem.UnknownLearningProblemException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Tests for {@link SetLearningProblemCommand}.
 * 
 * @author Tanja Tornede
 *
 */
public class SetLearningProblemCommandTest extends ACommandUnitTest {

   private static final String ERROR_CONSOLE_OUTPUT_NOT_AS_EXPECTED = "The console output is not as expected.";
   private static final String ERROR_LEARNING_PROBLEM_NOT_EMPTY = "The learning problem is incorrectly set in the system configuration (%s), but should be empty.";
   private static final String ERROR_LEARNING_PROBLEM_INCORRECTLY_SET = "The learning problem %s is not set correctly in the system configuration (%s).";
   private static final String ERROR_WRONG_RESULT_OF_COMMAND_RESULT = "The result of the according command result of the "
         + SetLearningProblemCommand.class.getSimpleName() + " is not a valid identifier.";

   private static final String REFLECTION_SETLEARNINGPROBLEMCOMMAND_OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND = "OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND";
   private static final String REFLECTION_SETLEARNINGPROBLEMCOMMAND_ERROR_UNKNOWN_IDENTIFIER = "ERROR_UNKNOWN_IDENTIFIER";

   private static final String COMMAND_LP = "-lp=";
   private static final String COMMAND_SET_LEARNING_PROBLEM = "set_learning_problem";

   private static final String LEARNING_PROBLEM_ORDINAL_CLASSIFICATION = "ordinal_classification";
   private static final String LEARNING_PROBLEM_ORDINAL_CLASSIFICATION_WITH_TYPO = "ordinal_clasification";


   /**
    * Creates a new unit test for the {@link SetLearningProblemCommand} without any additional path
    * to the resources.
    */
   public SetLearningProblemCommandTest() {
      super();
   }


   /**
    * Resets the system configuration.
    */
   @Override
   @Before
   public void setupTest() {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
   }


   /**
    * Tests the resulting information if the user gives the following sample learning problem:
    * {@code set_learning_problem -lp=ordinal_classification}.
    * 
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   @Test
   public void testSetLearningProblemCommandWithSampleLearningProblem()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String learningProblem = LEARNING_PROBLEM_ORDINAL_CLASSIFICATION;
      String[] command = { COMMAND_SET_LEARNING_PROBLEM, COMMAND_LP + learningProblem };

      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);
      assertCorrectSuccessfulOutput(consoleOutput, learningProblem);
      assertCorrectSuccessfulCommandResultAsLatestInCommandHistory(learningProblem);
      assertCorrectLearningProblemSetInSystemConfiguration(learningProblem);
   }


   /**
    * Checks if the output of the command is as expected.
    * 
    * @param consoleOutput the redirected console output
    * @param learningProblem the learning problem that was set
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException Due if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectSuccessfulOutput(String consoleOutput, String learningProblem)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND = TestUtils.getStringByReflection(SetLearningProblemCommandHandler.class,
            REFLECTION_SETLEARNINGPROBLEMCOMMAND_OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND);

      StringBuilder builder = new StringBuilder();
      builder.append(TestUtils.getInfoStartingCommandExecution(ECommand.SET_LEARNING_PROBLEM));
      builder.append(String.format(reflectionString_OUTPUT_SUCCESSFULLY_EXECUTED_COMMAND, learningProblem));

      Assert.assertEquals(ERROR_CONSOLE_OUTPUT_NOT_AS_EXPECTED, builder.toString().trim(), consoleOutput.trim());
   }


   /**
    * Checks if the latest command in the command history of the system status is a
    * {@link SetLearningProblemCommand} and if the according command result is a
    * {@link ELearningProblem} with the given learning problem as identifier, if the exception is
    * not set and if the additional information are empty.
    * 
    * @param learningProblem the learning problem to check in the {@link CommandResult}
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   private void assertCorrectSuccessfulCommandResultAsLatestInCommandHistory(String learningProblem)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();

      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), SetLearningProblemCommand.class);
      TestUtils.assertCorrectSuccessfulCommandResultWhichIsNotEmpty(latestPairInHistory.getSecond(), ELearningProblem.class);

      if (!((ELearningProblem) latestPairInHistory.getSecond().getResult()).getLearningProblemIdentifier().equals(learningProblem)) {
         Assert.fail(ERROR_WRONG_RESULT_OF_COMMAND_RESULT);
      }
   }


   /**
    * Checks if the learning problem is set correctly in the system configuration.
    * 
    * @param learningProblemIdentifier the identifier of the learning problem to check
    */
   private void assertCorrectLearningProblemSetInSystemConfiguration(String learningProblemIdentifier) {
      ELearningProblem sytemConfigurationLearningProblem = SystemConfiguration.getSystemConfiguration().getLearningProblem();
      if (!sytemConfigurationLearningProblem.getLearningProblemIdentifier().equals(learningProblemIdentifier)) {
         Assert.fail(String.format(ERROR_LEARNING_PROBLEM_INCORRECTLY_SET, learningProblemIdentifier,
               sytemConfigurationLearningProblem.getLearningProblemIdentifier()));
      }

   }


   /**
    * Tests the resulting information if the user gives the following not existing learning problem
    * (typo in it): {@code set_learning_problem -lp=ordinal_clasification}.
    * 
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   @Test
   public void testSetLearningProblemCommandWithUnknownLearningProblem()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String wrongLearningProblem = LEARNING_PROBLEM_ORDINAL_CLASSIFICATION_WITH_TYPO;
      String[] wrongCommand = { COMMAND_SET_LEARNING_PROBLEM, COMMAND_LP + wrongLearningProblem };

      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(wrongCommand);

      assertCorrectFailedOutput(consoleOutput, wrongLearningProblem);
      assertCorrectFailedCommandResultAsLatestInCommandHistory();
      assertNoLearningProblemSetInSystemConfiguration();
   }


   /**
    * Checks if the output of the command is as expected.
    * 
    * @param consoleOutput the redirected console output
    * @param wrongLearningProblem the learning problem that should not be set
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    */
   private void assertCorrectFailedOutput(String consoleOutput, String wrongLearningProblem)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_ERROR_UNKNOWN_IDENTIFIER = TestUtils.getStringByReflection(SetLearningProblemCommand.class,
            REFLECTION_SETLEARNINGPROBLEMCOMMAND_ERROR_UNKNOWN_IDENTIFIER);

      StringBuilder builder = new StringBuilder();
      builder.append(TestUtils.getInfoStartingCommandExecution(ECommand.SET_LEARNING_PROBLEM));
      builder.append(String.format(String.format(reflectionString_ERROR_UNKNOWN_IDENTIFIER, wrongLearningProblem)));

      Assert.assertEquals(ERROR_CONSOLE_OUTPUT_NOT_AS_EXPECTED, builder.toString().trim(), consoleOutput.toString().trim());
   }


   /**
    * Checks if the latest command in the command history of the system status is a
    * {@link SetLearningProblemCommand} and if the according command result has no result, if the
    * exception is set correctly and if no additional information are empty.
    * 
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   private void assertCorrectFailedCommandResultAsLatestInCommandHistory()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), SetLearningProblemCommand.class);
      TestUtils.assertCorrectFailedEmptyCommandResult(latestPairInHistory.getSecond(), UnknownLearningProblemException.class);
   }


   /**
    * Checks if the learning problem is not set in the system configuration.
    */
   private void assertNoLearningProblemSetInSystemConfiguration() {
      ELearningProblem sytemConfigurationLearningProblem = SystemConfiguration.getSystemConfiguration().getLearningProblem();
      if (SystemConfiguration.getSystemConfiguration().getLearningProblem() != null) {
         Assert.fail(String.format(ERROR_LEARNING_PROBLEM_NOT_EMPTY, sytemConfigurationLearningProblem.getLearningProblemIdentifier()));
      }
   }

}
