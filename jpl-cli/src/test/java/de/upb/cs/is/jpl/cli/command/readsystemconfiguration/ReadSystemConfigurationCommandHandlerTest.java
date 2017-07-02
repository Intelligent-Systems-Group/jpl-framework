package de.upb.cs.is.jpl.cli.command.readsystemconfiguration;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link ReadSystemConfigurationCommand}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class ReadSystemConfigurationCommandHandlerTest extends ACommandUnitTest {


   private static final String SYSTEM_CONFIGURATION_BASIC = "systemConfiguration.json";
   private static final String SYSTEM_CONFIGURATION_EMPTY_INPUT_JSON_PATH = "SystemConfigurationEmptyInput.json";
   private static final String SYSTEM_CONFIGURATION_MINIMUM_INPUT_JSON_PATH = "SystemConfigurationMinimumInput.json";
   private static final String SYSTEM_CONFIGURATION_INCORRECT_INPUT_JSON_PATH = "SystemConfigurationIncorrectInput.json";
   private static final String SYSTEM_CONFIGURATION_INCORRECT_INPUT_JSON_PATH_2 = "SystemConfigurationIncorrectInput.json";
   private static final String SYSTEM_CONFIGURATION_INCOMPLETE_INPUT_JSON_PATH = "SystemConfigurationIncompleteInput.json";
   private static final String OUTPUT_PATH_TXT = "output.txt";


   private static final String ASSERT_TRUE_SUCCESS_MESSAGE = "The command should contain the success message, commandSuccessMessage: %s.";
   private static final String ASSERT_EQUALS_EXECUTEABLE = "The read system configuration should be executeable, canBeExecuted: %s.";
   private static final String ASSERT_EQUALS_EXECUTE_SUCCESSFUL = "The execution of the read system configuration command should be successful, executedSuccessfully: %s";
   private static final String ASSERT_EQUALS_EMPTY_FAILURE_REASON = "The failure reason should be empty, failureReason: %s.";
   private static final String ASSERT_TRUE_OUTPUT_FILE_NAME = "The path of the output file should be equals: %s.";
   private static final String ASSERT_TRUE_FILE_SIZE = "The size of the datasetFiles should be 2.";
   private static final String ASSERT_EQULAS_RANK_AGGREGATION_PROBLEM = "The learning problem should be: %s.";

   private static final String COMMAND_SUCCESS_MESSAGE = "COMMAND_SUCCESS_MESSAGE";
   private static final String PARAMETER_KEY_FILEPATH_LONG = "PARAMETER_KEY_FILEPATH_LONG";


   private static final String RESOURCE_DIRECTORY_LEVEL = "readsystemconfiguration" + File.separator;


   /**
    * Creates a new unit test for the {@link ReadSystemConfigurationCommand} without any additional
    * path to the resources.
    */
   public ReadSystemConfigurationCommandHandlerTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Tests the {@link ReadSystemConfigurationCommand} command line output.
    *
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws NoSuchFieldException if there is no file for the provided absolute path
    * @throws IllegalAccessException if the type for the provided assert statement is of a wrong
    *            type
    */
   @Test
   public void testConsoleOutputForCorrectReadSystemConfiguration()
         throws SecurityException,
            IllegalArgumentException,
            NoSuchFieldException,
            IllegalAccessException {

      String parameterKeyFilePathLong = TestUtils.getStringByReflection(ReadSystemConfigurationCommandConfiguration.class,
            PARAMETER_KEY_FILEPATH_LONG);

      String[] command = { ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier(),
            parameterKeyFilePathLong + StringUtils.EQUALS_SIGN + getTestRessourcePathFor(SYSTEM_CONFIGURATION_BASIC) };

      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);
      String commandSuccessMessage = String.format(
            TestUtils.getStringByReflection(ReadSystemConfigurationCommandHandler.class, COMMAND_SUCCESS_MESSAGE),
            ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier());

      assertTrue(String.format(ASSERT_TRUE_SUCCESS_MESSAGE, commandSuccessMessage), consoleOutput.contains(commandSuccessMessage));
   }


   /**
    * Tests a correct JSON input File.
    */
   @Test
   public void testCorrectInputForSystemConfigurationCommand() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_BASIC), true, true);

      SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();

      List<DatasetFile> datasetFiles = systemConfiguration.getDatasetFiles();
      assertTrue(ASSERT_TRUE_FILE_SIZE, datasetFiles.size() == 2);

      ELearningProblem learningProblem = systemConfiguration.getLearningProblem();
      assertEquals(String.format(ASSERT_EQULAS_RANK_AGGREGATION_PROBLEM, ELearningProblem.RANK_AGGREGATION),
            ELearningProblem.RANK_AGGREGATION, learningProblem);

      String outputFilePath = systemConfiguration.getOutputFilePath();
      assertEquals(String.format(ASSERT_TRUE_OUTPUT_FILE_NAME, OUTPUT_PATH_TXT), OUTPUT_PATH_TXT, outputFilePath);
   }


   /**
    * Tests a correct JSON input File with mandatory fields only.
    */
   @Test
   public void testMinimumInputForSystemConfigurationCommand() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_MINIMUM_INPUT_JSON_PATH), true, true);
   }


   /**
    * Tests a empty JSON input File.
    * 
    */
   @Test
   public void testEmptyInputForSystemConfigurationCommand() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_EMPTY_INPUT_JSON_PATH), false, false);
   }


   /**
    * In the JSON file the 'absolute_path' is not set correctly.
    */
   @Test(expected = AssertionError.class)
   public void testIncorrectInputForSystemConfigurationCommand() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_INCORRECT_INPUT_JSON_PATH), true, true);
   }


   /**
    * In the JSON file the 'learning_problem' is not set correctly.
    */
   @Test(expected = AssertionError.class)
   public void testIncorrectInputForSystemConfigurationCommand2() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_INCORRECT_INPUT_JSON_PATH_2), true, true);
   }


   /**
    * In the JSON file the 'algorithms' field is not provided.
    */
   public void testIncompleteInputForSystemConfigurationCommand() {
      executeReadSystemConfigurationCommand(getTestRessourcePathFor(SYSTEM_CONFIGURATION_INCOMPLETE_INPUT_JSON_PATH), true, true);
   }


   /**
    * Executes the {@code ReadSystemConfigurationCommand} with the provided file path and checks
    * whether it is executable or not.
    *
    * @param filePath the file path to the system configuration
    * @param assertTrueCanBeExecuted the assert true can be executed
    * @param expectedValueCanBeExecuted the expected value can be executed
    */
   private void executeReadSystemConfigurationCommand(String filePath, boolean assertTrueCanBeExecuted,
         boolean expectedValueCanBeExecuted) {


      SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();

      ReadSystemConfigurationCommandHandler readSystemConfigurationCommandHandlerTest = new ReadSystemConfigurationCommandHandler();
      ReadSystemConfigurationCommandConfiguration readSystemConfigurationCommandConfiguration = new ReadSystemConfigurationCommandConfiguration();
      readSystemConfigurationCommandConfiguration.setFilePath(filePath);

      ICommand handleUserCommand = readSystemConfigurationCommandHandlerTest.handleUserCommand(readSystemConfigurationCommandConfiguration);

      boolean canBeExecuted = handleUserCommand.canBeExecuted();
      assertEquals(String.format(ASSERT_EQUALS_EXECUTEABLE, canBeExecuted), assertTrueCanBeExecuted, canBeExecuted);

      CommandResult commandResult = handleUserCommand.executeCommand();
      boolean executedSuccessfully = commandResult.isExecutedSuccessfully();
      assertEquals(String.format(ASSERT_EQUALS_EXECUTE_SUCCESSFUL, executedSuccessfully), expectedValueCanBeExecuted, executedSuccessfully);

      readSystemConfigurationCommandHandlerTest.interpretCommandResult(handleUserCommand, commandResult);

      String failureReason = handleUserCommand.getFailureReason();
      boolean isFailureReasonEmpty = failureReason.isEmpty();

      assertEquals(String.format(ASSERT_EQUALS_EMPTY_FAILURE_REASON, failureReason), expectedValueCanBeExecuted, isFailureReasonEmpty);
   }
}
