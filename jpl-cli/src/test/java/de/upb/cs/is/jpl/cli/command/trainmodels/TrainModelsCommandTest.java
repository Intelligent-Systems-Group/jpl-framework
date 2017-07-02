package de.upb.cs.is.jpl.cli.command.trainmodels;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDataset;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;
import de.upb.cs.is.jpl.cli.command.ACommandUnitTest;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.CommandLineParserView;
import de.upb.cs.is.jpl.cli.core.InputControl;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;


/**
 * Test for the {@link TrainModelsCommand}.
 *
 * @author Sebastian Gottschalk
 *
 */
public class TrainModelsCommandTest extends ACommandUnitTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "trainmodels" + File.separator;

   private SystemConfiguration systemConfiguration;
   private static final String ERROR_WRONG_NUMBER_OF_AGORITHMS = "The number of algorithms in the system configuration should be %s but is %s.";
   private static final String ERROR_LEARNING_MODEL_IS_NULL = "The learning model is null.";
   private static final String ERROR_WRONG_PREDICTION_TYPE = "The prediction of the model should be 1.";

   private static final String REFLECTION_ALGORITHM_DATASET_MODEL_MAP = "learningAlgorithmDatasetPairToLearningModelMap";
   private static final String JSON_TWO_ALGORITHMS_CONFIGURATION = "[{\"name\": \"perceptron_rank\", \"parameters\": {\"a\": 1}},{\"name\": \"perceptron_rank\", \"parameters\": {\"a\": 2}}]";
   private static final String JSON_ONE_ALGORITHM_CONFIGURATION = "[{\"name\": \"perceptron_rank\", \"parameters\": {\"a\": 1}}]";

   private static final String CLI_PARAMETER_DATASET = "--dataset=";
   private static final String CORRECT_DATASET = "instancerankingtest.gprf";
   private static final String INCORRECT_DATASET = "incorrectfilecontent.gprf";
   private static final String RENAMED_PDF = "renamedpdf.gprf";

   private InputControl inputControl;


   /**
    * Creates a new test for the {@link TrainModelsCommand}.
    */
   public TrainModelsCommandTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Initializes this test.
    */
   @Before
   public void setupTrainModelsCommandTest() {
      systemConfiguration = SystemConfiguration.getSystemConfiguration();
      systemConfiguration.resetSystemConfiguration();
      inputControl = InputControl.getInputControl();

   }


   /**
    * Asserts that the expected number of trained algorithm equals the number of algorithm in system
    * configuration.
    *
    * @param amountOfAlgorithms the expected amount of algorithms in the system configuration
    * 
    * @throws NoSuchFieldException if the reflection process fails
    * @throws SecurityException if the reflection process fails
    * @throws IllegalArgumentException if the reflection process fails
    * @throws IllegalAccessException if the reflection process fails
    */
   private void assertCorrectNumberOFLearningAlgorithmsTrainedInSystemConfiguration(int amountOfAlgorithms)
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
      SystemConfiguration systemConfiguration = SystemConfiguration.getSystemConfiguration();

      Field field = systemConfiguration.getClass().getDeclaredField(REFLECTION_ALGORITHM_DATASET_MODEL_MAP);
      field.setAccessible(true);
      Object value = field.get(systemConfiguration);

      @SuppressWarnings("unchecked")
      Map<Pair<ILearningAlgorithm, IDataset<?, ?, ?>>, ILearningModel<?>> learningAlgorithmDatasetPairToLearningModelMap = (HashMap<Pair<ILearningAlgorithm, IDataset<?, ?, ?>>, ILearningModel<?>>) value;

      assertEquals(
            String.format(ERROR_WRONG_NUMBER_OF_AGORITHMS, amountOfAlgorithms, learningAlgorithmDatasetPairToLearningModelMap.size()),
            amountOfAlgorithms, learningAlgorithmDatasetPairToLearningModelMap.size());
   }


   /**
    * Trains two test algorithms on the dummy dataset and checks if the results are as expected.
    *
    * @throws NoSuchFieldException if the reflection process fails
    * @throws SecurityException if the reflection process fails
    * @throws IllegalArgumentException if the reflection process fails
    * @throws IllegalAccessException if the reflection process fails
    * @throws ParsingFailedException if the parsing process of the dataset failed
    * @throws TrainModelsFailedException if the training of the model failed
    * @throws PredictionFailedException if the prediction on the instance failed
    */
   @Test
   public void trainModelsOnInstanceRankingDataset()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException,
            ParsingFailedException,
            TrainModelsFailedException,
            PredictionFailedException {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
      addDatasetToSystemConfiguration();
      setTwoPrankAlgorithmsInSystemConfiguration();
      trainAlgorithms();
      checkTrainingResults();
      assertCorrectNumberOFLearningAlgorithmsTrainedInSystemConfiguration(2);
   }


   /**
    * Trains a model on a corrupt dataset.
    *
    * @throws NoSuchFieldException if the reflection process fails
    * @throws SecurityException if the reflection process fails
    * @throws IllegalArgumentException if the reflection process fails
    * @throws IllegalAccessException if the reflection process fails
    * @throws JsonParsingFailedException if json can't be parsed
    */
   @Test
   public void trainModelsOnCorruptDataset()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException,
            JsonParsingFailedException {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
      addWrongContentDatasetToSystemConfiguration();
      setPrankAlgorithmInSystemConfiguration();
      trainAlgorithms();
      assertCorrectNumberOFLearningAlgorithmsTrainedInSystemConfiguration(0);
   }


   /**
    * Trains a model on a PDF as dataset.
    *
    * @throws NoSuchFieldException if the reflection process fails
    * @throws SecurityException if the reflection process fails
    * @throws IllegalArgumentException if the reflection process fails
    * @throws IllegalAccessException if the reflection process fails
    * @throws JsonParsingFailedException if json can't be parsed
    */
   @Test
   public void trainModelsOnPDFDataset()
         throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException,
            JsonParsingFailedException {
      SystemConfiguration.getSystemConfiguration().resetSystemConfiguration();
      addPDFAsDatasetToSystemConfiguration();
      setPrankAlgorithmInSystemConfiguration();
      trainAlgorithms();
      assertCorrectNumberOFLearningAlgorithmsTrainedInSystemConfiguration(0);
   }


   /**
    * Checks the overall training results and asserts that the training yields the expected results.
    * 
    * @throws ParsingFailedException if the parsing process of the dataset failed
    * @throws TrainModelsFailedException if the training of the model failed
    * @throws PredictionFailedException if the prediction on the instance failed
    */
   private void checkTrainingResults() throws ParsingFailedException, TrainModelsFailedException, PredictionFailedException {
      for (ILearningAlgorithm learningAlgorithm : systemConfiguration.getLearningAlgorithms()) {
         for (DatasetFile datasetFile : systemConfiguration.getDatasetFiles()) {
            assertCorrectTrainingForSingleInstanceRankingAlgorithm(learningAlgorithm, datasetFile);
         }
      }
   }


   /**
    * Performs all necessary assertions to assert that the training for a single algorithm on a
    * specific dataset was correct. The {@link learningAlgorithm} has to be one which can work with
    * the {@link InstanceRankingDataset}.
    * 
    * @param learningAlgorithm the algorithm to check
    * @param datasetFile the dataset the algorithm should be trained on
    * 
    * @throws ParsingFailedException if the parsing process of the dataset failed
    * @throws TrainModelsFailedException if the training of the model failed
    * @throws PredictionFailedException if the prediction on the instance failed
    */
   private void assertCorrectTrainingForSingleInstanceRankingAlgorithm(ILearningAlgorithm learningAlgorithm, DatasetFile datasetFile)
         throws ParsingFailedException,
            TrainModelsFailedException,
            PredictionFailedException {
      IDatasetParser datasetParser = learningAlgorithm.getDatasetParser();
      IDataset<?, ?, ?> dataset = datasetParser.parse(datasetFile);
      ILearningModel<?> learningModel = learningAlgorithm.train(dataset);
      assertNotNull(ERROR_LEARNING_MODEL_IS_NULL, learningModel);
      double[] featureVector = { 8.3, 4.3, 6.7, 7.1, 5.3 };
      assertEquals(ERROR_WRONG_PREDICTION_TYPE, learningModel.predict(new InstanceRankingInstance(1, featureVector, 5)), 1);
   }


   /**
    * Performs the train models command.
    */
   private void trainAlgorithms() {
      String[] trainModelsCommandString = { ECommand.TRAIN_MODELS.getCommandIdentifier() };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), trainModelsCommandString);
   }


   /**
    * Sets two test learning algorithms in the system configuration.
    * 
    * @throws JsonParsingFailedException if JSON can't be parsed
    */
   private void setTwoPrankAlgorithmsInSystemConfiguration() throws JsonParsingFailedException {

      JsonArray createJsonArrayFromString = JsonUtils.createJsonArrayFromString(JSON_TWO_ALGORITHMS_CONFIGURATION);
      systemConfiguration.setJsonAlgorithmConfiguration(createJsonArrayFromString);
      String[] setMultipleLearningAlgorithmsCommandString = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), setMultipleLearningAlgorithmsCommandString);
   }


   /**
    * Sets one test learning algorithms in the system configuration.
    * 
    * @throws JsonParsingFailedException if JSON can't be parsed
    */
   private void setPrankAlgorithmInSystemConfiguration() throws JsonParsingFailedException {
      JsonArray createJsonArrayFromString = JsonUtils.createJsonArrayFromString(JSON_ONE_ALGORITHM_CONFIGURATION);
      systemConfiguration.setJsonAlgorithmConfiguration(createJsonArrayFromString);
      String[] setMultipleLearningAlgorithmsCommandString = { ECommand.LOAD_LEARNING_ALGORITHMS.getCommandIdentifier() };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), setMultipleLearningAlgorithmsCommandString);
   }


   /**
    * Adds the dummy dataset to the system configuration.
    */
   private void addDatasetToSystemConfiguration() {

      String[] setDatasetCommandString = { ECommand.ADD_DATASET.getCommandIdentifier(),
            CLI_PARAMETER_DATASET + getTestRessourcePathFor(CORRECT_DATASET) };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), setDatasetCommandString);
   }


   /**
    * Adds a PDF as dataset to the system configuration.
    */
   private void addPDFAsDatasetToSystemConfiguration() {

      String[] setDatasetCommandString = { ECommand.ADD_DATASET.getCommandIdentifier(),
            CLI_PARAMETER_DATASET + getTestRessourcePathFor(RENAMED_PDF) };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), setDatasetCommandString);
   }


   /**
    * Adds a GPRF dataset with wrong content to the system configuration.
    */
   private void addWrongContentDatasetToSystemConfiguration() {

      String[] setDatasetCommandString = { ECommand.ADD_DATASET.getCommandIdentifier(),
            CLI_PARAMETER_DATASET + getTestRessourcePathFor(INCORRECT_DATASET) };
      inputControl.update(CommandLineParserView.getCommandLineParserView(), setDatasetCommandString);
   }
}
