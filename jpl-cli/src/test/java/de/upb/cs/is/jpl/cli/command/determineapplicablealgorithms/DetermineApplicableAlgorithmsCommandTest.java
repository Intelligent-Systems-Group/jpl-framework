package de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms;


import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDatasetParser;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDatasetParser;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.command.ICommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.CommandCannotBeExecutedException;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link DetermineApplicableAlgorithmsCommand}.
 *
 * @author Tanja Tornede
 */
public class DetermineApplicableAlgorithmsCommandTest extends ACommandUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "determineapplicablealgorithms" + File.separator;

   private static final String ERROR_ALGORITHM_NOT_USES_DATASET_BUT_IS_APPLICABLE = "The learning algorithm %s does not use the %s but is as applicable algorithm in the list of applicable algorithms of the result of the command result.";

   private static final String REFLECTION_DETERMINEAPPLICABLEALGORITHMSCOMMANDHANDLER_ALGORITHM_RUNS_ON = "ALGORITHM_RUNS_ON";
   private static final String REFLECTION_DETERMINEAPPLICABLEALGORITHMSCOMMAND_ERROR_NO_DATASET_IN_CONFIGURATION = "ERROR_NO_DATASET_IN_CONFIGURATION";

   private static final String COMMAND_DATASET = "--dataset=";
   private static final String FILE_RELATIVE_DATASET = "relativeDataset.gprf";

   private SystemConfiguration systemConfiguration;


   /**
    * Creates a new unit test for the {@link DetermineApplicableAlgorithmsCommand} without any
    * additional path to the resources.
    */
   public DetermineApplicableAlgorithmsCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Resets and initializes the system configuration.
    */
   @Override
   @Before
   public void setupTest() {
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();
   }


   /**
    * Tests if a relative dataset can only be applied by the algorithms which are using relative
    * dataset parser.
    * 
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   @Test
   public void testDetermineApplicableAlgorithmsWithDefaultRelativeDataset()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      addDefaultDatasetToSystemConfiguration();

      String[] command = { ECommand.DETERMINE_APPLICABLE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);

      assertCorrectCommandResultAndCorrectConsoleOutput(consoleOutput);
   }


   /**
    * Adds an relative dataset to the system configuration.
    */
   private void addDefaultDatasetToSystemConfiguration() {
      String[] command = { ECommand.ADD_DATASET.getCommandIdentifier(), COMMAND_DATASET + getTestRessourcePathFor(FILE_RELATIVE_DATASET) };
      TestUtils.simulateCommandLineInput(command);
   }


   /**
    * Checks if the latest command in the command history of the system status is a
    * {@link DetermineApplicableAlgorithmsCommand} and if the according command result is a map
    * containing the above added dataset as key and a list containing all algorithms using relative
    * datasets as value, if the exception is not set and if the additional information are empty.
    * 
    * @param consoleOutput the redirected console output
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertCorrectCommandResultAndCorrectConsoleOutput(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), DetermineApplicableAlgorithmsCommand.class);
      TestUtils.assertCorrectSuccessfulCommandResult(latestPairInHistory.getSecond());

      @SuppressWarnings("unchecked")
      Map<DatasetFile, List<ILearningAlgorithm>> datasetFileToLearningAlgorithmsMap = ((Map<DatasetFile, List<ILearningAlgorithm>>) latestPairInHistory
            .getSecond().getResult());
      assertCorrectEntriesInDatasetFileToLearningAlgorithmsMap(datasetFileToLearningAlgorithmsMap);
      assertCorrectConsoleOutput(consoleOutput, datasetFileToLearningAlgorithmsMap);
   }


   /**
    * Checks if the given map contains all {@link ILearningAlgorithm}'s which are using relative
    * datasets.
    * 
    * @param datasetFileToLearningAlgorithmsMap the map resulting after execution of the
    *           {@link DetermineApplicableAlgorithmsCommand}
    */
   private void assertCorrectEntriesInDatasetFileToLearningAlgorithmsMap(
         Map<DatasetFile, List<ILearningAlgorithm>> datasetFileToLearningAlgorithmsMap) {
      Assert.assertEquals(1, datasetFileToLearningAlgorithmsMap.size());

      Set<String> allLearningAlgorithmsForRelativeDataset = getAllLearningAlgorithmClassNamesUsingRelativeDataset();

      for (Entry<DatasetFile, List<ILearningAlgorithm>> entry : datasetFileToLearningAlgorithmsMap.entrySet()) {
         Assert.assertEquals(allLearningAlgorithmsForRelativeDataset.size(), entry.getValue().size());
         for (ILearningAlgorithm learningAlgorithm : entry.getValue()) {
            String learningAlgorithmName = learningAlgorithm.toString();
            if (allLearningAlgorithmsForRelativeDataset.contains(learningAlgorithmName)) {
               allLearningAlgorithmsForRelativeDataset.remove(learningAlgorithmName);
            } else {
               Assert.fail(String.format(ERROR_ALGORITHM_NOT_USES_DATASET_BUT_IS_APPLICABLE, learningAlgorithmName,
                     DefaultDataset.class.getSimpleName()));
            }
         }
      }
   }


   /**
    * Returns a set of all {@link ILearningAlgorithm}'s which are using relative datasets. Note that
    * the list contains the class names of the according {@link ILearningAlgorithm}'s.
    * 
    * @return a set of class names of all {@link ILearningAlgorithm} using relative datasets
    */
   private Set<String> getAllLearningAlgorithmClassNamesUsingRelativeDataset() {
      Set<String> learningAlgorithmsSet = new HashSet<>();
      for (ELearningAlgorithm eLearningAlgorithm : ELearningAlgorithm.getELearningAlgorithms()) {
         ILearningAlgorithm learningAlgorithm = eLearningAlgorithm.createLearningAlgorithm();
         if (learningAlgorithm.getDatasetParser() instanceof DefaultRelativeDatasetParser
               || learningAlgorithm.getDatasetParser() instanceof ObjectRankingDatasetParser) {
            learningAlgorithmsSet.add(learningAlgorithm.toString());
         }
      }
      return learningAlgorithmsSet;
   }


   /**
    * Checks if the console output contains a line for every dataset file and learning algorithm
    * pair of the given map and if the output for every pair is as defined in the
    * {@link DetermineApplicableAlgorithmsCommandHandler}.
    * 
    * @param consoleOutput the redirected Console output
    * @param datasetFileToLearningAlgorithmsMap the map resulting after execution of the
    *           {@link DetermineApplicableAlgorithmsCommand}
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    */
   private void assertCorrectConsoleOutput(String consoleOutput,
         Map<DatasetFile, List<ILearningAlgorithm>> datasetFileToLearningAlgorithmsMap)
         throws IllegalArgumentException,
            IllegalAccessException,
            NoSuchFieldException,
            SecurityException {
      String reflectionString_ALGORITHM_RUNS_ON = TestUtils.getStringByReflection(DetermineApplicableAlgorithmsCommandHandler.class,
            REFLECTION_DETERMINEAPPLICABLEALGORITHMSCOMMANDHANDLER_ALGORITHM_RUNS_ON);

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(TestUtils.getInfoStartingCommandExecution(ECommand.DETERMINE_APPLICABLE_ALGORITHMS));
      for (Entry<DatasetFile, List<ILearningAlgorithm>> entry : datasetFileToLearningAlgorithmsMap.entrySet()) {
         for (ILearningAlgorithm learningAlgorithm : entry.getValue()) {
            stringBuilder.append(String.format(reflectionString_ALGORITHM_RUNS_ON, learningAlgorithm, entry.getKey().getFile().getName()));
            stringBuilder.append(StringUtils.LINE_BREAK);
         }
      }
      Assert.assertEquals(stringBuilder.toString().trim(), consoleOutput.trim());
   }


   /**
    * Tests if the {@link DetermineApplicableAlgorithmsCommand} will not be executed as no dataset
    * is set in the system configuration.
    * 
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   @Test
   public void testDetermineApplicableAlgorithmsWithNoDataset()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {

      String[] command = { ECommand.DETERMINE_APPLICABLE_ALGORITHMS.getCommandIdentifier() };
      String consoleOutput = TestUtils.simulateCommandLineInputAndReturnConsoleOutput(command);

      assertFailedCommandResultAndCorrectConsoleOutput(consoleOutput);
   }


   /**
    * Checks if the latest command in the command history of the system status is a
    * {@link DetermineApplicableAlgorithmsCommand} and if the according command result is not
    * executed successfully, if the exception is set to {@link CommandCannotBeExecutedException}, if
    * the result is null and if the additional information are empty.
    * 
    * @param consoleOutput the redirected console output
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    */
   private void assertFailedCommandResultAndCorrectConsoleOutput(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      Pair<ICommand, CommandResult> latestPairInHistory = TestUtils.getLatestPairOfCommandAndCommandResultInCommandHistory();
      TestUtils.assertCorrectCommandType(latestPairInHistory.getFirst(), DetermineApplicableAlgorithmsCommand.class);
      TestUtils.assertCorrectFailedEmptyCommandResult(latestPairInHistory.getSecond(), CommandCannotBeExecutedException.class);
      assertFailedConsoleOutput(consoleOutput);
   }


   /**
    * Checks if the console output is about a {@link DetermineApplicableAlgorithmsCommand} which
    * failed during execution and if the reason is that no dataset was found in the system
    * configuration.
    * 
    * @param consoleOutput the redirected Console output
    * @throws IllegalArgumentException if reflection did not work
    * @throws IllegalAccessException if reflection did not work
    * @throws NoSuchFieldException if reflection did not work
    * @throws SecurityException if reflection did not work
    */
   private void assertFailedConsoleOutput(String consoleOutput)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      String reflectionString_ERROR_NO_DATASET_IN_CONFIGURATION = TestUtils.getStringByReflection(
            DetermineApplicableAlgorithmsCommand.class, REFLECTION_DETERMINEAPPLICABLEALGORITHMSCOMMAND_ERROR_NO_DATASET_IN_CONFIGURATION);

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format(TestUtils.getInfoCommandCannotBeExecuted(ECommand.DETERMINE_APPLICABLE_ALGORITHMS,
            reflectionString_ERROR_NO_DATASET_IN_CONFIGURATION)));
      Assert.assertEquals(stringBuilder.toString().trim(), consoleOutput.trim());
   }
}
