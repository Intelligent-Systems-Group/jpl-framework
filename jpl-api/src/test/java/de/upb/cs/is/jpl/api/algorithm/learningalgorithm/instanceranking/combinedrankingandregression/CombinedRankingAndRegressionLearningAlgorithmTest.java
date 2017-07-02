package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression;


import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.configuration.logging.LoggingConfiguration;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Unit tests for {@link CombinedRankingAndRegressionLearningAlgorithm}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class CombinedRankingAndRegressionLearningAlgorithmTest extends ALearningAlgorithmTest<double[], List<double[]>, IVector> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;
   private static final String DATASET_FILE = "instancemovielens.gprf";
   private static final String ERROR_REFLECTION_FAILED = "Test can not run because reflection failed";
   private static final String ERROR_FAILED_TO_ADD_INSTANCES = "Failed to add instances to prediction list. Problem: ";
   private static final String PARAMETER_LOSSFUNCTION = "loss_function_identifier";
   private static final String PARAMETER_ALPHA = "alpha";
   private static final String PARAMETER_LAMBDA = "lambda";
   private static final String PARAMETER_ITERATIONS = "iterations";

   private static final int RANDOM_SEED = 1234;
   // Chosen of high delta because of randomization
   private final static double DOUBLE_DELTA = 0.15;


   private static final String PARAMETER_LOSSFUNCTION_REFLECTION_VARIABLE = "WRONG_VALUE_FOR_LOSSFUNCTION";
   private static final String PARAMETER_ALPHA_REFLECTION_VARIABLE = "WRONG_VALUE_FOR_ALPHA";
   private static final String PARAMETER_LAMBDA_REFLECTION_VARIABLE = "WRONG_VALUE_FOR_LAMBDA";
   private static final String PARAMETER_ITERATIONS_REFLECTION_VARIABLE = "WRONG_VALUE_FOR_ITERATIONS";


   /**
    * Makes sure that the logging is initialized correctly and the random number generator is
    * initialized with the same seed.
    */
   @Override
   @Before
   public void setupTest() {
      LoggingConfiguration.setupLoggingConfiguration();
      RandomGenerator.initializeRNG(RANDOM_SEED);
   }


   /**
    * Creates a new unit test for the CRR algorithm.
    */
   public CombinedRankingAndRegressionLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new CombinedRankingAndRegressionLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> returnList = new ArrayList<>();
      returnList.add(this.createDatasetOutOfFile(new InstanceRankingDatasetParser(), getTestRessourcePathFor(DATASET_FILE)));
      return returnList;
   }


   @SuppressWarnings("unchecked")
   @Override
   public List<IDataset<double[], List<double[]>, IVector>> getCorrectDatasetList() {
      List<IDataset<double[], List<double[]>, IVector>> returnList = new ArrayList<>();
      returnList.add((IDataset<double[], List<double[]>, IVector>) this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(),
            getTestRessourcePathFor(DATASET_FILE)));
      return returnList;
   }


   @Override
   @Test
   public void testCorrectPredictions() {
      List<IDataset<double[], List<double[]>, IVector>> correctDatasetList = getCorrectDatasetList();
      List<Pair<IDataset<double[], List<double[]>, IVector>, List<IVector>>> predictionDatasetList = getPredictionsForDatasetList();
      Assert.assertEquals(ERROR_DIFFERENT_LIST_LENGTHS, correctDatasetList.size(), predictionDatasetList.size());
      for (int i = 0; i < correctDatasetList.size(); i++) {
         IDataset<double[], List<double[]>, IVector> testDataset = correctDatasetList.get(i);
         IDataset<double[], List<double[]>, IVector> predictDataset = predictionDatasetList.get(i).getFirst();
         List<IVector> expectedPredictedValueOfDataset = predictionDatasetList.get(i).getSecond();
         try {
            // Create dataset
            ITrainableAlgorithm algorithm = getTrainableAlgorithm();
            // Check if model is not null
            @SuppressWarnings("unchecked")
            ILearningModel<Double> trainedLearningModel = (ILearningModel<Double>) algorithm.train(testDataset);
            // Test dataset
            List<Double> predictedValueOfDataset = trainedLearningModel.predict(predictDataset);
            if (!areIVectorRatingListsEqual(predictedValueOfDataset, expectedPredictedValueOfDataset)) {
               fail(String.format(ERROR_WRONG_OUTPUT, expectedPredictedValueOfDataset, predictedValueOfDataset));
            }
         } catch (TrainModelsFailedException e) {
            fail(String.format(ERROR_INCORRECT_DATASET, e.getMessage()));
         } catch (PredictionFailedException e) {
            fail(String.format(ERROR_PREDICTION_FAILED, e.getMessage()));
         }
      }
   }


   /**
    * Check if the predicted value of the dataset are equal to the first items in the list of
    * {@link IVector}s.
    * 
    * @param predictedValueOfDataset predicted values of the dataset
    * @param expectedPredictedValueOfDataset expected prediction of the dataset
    * @return true if the lists are equal
    */
   private boolean areIVectorRatingListsEqual(List<Double> predictedValueOfDataset, List<IVector> expectedPredictedValueOfDataset) {
      for (int i = 0; i < predictedValueOfDataset.size(); i++) {
         if (Math.abs(predictedValueOfDataset.get(i) - expectedPredictedValueOfDataset.get(i).getValue(0)) > DOUBLE_DELTA) {
            return false;
         }
      }
      return true;
   }


   @Override
   public List<Pair<IDataset<double[], List<double[]>, IVector>, List<IVector>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<double[], List<double[]>, IVector>, List<IVector>>> returnList = new ArrayList<>();
      IDataset<double[], List<double[]>, IVector> dataset = new DefaultAbsoluteDataset();
      List<IVector> predictionList = new LinkedList<>();

      double[] minusArray = { -1 };
      double[] contentArray = { -1 };
      DenseDoubleVector emptyPrediction = new DenseDoubleVector(minusArray);
      double[] itemFeatures = { 1, 0, 1 };
      List<double[]> itemList = new LinkedList<>();
      itemList.add(itemFeatures);

      try {

         contentArray[0] = 8.7607;
         predictionList.add(new DenseDoubleVector(contentArray));
         double[] predictionFeatures = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
         dataset.addInstance(new DefaultInstance<IVector>(predictionFeatures, itemList, emptyPrediction));

         contentArray[0] = 0.0;
         predictionList.add(new DenseDoubleVector(contentArray));
         double[] predictionFeatures2 = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
         dataset.addInstance(new DefaultInstance<IVector>(predictionFeatures2, itemList, emptyPrediction));

         contentArray[0] = 4.6753;
         predictionList.add(new DenseDoubleVector(contentArray));
         double[] predictionFeatures3 = { 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0 };
         dataset.addInstance(new DefaultInstance<IVector>(predictionFeatures3, itemList, emptyPrediction));

      } catch (InvalidInstanceException e) {
         Assert.fail(ERROR_FAILED_TO_ADD_INSTANCES + e.getMessage());
      }

      returnList.add(Pair.of(dataset, predictionList));
      return returnList;

   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> returnList = new ArrayList<>();

      String[] parameterKeys = { PARAMETER_LOSSFUNCTION, PARAMETER_ALPHA, PARAMETER_LAMBDA, PARAMETER_ITERATIONS };
      String[] parameterValues = { "squared", "0.5", "3", "100" };

      // Add correct combinations which should be tested
      for (int i = 0; i < parameterKeys.length; i++) {
         returnList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterKeys[i], parameterValues[i]));
      }

      return returnList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> returnList = new ArrayList<>();

      String[] parameterKeys = { PARAMETER_LOSSFUNCTION, PARAMETER_ALPHA, PARAMETER_LAMBDA, PARAMETER_ITERATIONS };
      String[] parameterValues = { "hello", "1.2", "0", "-100" };
      String[] parameterReflections = { PARAMETER_LOSSFUNCTION_REFLECTION_VARIABLE, PARAMETER_ALPHA_REFLECTION_VARIABLE,
            PARAMETER_LAMBDA_REFLECTION_VARIABLE, PARAMETER_ITERATIONS_REFLECTION_VARIABLE };

      // Add correct combinations which should be tested
      try {
         for (int i = 0; i < parameterKeys.length; i++) {
            returnList
                  .add(Pair.of(TestUtils.getStringByReflection(CombinedRankingAndRegressionConfiguration.class, parameterReflections[i]),
                        JsonUtils.createJsonObjectFromKeyAndValue(parameterKeys[i], parameterValues[i])));
         }
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);

      }

      return returnList;
   }


}
