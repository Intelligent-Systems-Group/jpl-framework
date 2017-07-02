package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.ARegressionUnitTest;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.batch.BatchGradientDescent;
import de.upb.cs.is.jpl.api.algorithm.optimization.gradientdescent.gradientstep.fixedlearningrate.FixedLearningRateGradientStep;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link LogisticRegression} implementation. A simple dataset provided by
 * Wikipedia with 20 instance is used:
 * <a href="https://en.wikipedia.org/wiki/Logistic_regression#Example:
 * _Probability_of_passing_an_exam_versus_hours_of_study"> Wikipedia </a>
 * 
 * The dataset is made up from the following situation: A group of 20 students spend between 0 and 6
 * hours studying for an exam. For each of the students we know if they passed or failed the exam.
 * Using a simple logistic regression analysis the probability that a student passes the exam under
 * the assumptions that he studies x hours where x in [0,6] can be computed. These values are used
 * as reference values to ensure that the logistic regression implementation predicts the correct
 * values.
 * 
 * @author Alexander Hetzer
 *
 */
public class LogisticRegressionTest extends ARegressionUnitTest<double[], NullType, Double> {

   private static final String REFLECTION_ERROR_UNKNOWN_GRADIENT_DESCENT_IDENTIFIER = "ERROR_UNKNOWN_GRADIENT_DESCENT_IDENTIFIER";

   private static final String ERROR_OBTAINING_WRONG_PARAMETERS = "Could not obtain the list of wrong parameters due to an error concerning reflection.";
   private static final String ERROR_OBTAINING_RIGHT_PARAMETERS = "Could not obtain the list of right parameters due to an error concerning reflection.";

   private static final String WRONG_GRADIENT_DESCENT_IDENTIFIER = "blubb";

   private static final String FILE_NAME_WRONG_PARAMETERS = "wrong_parameters.json";
   private static final String FILE_NAME_RIGHT_PARAMETERS = "right_parameters.json";

   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE = "learning_rate";

   private static final String WRONG_DATASET_FILE_NAME = "wrongDataset.gprf";

   private static final String RESOURCE_DIRECTORY_LEVEL = "logistic" + File.separator;


   /** This delta is used instead of the one of the {@link TestUtils} as that is to restrictive. */
   private final static double DOUBLE_DELTA = 0.01;


   /**
    * Creates a new unit test for logistic regression algorithms without any additional path to the
    * resources.
    */
   public LogisticRegressionTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new LogisticRegression(new BatchGradientDescent(new FixedLearningRateGradientStep()), 0.1);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> wrongDatasetList = new ArrayList<>();
      wrongDatasetList
            .add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_FILE_NAME)));
      return wrongDatasetList;
   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> correctDatasetList = new ArrayList<>();
      BaselearnerDataset dataset = createBaselearnerDatasetForTraining();
      correctDatasetList.add(dataset);
      return correctDatasetList;
   }


   /**
    * Returns a sample dataset for training. See class javadoc more details on the dataset.
    * 
    * @return a correct {@link BaselearnerDataset} for training
    */
   public BaselearnerDataset createBaselearnerDatasetForTraining() {
      double[][] datasetEntries = { { 0.50 }, { 0.75 }, { 1.00 }, { 1.25 }, { 1.50 }, { 1.75 }, { 1.75 }, { 2.00 }, { 2.25 }, { 2.50 },
            { 2.75 }, { 3.00 }, { 3.25 }, { 3.50 }, { 4.00 }, { 4.25 }, { 4.50 }, { 4.75 }, { 5.00 }, { 5.50 } };

      double[] correctResults = { -1, -1, -1, -1, -1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, 1, 1, 1, 1, 1 };

      BaselearnerDataset dataset = new BaselearnerDataset(datasetEntries.length, 1);
      for (int i = 0; i < datasetEntries.length; i++) {
         dataset.addFeatureVectorWithResult(datasetEntries[i], correctResults[i]);
      }
      return dataset;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      BaselearnerDataset dataset = createBaselearnerDatasetForPrediction();
      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> correctDatasetList = new ArrayList<>();
      List<Double> correctPredictions = Arrays.asList(ArrayUtils.toObject(dataset.getCorrectResults()));
      Arrays.fill(dataset.getCorrectResults(), 0);
      correctDatasetList.add(Pair.of(dataset, correctPredictions));
      return correctDatasetList;
   }


   /**
    * Initializes a sample dataset for prediction. See class javadoc more details on the dataset.
    * 
    * @return a correct {@link BaselearnerDataset} for prediction
    */
   public BaselearnerDataset createBaselearnerDatasetForPrediction() {
      double[][] datasetEntries = { { 1 }, { 2 }, { 3 }, { 4 }, { 5 } };
      double[] correctResults = { 0.07, 0.26, 0.61, 0.87, 0.97 };

      BaselearnerDataset dataset = new BaselearnerDataset(datasetEntries.length, 1);
      for (int i = 0; i < datasetEntries.length; i++) {
         dataset.addFeatureVectorWithResult(datasetEntries[i], correctResults[i]);
      }
      return dataset;
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> parameterList = new ArrayList<>();
      try {
         parameterList.add(JsonUtils.createJsonObjectFromFile(new File(getTestRessourcePathFor(FILE_NAME_RIGHT_PARAMETERS))));
      } catch (JsonParsingFailedException e) {
         fail(ERROR_OBTAINING_RIGHT_PARAMETERS);
      }
      parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(CONFIGURATION_PARAMETER_LEARNING_RATE, String.valueOf(0.1)));
      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> wrongParametersWithErrorMessages = new ArrayList<>();
      try {
         wrongParametersWithErrorMessages.add(Pair.of(
               String.format(TestUtils.getStringByReflection(LogisticRegressionConfiguration.class,
                     REFLECTION_ERROR_UNKNOWN_GRADIENT_DESCENT_IDENTIFIER), WRONG_GRADIENT_DESCENT_IDENTIFIER),
               JsonUtils.createJsonObjectFromFile(new File(getTestRessourcePathFor(FILE_NAME_WRONG_PARAMETERS)))));
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
            | JsonParsingFailedException e) {
         fail(ERROR_OBTAINING_WRONG_PARAMETERS);
      }
      return wrongParametersWithErrorMessages;
   }


   @Override
   protected boolean areDoublesEqual(double firstValue, double secondValue) {
      return TestUtils.areDoublesEqual(firstValue, secondValue, DOUBLE_DELTA);
   }

}
