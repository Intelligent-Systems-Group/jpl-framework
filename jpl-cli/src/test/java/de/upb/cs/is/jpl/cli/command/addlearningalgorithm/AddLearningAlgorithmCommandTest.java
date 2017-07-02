package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankLearningAlgorithm;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.util.TestUtils;


/**
 * Test for the {@link AddLearningAlgorithmFromJsonCommand}.
 *
 * @author Alexander Hetzer
 */
public class AddLearningAlgorithmCommandTest extends ACommandUnitTest {

   private static final String OPTION_PARAMETERS = "-p=k:3,t:1";
   private static final String OPTION_PARAMETERS_SUBSET = "-p=t:1";
   private static final String OPTION_PARAMETERS_TOO_MUCH_WHITESPACE = "-p =k:3,t:1";
   private static final String OPTION_ALGORITHM_NAME_PRANK = "-n=perceptron_rank";
   private static final String OPTION_ALGORITHM_NAME_TOO_MUCH_WHITESPACE = "-n= perceptron_rank ";

   private static final String OPTION_PARAMETERS_EXTREME_WHITESPACE = "-p =  k  :  3 , t  :  1  ";
   private static final String OPTION_ALGORITHM_NAME_WRONG = "-n=Blubbie";
   private static final String OPTION_PARAMETERS_MISSING_SPLIT_CHAR = "-p =k:3 t:1";

   private SystemConfiguration systemConfiguration;
   private PerceptronRankConfiguration defaultPrankAlgorithmConfiguration;


   /**
    * Assigns and resets the system configuration, and assigns the
    * {@code #defaultPrankAlgorithmConfiguration} with a default configuration.
    */
   @Before
   public void resetSystemConfiguration() {
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();
      defaultPrankAlgorithmConfiguration = (PerceptronRankConfiguration) new PerceptronRankLearningAlgorithm()
            .getDefaultAlgorithmConfiguration();
   }


   /**
    * Tests whether adding a correctly defined learning algorithm via console works.
    */
   @Test
   public void testAddingCorrectlyDefinedLearningAlgorithm() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_PRANK,
            OPTION_PARAMETERS };
      TestUtils.simulateCommandLineInput(commandLineInput);

      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(1);
      assertCorrectAlgorithmClassInSystemConfiguration();
      assertLearningAlgorithmIsInitializedWithGivenParameters(3);
   }


   /**
    * Tests whether adding a correctly defined learning algorithm with only a subset of the possible
    * parameters via console works.
    */
   @Test
   public void testAddingLearningAlgorithmWithSubsetOfParameters() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_PRANK,
            OPTION_PARAMETERS_SUBSET };
      TestUtils.simulateCommandLineInput(commandLineInput);

      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(1);
      assertCorrectAlgorithmClassInSystemConfiguration();
      assertLearningAlgorithmIsInitializedWithGivenParameters(defaultPrankAlgorithmConfiguration.getK());
   }


   /**
    * Tests whether adding a slightly incorrectly defined algorithm with too much whitespace via
    * console works.
    */
   @Test
   public void testAddingSlightlyIncorrectlyDefinedLearningAlgorithmWithWhitespace() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_TOO_MUCH_WHITESPACE,
            OPTION_PARAMETERS_EXTREME_WHITESPACE };
      TestUtils.simulateCommandLineInput(commandLineInput);

      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(1);
      assertCorrectAlgorithmClassInSystemConfiguration();
      assertLearningAlgorithmIsInitializedWithGivenParameters(3);
   }


   /**
    * Tests whether adding a learning algorithm without parameters via console works.
    */
   @Test
   public void testAddingLearningAlgorithmWithoutParameters() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_PRANK };
      TestUtils.simulateCommandLineInput(commandLineInput);

      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(1);
      assertCorrectAlgorithmClassInSystemConfiguration();
      assertLearningAlgorithmIsInitializedWithGivenParameters(defaultPrankAlgorithmConfiguration.getK());
   }


   // == Tests below should lead to not adding an algorithm ==

   /**
    * Tests whether adding a learning algorithm with an unknown identifier fails.
    */
   @Test
   public void testAddingUnknownLearningAlgorithm() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_WRONG,
            OPTION_PARAMETERS_TOO_MUCH_WHITESPACE };
      TestUtils.simulateCommandLineInput(commandLineInput);
      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(0);
   }


   /**
    * Tests whether adding a learning algorithm without identifier fails.
    */
   @Test
   public void testAddingLearningAlgorithmWithoutIdentifier() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_PARAMETERS_TOO_MUCH_WHITESPACE };
      TestUtils.simulateCommandLineInput(commandLineInput);
      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(0);
   }


   /**
    * Tests whether adding a learning algorithm with malformed parameters due to a missing parameter
    * separator fails.
    */
   @Test
   public void testAddingLearningAlgorithmWithMalformedParametersDueToMissingParameterSeparator() {
      String[] commandLineInput = { ECommand.ADD_LEARNING_ALGORITHM.getCommandIdentifier(), OPTION_ALGORITHM_NAME_PRANK,
            OPTION_PARAMETERS_MISSING_SPLIT_CHAR };
      TestUtils.simulateCommandLineInput(commandLineInput);
      assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(0);
   }


   /**
    * Asserts that the first learning algorithm in the system configuration is initialized with the
    * parameters given. This method is intended to work with the {@link PRankAlgorithm}.
    * 
    * @param k expected value of parameter k of {@link PRankAlgorithm}
    */
   private void assertLearningAlgorithmIsInitializedWithGivenParameters(int k) {
      List<ILearningAlgorithm> learningAlgorithmsInSystemConfiguration = systemConfiguration.getLearningAlgorithms();
      ILearningAlgorithm firstLearningAlgorithmInSystemConfiguration = learningAlgorithmsInSystemConfiguration.get(0);
      PerceptronRankLearningAlgorithm prankLearningAlgorithm = (PerceptronRankLearningAlgorithm) firstLearningAlgorithmInSystemConfiguration;
      PerceptronRankConfiguration prankAlgorithmConfiguration = (PerceptronRankConfiguration) prankLearningAlgorithm
            .getAlgorithmConfiguration();
      assertEquals(k, prankAlgorithmConfiguration.getK());
   }


   /**
    * Asserts that the amount of learning algorithms in the system configuration is equal to the
    * given number.
    * 
    * @param expectedNumberOfLearningAlgorithms the expected number of learning algorithms in the
    *           system configuration
    */
   private void assertGivenNumberOfLearningAlgorithmsInSystemConfiguration(int expectedNumberOfLearningAlgorithms) {
      List<ILearningAlgorithm> learningAlgorithmsInSystemConfiguration = systemConfiguration.getLearningAlgorithms();
      assertEquals(expectedNumberOfLearningAlgorithms, learningAlgorithmsInSystemConfiguration.size());
   }


   /**
    * Asserts that the first learning algorithm in the system configuration is of the type
    * {@link PRankAlgorithm}.
    */
   private void assertCorrectAlgorithmClassInSystemConfiguration() {
      List<ILearningAlgorithm> learningAlgorithmsInSystemConfiguration = systemConfiguration.getLearningAlgorithms();
      ILearningAlgorithm firstLearningAlgorithmInSystemConfiguration = learningAlgorithmsInSystemConfiguration.get(0);
      assertEquals(PerceptronRankLearningAlgorithm.class, firstLearningAlgorithmInSystemConfiguration.getClass());
   }


}