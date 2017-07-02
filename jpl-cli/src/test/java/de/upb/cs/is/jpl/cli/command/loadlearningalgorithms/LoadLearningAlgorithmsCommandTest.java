package de.upb.cs.is.jpl.cli.command.loadlearningalgorithms;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.IOUtils;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.addlearningalgorithm.AddLearningAlgorithmFromJsonCommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.loadlearningalgorithms.LoadLearningAlgorithmsFailedException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link LoadLearningAlgorithmsCommand}.
 *
 * @author Tanja Tornede
 */
public class LoadLearningAlgorithmsCommandTest extends ACommandUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "loadlearningalgorithms" + File.separator;

   private static final String ERROR_EMPTY_LIST_OF_COMMAND_RESULTS = "The list of the command results of is empty, but should not be.";

   private static final String REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN = "ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN";
   private static final String REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_AMOUNT_OF_FAILED_ALGORITHMS = "AMOUNT_OF_FAILED_ALGORITHMS";
   private static final String REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_ADDED_ALGORITHM = "ADDED_ALGORITHM";
   private static final String REFLECTION_LOADLEARNINGALGORITHMSCOMMAND_ERROR_FAILED_TO_LOAD_ANY_ALGORITHM = "ERROR_FAILED_TO_LOAD_ANY_ALGORITHM";
   private static final String REFLECTION_PRANKALGORITHMCONFIGURATION_WRONG_VALUE_FOR_K = "WRONG_VALUE_FOR_K";

   private static final String CORRECT_JSON_FILE = "correctJsonFile.json";
   private static final String ONE_INCORRECT_DEFINED_PARAMETERS_JSON_FILE = "oneIncorrectDefinedParametersJsonFile.json";
   private static final String INCORRECT_DEFINED_PARAMETERS_JSON_FILE = "incorrectDefinedParametersJsonFile.json";

   private static final String JSON_KEY_NAME = "name";

   private SystemConfiguration systemConfiguration;


   /**
    * Creates a new unit test for the {@link LoadLearningAlgorithmsCommand} without any additional
    * path to the resources.
    */
   public LoadLearningAlgorithmsCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Initializes and resets the system configuration.
    */
   @Override
   @Before
   public void setupTest() {
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();
   }


   /**
    * Tests if two correctly defined algorithms can be added to the system configuration.
    * 
    * @throws IOException if reading the json file did not work
    * @throws JsonParsingFailedException if creating a JSON object did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   @Test
   public void testAddingMultipleCorrectlyDefinedAlgorithmsToSystemConfiguration()
         throws IOException,
            JsonParsingFailedException,
            NoSuchFieldException,
            IllegalAccessException {

      String jsonString = IOUtils.readStringFromFile(getTestRessourcePathFor(CORRECT_JSON_FILE));
      JsonArray createJsonArrayFromString = JsonUtils.createJsonArrayFromString(jsonString);
      systemConfiguration.setJsonAlgorithmConfiguration(createJsonArrayFromString);

      String[] command = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);

      assertCorrectLearningAlgorithmsSetInSystemConfiguration(jsonString, 2);
      assertCorrectCommandResultAndConsoleOutputForMultipleCorrectlyDefinedAlgorithms(consoleOutput);
   }


   /**
    * Checks if the algorithms set in the system configuration are the ones defined in the given
    * JSON string.
    * 
    * @param jsonString the json string defining the algorithm configuration.
    * @param amountOfAlgorithms the expected amount of algorithms in the system configuration.
    * @throws JsonParsingFailedException if creating the json object did not work
    */
   private void assertCorrectLearningAlgorithmsSetInSystemConfiguration(String jsonString, int amountOfAlgorithms)
         throws JsonParsingFailedException {
      Assert.assertEquals(amountOfAlgorithms, systemConfiguration.getLearningAlgorithms().size());
      JsonArray jsonArray = JsonUtils.createJsonArrayFromString(jsonString);

      List<ILearningAlgorithm> learningAlgorithmsInSystemConfiguration = systemConfiguration.getLearningAlgorithms();
      for (int i = 0; i < learningAlgorithmsInSystemConfiguration.size(); i++) {
         ILearningAlgorithm learningAlgorithm = learningAlgorithmsInSystemConfiguration.get(i);
         String definedLearningAlgorithmName = JsonUtils.createJsonObjectFromString(jsonArray.get(i).toString()).get(JSON_KEY_NAME)
               .getAsString();
         Assert.assertEquals(definedLearningAlgorithmName, getIdentifierOfLearningAlgorithm(learningAlgorithm));
      }
   }


   /**
    * Returns the identifier of the given {@link ILearningAlgorithm}.
    * 
    * @param learningAlgorithm the learning algorithm to get the identifier from
    * @return the identifier of the given learning algorithm, empty string if it was not found
    */
   private String getIdentifierOfLearningAlgorithm(ILearningAlgorithm learningAlgorithm) {
      for (ELearningAlgorithm eLearningAlgorithm : ELearningAlgorithm.getELearningAlgorithms()) {
         if (learningAlgorithm.getClass().isInstance(eLearningAlgorithm.createLearningAlgorithm())) {
            return eLearningAlgorithm.getIdentifier();
         }
      }
      return StringUtils.EMPTY_STRING;
   }


   /**
    * Checks if the command result and the console output are correct.
    * 
    * @param consoleOutput the redirected console output
    * @throws NoSuchFieldException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   private void assertCorrectCommandResultAndConsoleOutputForMultipleCorrectlyDefinedAlgorithms(String consoleOutput)
         throws NoSuchFieldException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), LoadLearningAlgorithmsCommand.class);
      TestUtils.assertCorrectSuccessfulCommandResult(latestPairInHistory.getSecond());

      @SuppressWarnings("unchecked")
      List<CommandResult> addAlgorithmCommandResultList = ((List<CommandResult>) latestPairInHistory.getSecond().getResult());

      assertCorrectListOfAddedAlgorithmsForMultipleCorrectlyDefinedAlgorithms(addAlgorithmCommandResultList);
      assertCorrectConsoleOutputForMultipleCorrectlyDefinedAlgorithms(consoleOutput, addAlgorithmCommandResultList);
   }


   /**
    * Checks if the {@link CommandResult}'s of the given list are set correctly for a
    * {@link AddLearningAlgorithmFromJsonCommand}.
    * 
    * @param addAlgorithmCommandResultList the list of {@link CommandResult}'s to check
    */
   private void assertCorrectListOfAddedAlgorithmsForMultipleCorrectlyDefinedAlgorithms(List<CommandResult> addAlgorithmCommandResultList) {
      if (addAlgorithmCommandResultList.isEmpty()) {
         Assert.fail(ERROR_EMPTY_LIST_OF_COMMAND_RESULTS);
      }
      Assert.assertTrue(addAlgorithmCommandResultList.size() == 2);
      TestUtils.assertCorrectSuccessfulListOfCommandResultsWhichAreNotEmpty(addAlgorithmCommandResultList, ILearningAlgorithm.class);

   }


   /**
    * Checks if the console output is correct.
    * 
    * @param consoleOutput the redirected console output
    * @param addAlgorithmCommandResultList the list of {@link CommandResult}'s to check
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   private void assertCorrectConsoleOutputForMultipleCorrectlyDefinedAlgorithms(String consoleOutput,
         List<CommandResult> addAlgorithmCommandResultList)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_ADDED_ALGORITHM = TestUtils.getStringByReflection(LoadLearningAlgorithmsCommandHandler.class,
            REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_ADDED_ALGORITHM);

      StringBuilder builder = new StringBuilder();
      builder.append(TestUtils.getInfoStartingCommandExecution(ECommand.LOAD_LEARNING_ALGORITHMS));

      for (CommandResult addingLearningAlgorithmCommandResult : addAlgorithmCommandResultList) {
         builder.append(String.format(reflectionString_ADDED_ALGORITHM, addingLearningAlgorithmCommandResult.getResult().toString()));
      }
      Assert.assertEquals(builder.toString().trim(), consoleOutput.trim());
   }


   /**
    * Checks if no algorithms are set in the system configuration.
    */
   private void assertNoLearningAlgorithmsSetInSystemConfiguration() {
      Assert.assertEquals(0, systemConfiguration.getLearningAlgorithms().size());
   }


   /**
    * Tests if the {@link LoadLearningAlgorithmsCommand} is not executed due to incorrectly defined
    * parameters.
    * 
    * @throws IOException if reading the json file did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    * @throws JsonParsingFailedException if json can't be parsed
    */
   @Test
   public void testAddingLearningAlgorithmsWithWrongParametersIsForbidden()
         throws IOException,
            NoSuchFieldException,
            IllegalAccessException,
            JsonParsingFailedException {

      String jsonString = IOUtils.readStringFromFile(getTestRessourcePathFor(INCORRECT_DEFINED_PARAMETERS_JSON_FILE));
      JsonArray createJsonArrayFromString = JsonUtils.createJsonArrayFromString(jsonString);
      systemConfiguration.setJsonAlgorithmConfiguration(createJsonArrayFromString);

      String[] command = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);

      assertNoLearningAlgorithmsSetInSystemConfiguration();
      assertCorrectFailedCommandResultAndConsoleOutputForAddingLearningAlgorithmsWithWrongParameters(consoleOutput);

   }


   /**
    * Checks if the given {@link CommandResult} and the given redirected console output is correct.
    * 
    * @param consoleOutput the redirected console output to check
    * @throws IllegalAccessException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws NoSuchFieldException if the reflection did not work
    */
   private void assertCorrectFailedCommandResultAndConsoleOutputForAddingLearningAlgorithmsWithWrongParameters(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), LoadLearningAlgorithmsCommand.class);
      TestUtils.assertCorrectFailedEmptyCommandResultHavingResult(latestPairInHistory.getSecond(),
            LoadLearningAlgorithmsFailedException.class);

      assertCorrectConsoleOutputForAddingLearningAlgorithmsWithWrongParameters(consoleOutput);
   }


   /**
    * Checks if the console output for adding learning algorithms with wrong parameters is as
    * expected.
    * 
    * @param consoleOutput the redirected console output
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectConsoleOutputForAddingLearningAlgorithmsWithWrongParameters(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_ERROR_FAILED_TO_LOAD_ANY_ALGORITHM = TestUtils.getStringByReflection(LoadLearningAlgorithmsCommand.class,
            REFLECTION_LOADLEARNINGALGORITHMSCOMMAND_ERROR_FAILED_TO_LOAD_ANY_ALGORITHM);

      String reflectionString_WRONG_VALUE_FOR_K = TestUtils.getStringByReflection(PerceptronRankConfiguration.class,
            REFLECTION_PRANKALGORITHMCONFIGURATION_WRONG_VALUE_FOR_K);

      StringBuilder builder = new StringBuilder();
      builder.append(TestUtils.getInfoStartingCommandExecution(ECommand.LOAD_LEARNING_ALGORITHMS));
      builder.append(String.format(reflectionString_ERROR_FAILED_TO_LOAD_ANY_ALGORITHM));
      builder.append(reflectionString_WRONG_VALUE_FOR_K);
      Assert.assertEquals(builder.toString().trim(), consoleOutput.trim());
   }


   /**
    * Tests if the {@link LoadLearningAlgorithmsCommand} is not executed due to incorrectly defined
    * parameters, but the one with correctly defined parameters is executed.
    * 
    * @throws IOException if reading the json file did not work
    * @throws JsonParsingFailedException if creating a json object did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws IllegalArgumentException if accessing the command history did not work
    * @throws SecurityException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    */
   @Test
   public void testAddingLearningAlgorithmsWithCorrectAndWrongParameters()
         throws IOException,
            IllegalAccessException,
            JsonParsingFailedException,
            NoSuchFieldException,
            SecurityException,
            IllegalArgumentException {

      String jsonString = IOUtils.readStringFromFile(getTestRessourcePathFor(ONE_INCORRECT_DEFINED_PARAMETERS_JSON_FILE));
      JsonArray createJsonArrayFromString = JsonUtils.createJsonArrayFromString(jsonString);
      systemConfiguration.setJsonAlgorithmConfiguration(createJsonArrayFromString);

      String[] command = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);

      assertCorrectLearningAlgorithmsSetInSystemConfiguration(jsonString, 1);
      assertCorrectCommandResultAndConsoleOutput(consoleOutput);
   }


   /**
    * Checks if the {@link CommandResult} and the console output is correct.
    * 
    * @param consoleOutput the redirected console output
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectCommandResultAndConsoleOutput(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), LoadLearningAlgorithmsCommand.class);
      TestUtils.assertCorrectSuccessfulCommandResult(latestPairInHistory.getSecond());

      @SuppressWarnings("unchecked")
      List<CommandResult> addAlgorithmCommandResultList = (List<CommandResult>) latestPairInHistory.getSecond().getResult();
      if (addAlgorithmCommandResultList.isEmpty()) {
         Assert.fail(ERROR_EMPTY_LIST_OF_COMMAND_RESULTS);
      }

      TestUtils.assertCorrectFailedEmptyCommandResult(addAlgorithmCommandResultList.get(0), ParameterValidationFailedException.class);
      TestUtils.assertCorrectSuccessfulCommandResultWhichIsNotEmpty(addAlgorithmCommandResultList.get(1), ILearningAlgorithm.class);

      assertCorrectFailedCommandResultAndConsoleOutputForAddingLearningAlgorithmsWithCorrectAndWrongParameters(consoleOutput,
            addAlgorithmCommandResultList);
   }


   /**
    * Check if the {@link CommandResult} is one of a failed command and if the console output is as
    * expected.
    * 
    * @param consoleOutput the redirected console output
    * @param addAlgorithmCommandResultList the list of {@link CommandResult} to check for the
    *           console output
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectFailedCommandResultAndConsoleOutputForAddingLearningAlgorithmsWithCorrectAndWrongParameters(
         String consoleOutput, List<CommandResult> addAlgorithmCommandResultList)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_ADDED_ALGORITHM = TestUtils.getStringByReflection(LoadLearningAlgorithmsCommandHandler.class,
            REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_ADDED_ALGORITHM);
      String reflectionString_AMOUNT_OF_FAILED_ALGORITHMS = TestUtils.getStringByReflection(LoadLearningAlgorithmsCommandHandler.class,
            REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_AMOUNT_OF_FAILED_ALGORITHMS);
      String reflectionString_ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN = TestUtils.getStringByReflection(
            LoadLearningAlgorithmsCommandHandler.class,
            REFLECTION_LOADLEARNINGALGORITHMSCOMMANDHANDLER_ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN);

      ILearningAlgorithm learningAlgorithm = ((ILearningAlgorithm) addAlgorithmCommandResultList.get(1).getResult());
      StringBuilder builder = new StringBuilder();
      builder.append(TestUtils.getInfoStartingCommandExecution(ECommand.LOAD_LEARNING_ALGORITHMS));
      builder.append(String.format(reflectionString_ADDED_ALGORITHM, learningAlgorithm.toString()));
      builder.append(String.format(reflectionString_AMOUNT_OF_FAILED_ALGORITHMS, 1));
      builder.append(StringUtils.LINE_BREAK);
      builder.append(reflectionString_ERROR_ADDED_LEARNING_ALGORITHM_FAILED_REASON_UNKNOWN);


      Assert.assertEquals(builder.toString().trim(), consoleOutput.trim());
   }

}
