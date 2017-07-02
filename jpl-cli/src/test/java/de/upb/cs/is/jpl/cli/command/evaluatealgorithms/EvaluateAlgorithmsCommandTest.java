package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.evaluation.AEvaluationConfiguration;
import de.upb.cs.is.jpl.api.evaluation.EEvaluation;
import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.FMeasureConfiguration;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;
import de.upb.cs.is.jpl.cli.exception.command.NullException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.CannotCreateEvaluationForLearningProblemException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.CouldNotSetupEvaluationException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.DatasetsOrLearningAlgorithmsNotSetForEvaluationException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.EvaluationDoesnotExistForLearningProblemException;
import de.upb.cs.is.jpl.cli.exception.command.evaluatealgorithmscommand.UnknownEvaluationIdentifierException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link EvaluateAlgorithmsCommand}, using inputControl and consoleOutput.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluateAlgorithmsCommandTest extends ACommandUnitTest {
   private final static Logger logger = LoggerFactory.getLogger(EvaluateAlgorithmsCommandTest.class);
   private static final String SYSTEM_CONFIG_JSON_FILE = "systemConfigComplete.json";
   private static final String SYSTEM_CONFIG_INCOMPLETE_JSON_FILE = "systemConfigCompleteIncomplete.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_NULL_EVALUATION = "systemConfigCompleteWillReturnNullEvaluation.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_WRONG_PARAMETERS = "systemConfigCompleteParametersInvalid.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_WRONG_PARAMETERS_FORMETRICS = "systemConfigCompleteParametersInvalidForMetric.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_UNKNOWN_EVALUATION_IDENTIFIER = "systemConfigCompleteWithUnknownEvaluationIdentifier.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_NO_EVALUATION = "systemConfigCompleteWillHaveNoEvaluation.json";
   private static final String SYSTEM_CONFIG_COMPLETE_WITH_SETUP_PROBLEM_IN_EVALUATION = "systemConfigCompleteWithCouldNotSetupEvaluation.json";


   private static final String EVALUATIONS_CONSOLE_OUTPUT = "Evaluation Command console output: %s";
   private static final String RESOURCE_DIRECTORY_LEVEL = "evaluatealgorithms" + File.separator;
   private static final String PARAMETER_KEY_FILEPATH_SHORT = "-p=";

   private static final String RANK_AGGREGATION_EVALUATION_WRONG_VALUE = "spearman_correlation21";
   private static final String INCORRECT_EVALUATION_IDENTIFIER = "supplied_testsetyxa";
   private static final String EVALUATE_ALGORITHM_IDENTIFIER_COMMAND_LINE = "--evaluation_identifier=%s";
   private static final String METRICS_IDENTIFIER_COMMAND_LINE = "--metrics= %s,%s";
   private static final String REFLECTION_COMMAND_POSITIVE_TEST = "SUCCESSFULL_EXECUTION_COMMAND_RESULT";
   private static final String REFLECTION_COMMAND_CANNOTBE_EXECUTED = "COMMAND_CANNOT_BE_EXECUTED_ERROR_MESSAGE";
   private static final String EVALUATION_IDENTIFER_NOT_SET_REFLECTION = "EVALUATION_IDENTIFIER_NOTSET_ERROR_MESSAGE";
   private static final String REFLECTION_CANNOT_CREATE_EVALUATION_FOR_LEARNING_PROBLEM = "CANNOT_CREATE_EVALUATION_FORLEARNINGPROBLEM_ERROR_MESSAGE";
   private static final String REFLECTION_VALIDATION_EVALUATION_METRIC = "VALIDATION_EVALUATIONMETRICS_ERROR_MESSAGE";
   private static final String REFLECTION_TYPE_VALUE_INVALID = "ERROR_NO_NEGATIVE_BETA_VALUES";
   private static final String REFLECTION_EVALUATION_IDENTIFIER_UNKNOWN_ERROR_MESSAGE = "EVALUATION_IDENTIFIER_UNKNOWN_ERROR_MESSAGE";
   private static final String REFLECTION_EVALUATION_FOR_LEARNINGPROBLEM_NOT_EXIST_ERROR_MESSAGE = "EVALUATION_FOR_LEARNINGPROBLEM_NOT_EXIST_ERROR_MESSAGE";
   private static final String REFLECTION_LEARNING_ALGORITHMS_ERROR_MESSAGE = "LEARNING_ALGORITHMS_ERROR_MESSAGE";
   private static final String REFLECTION_COULDNOT_SETUP_EVALUATION_ERROR_MESSAGE = "COULDNOT_SETUP_EVALUATION_ERROR_MESSAGE";
   private static SystemConfiguration systemConfiguration;
   private String[] currentCommand;


   /**
    * Creates a new unit test for the {@link DetermineApplicableAlgorithmsCommand} without any
    * additional path to the resources.
    */
   public EvaluateAlgorithmsCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * This method is run before all the test run. It reset the system configuration, initializes
    * inputControl and setup logging for tests.
    * 
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    */
   @Before
   public void setupTests() throws SecurityException, IllegalArgumentException {
      super.setupTest();
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();
   }


   /**
    * Running all other commands required for evaluate algorithms command.
    */
   private void runSystemConfigCommandWithFile(String filePath) {
      currentCommand = new String[] { ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier(), PARAMETER_KEY_FILEPATH_SHORT + filePath };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.TRAIN_MODELS.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
   }


   /**
    * Running all other commands required for evaluate algorithms command without loading the
    * algorithms.
    */
   private void runSystemConfigCommandWithFileWithoutLoadingAlgorithms(String filePath) {
      currentCommand = new String[] { ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier(), PARAMETER_KEY_FILEPATH_SHORT + filePath };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
   }


   /**
    * Running all other commands required for evaluate algorithms command without training the
    * algorithms.
    */
   private void runSystemConfigCommandWithFileWithoutTrainingModels(String filePath) {
      currentCommand = new String[] { ECommand.READ_SYSTEM_CONFIGURATION.getCommandIdentifier(), PARAMETER_KEY_FILEPATH_SHORT + filePath };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
      currentCommand = new String[] { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      TestUtils.simulateCommandLineInput(currentCommand);
   }


   /**
    * Positive Test for {@link EvaluateAlgorithmsCommand}.
    */
   @Test
   public void testEvaluateAlgorithmCommand() {

      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_JSON_FILE));
      EvaluateAlgorithmsCommandHandler commandHandler = new EvaluateAlgorithmsCommandHandler();
      EvaluateAlgorithmsCommandConfiguration commandConfiguration = new EvaluateAlgorithmsCommandConfiguration();
      EvaluateAlgorithmsCommand handleUserCommand = (EvaluateAlgorithmsCommand) commandHandler.handleUserCommand(commandConfiguration);
      boolean canBeExecuted = handleUserCommand.canBeExecuted();
      assertTrue(canBeExecuted);
      CommandResult commandResult = handleUserCommand.executeCommand();
      boolean executedSuccessfully = false;
      if (commandResult != null)
         executedSuccessfully = commandResult.isExecutedSuccessfully();
      assertTrue(executedSuccessfully);
      String failureReason = handleUserCommand.getFailureReason();
      boolean empty = failureReason.isEmpty();
      assertTrue(empty);
   }


   /**
    * Positive Test for {@link EvaluateAlgorithmsCommand} using inputControl using command line.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithCommandLineCompleteInput()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String correctResult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_COMMAND_POSITIVE_TEST),
            EEvaluation.SUPPLIED_TEST_SET_RANK_AGGREGATION.getEvaluationIdentifier(),
            ELearningProblem.RANK_AGGREGATION.getLearningProblemIdentifier());
      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_JSON_FILE));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier(),
            String.format(EVALUATE_ALGORITHM_IDENTIFIER_COMMAND_LINE, EvaluationsKeyValuePairs.EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER),
            String.format(METRICS_IDENTIFIER_COMMAND_LINE, EMetric.KENDALLS_TAU.getMetricIdentifier(),
                  EMetric.SPEARMANS_RANK_CORRELATION.getMetricIdentifier()) };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(commandResult.isExecutedSuccessfully());
      assertTrue(command.getFailureReason().isEmpty());
      assertTrue(commandResult.getException() instanceof NullException);
      assertTrue(commandResult.getResult().toString().equals(correctResult));
   }


   /**
    * Positive Test for {@link EvaluateAlgorithmsCommand} using inputControl using
    * SystemConfiguration.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigCompleteInput()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String correctResult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_COMMAND_POSITIVE_TEST),
            EEvaluation.SUPPLIED_TEST_SET_RANK_AGGREGATION.getEvaluationIdentifier(),
            ELearningProblem.RANK_AGGREGATION.getLearningProblemIdentifier());
      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_JSON_FILE));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(commandResult.isExecutedSuccessfully());
      assertTrue(command.getFailureReason().isEmpty());
      assertTrue(commandResult.getException() instanceof NullException);
      assertTrue(commandResult.getResult().toString().equals(correctResult));

   }


   /**
    * Test with incomplete systemConfig file.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigInCompleteInput()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String commandError = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, EVALUATION_IDENTIFER_NOT_SET_REFLECTION),
            ELearningProblem.RANK_AGGREGATION.getLearningProblemIdentifier());
      String inCorrectresult = String
            .format(TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_COMMAND_CANNOTBE_EXECUTED), commandError);
      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_INCOMPLETE_JSON_FILE));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertFalse(command.getFailureReason().isEmpty());
      assertFalse(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, CommandCannotBeExecutedException.class);
      assertTrue(command.getFailureReason().equals(inCorrectresult));
   }


   /**
    * Test with systemConfigFile which creates an evaluation which is not possible.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigNullEvalaution()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String inCorrectresult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_CANNOT_CREATE_EVALUATION_FOR_LEARNING_PROBLEM),
            EEvaluation.CROSS_VALIDATION_DATASET_RANK_AGGREGATION.getEvaluationIdentifier(),
            ELearningProblem.RANK_AGGREGATION.getLearningProblemIdentifier());
      runSystemConfigCommandWithFileWithoutTrainingModels(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_NULL_EVALUATION));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, CannotCreateEvaluationForLearningProblemException.class);
      assertTrue(commandResult.getException().getMessage().equals(inCorrectresult));
   }


   /**
    * Test with systemConfigFile which has wrong parameters for evaluation.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigWrongParameters()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String inCorrectresult = String.format(
            TestUtils.getStringByReflection(AEvaluationConfiguration.class, REFLECTION_VALIDATION_EVALUATION_METRIC),
            RANK_AGGREGATION_EVALUATION_WRONG_VALUE);
      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_WRONG_PARAMETERS));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, ParameterValidationFailedException.class);
      assertTrue(commandResult.getException().getMessage().contains(inCorrectresult));

   }


   /**
    * Test with systemConfigFile which has wrong parameters for evaluation metric.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigWrongParametersForMetrics()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String invalidValue = TestUtils.getStringByReflection(FMeasureConfiguration.class, REFLECTION_TYPE_VALUE_INVALID);
      runSystemConfigCommandWithFileWithoutTrainingModels(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_WRONG_PARAMETERS_FORMETRICS));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, ParameterValidationFailedException.class);
      assertTrue(commandResult.getException().getMessage().contains(invalidValue));


   }


   /**
    * Test with systemConfigFile which has wrong evaluation identifier.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigWithUnknownEvaluationIdentifier()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {

      String inCorrectresult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_EVALUATION_IDENTIFIER_UNKNOWN_ERROR_MESSAGE),
            INCORRECT_EVALUATION_IDENTIFIER);
      runSystemConfigCommandWithFile(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_UNKNOWN_EVALUATION_IDENTIFIER));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, UnknownEvaluationIdentifierException.class);
      assertTrue(commandResult.getException().getMessage().equals(inCorrectresult));
   }


   /**
    * Test with systemConfigFile which get an evaluation which does not exist for given problem.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigHaveNoEvaluation()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String inCorrectresult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class,
                  REFLECTION_EVALUATION_FOR_LEARNINGPROBLEM_NOT_EXIST_ERROR_MESSAGE),
            EvaluationsKeyValuePairs.EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER,
            ELearningProblem.RANK_AGGREGATION.getLearningProblemIdentifier());
      runSystemConfigCommandWithFileWithoutTrainingModels(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_NO_EVALUATION));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, EvaluationDoesnotExistForLearningProblemException.class);
      assertTrue(commandResult.getException().getMessage().equals(inCorrectresult));
   }


   /**
    * Test with systemConfigFile which get an evaluation but learning algorithms are not set.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigWithNoAlgorithmsSet()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      runSystemConfigCommandWithFileWithoutLoadingAlgorithms(getTestRessourcePathFor(SYSTEM_CONFIG_JSON_FILE));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, DatasetsOrLearningAlgorithmsNotSetForEvaluationException.class);
      String inCorrectresult = String.format(
            StringUtils.LINE_BREAK
                  + TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_LEARNING_ALGORITHMS_ERROR_MESSAGE),
            EEvaluation.SUPPLIED_TEST_SET_RANK_AGGREGATION.getEvaluationIdentifier());
      assertTrue(commandResult.getException().getMessage().equals(inCorrectresult));
   }


   /**
    * Test with systemConfigFile where evaluation cannot be setup.
    * 
    * @throws NoSuchFieldException if the reflection did not work
    * @throws SecurityException if the reflection did not work
    * @throws IllegalArgumentException if the reflection did not work
    * @throws IllegalAccessException if the reflection did not work
    */
   @Test
   public void testEvaluateAlgorithmsCommandWithSystemConfigWithNoSetupForEvaluation()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      runSystemConfigCommandWithFileWithoutTrainingModels(getTestRessourcePathFor(SYSTEM_CONFIG_COMPLETE_WITH_SETUP_PROBLEM_IN_EVALUATION));
      currentCommand = new String[] { ECommand.EVALUATE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(currentCommand);
      logger.debug(String.format(EVALUATIONS_CONSOLE_OUTPUT, consoleOutput));
      Pair<ICommand, CommandResult> commandAndResult = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      ICommand command = commandAndResult.getFirst();
      CommandResult commandResult = commandAndResult.getSecond();
      assertTrue(commandResult != null);
      assertTrue(command.canBeExecuted());
      TestUtils.assertCorrectFailedEmptyCommandResult(commandResult, CouldNotSetupEvaluationException.class);
      String inCorrectresult = String.format(
            TestUtils.getStringByReflection(EvaluateAlgorithmsCommand.class, REFLECTION_COULDNOT_SETUP_EVALUATION_ERROR_MESSAGE),
            EvaluationsKeyValuePairs.EVALUATION_USE_TRAINING_DATASET_IDENTIFIER);
      assertTrue(commandResult.getException().getMessage().equals(inCorrectresult));
   }
}
