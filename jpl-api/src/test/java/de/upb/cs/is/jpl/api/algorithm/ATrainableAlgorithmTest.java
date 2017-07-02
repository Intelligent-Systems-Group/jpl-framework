package de.upb.cs.is.jpl.api.algorithm;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.metric.kendallstau.KendallsTau;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * An abstract class for testing general functions of the {@link ITrainableAlgorithm}s. The abstract
 * functions must be overridden with an algorithm specific content.
 * 
 * @author Sebastian Gottschalk
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of the
 *           algorithm
 * @param <ITEM> the type of the item feature vector used in the implementation of the algorithm
 * @param <RATING> the type of the rating used in the implementation of the algorithm
 *
 */
public abstract class ATrainableAlgorithmTest<CONTEXT, ITEM, RATING> extends AAlgorithmTest {

   protected static final String ERROR_INCORRECT_DATASET = "The algorithm can not work with the correct dataset / Error: %s";
   protected static final String ERROR_WRONG_OUTPUT = "The output should be \"%s\" but is \"%s\".";
   protected static final String ERROR_PREDICTION_FAILED = "Prediction failed / Error: %s";
   private static final String ERROR_CORRECT_DATASET = "The algorithm should not work with:  %s.";
   protected static final String ERROR_DIFFERENT_LIST_LENGTHS = "The lists for the predictions and the correct datasets should be equal.";
   protected static final String ERROR_UNINITIALIZED_LIST = "Both lists must be initialized.";
   private final static String ERROR_NO_MODEL_RETURN = "There is no model returned by the algorithm.";


   /**
    * Creates a new unit test for algorithms with the additional path to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ATrainableAlgorithmTest(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   /**
    * Returns an instance of the {@link ITrainableAlgorithm} which should be checked.
    * 
    * @return instance of the trainable algorithm to check
    */
   public abstract ITrainableAlgorithm getTrainableAlgorithm();


   @Override
   public IAlgorithm getAlgorithm() {
      return getTrainableAlgorithm();
   }


   /**
    * Returns a list of wrong {@link IDataset}s which can not be used with the
    * {@link ITrainableAlgorithm}.
    * 
    * @return a list of wrong datasets
    */
   public abstract List<IDataset<?, ?, ?>> getWrongDatasetList();


   /**
    * Returns a list of correct {@link IDataset}s which can be used with the
    * {@link ITrainableAlgorithm}.
    * 
    * @return a list of correct datasets
    */
   public abstract List<IDataset<CONTEXT, ITEM, RATING>> getCorrectDatasetList();


   /**
    * Returns a list of {@link Pair}s of the {@link IDataset} and the expected prediction list.
    * 
    * @return a list of pairs where each pair contains a dataset and the expected predictions as a
    *         list
    */
   public abstract List<Pair<IDataset<CONTEXT, ITEM, RATING>, List<RATING>>> getPredictionsForDatasetList();


   /**
    * The comparison method is used to test if two rating lists are equal. If the rating is of type
    * double the functions checks the distance between two ratings against the threshold
    * {@link TestUtils#DOUBLE_DELTA} otherwise it uses the internal {@link List#equals(Object)}
    * method.
    * 
    * @param firstList the first list which should be checked
    * @param secondList the second list which should be checked
    * @return {@code true} if the list are equal otherwise {@code false}
    */
   @SuppressWarnings("unchecked")
   protected boolean areRatingListsEqual(List<RATING> firstList, List<RATING> secondList) {
      // Datasets have not the same length
      if (firstList == null || secondList == null) {
         throw new IllegalArgumentException(ERROR_UNINITIALIZED_LIST);
      }
      // List have not the same length
      if (firstList.size() != secondList.size()) {
         return false;
      }
      // Lists are empty
      if (firstList.isEmpty()) {
         return true;
      }
      // Is the list of type Double?
      if (firstList.get(0) instanceof Double && secondList.get(0) instanceof Double) {
         return areDoubleListsEqual((List<Double>) firstList, (List<Double>) secondList);
      }
      if (secondList.get(0) instanceof Ranking && secondList.get(0) instanceof Ranking) {
         return areRankingListEqual((List<Ranking>) firstList, (List<Ranking>) secondList, 0.1);
      }

      return secondList.equals(firstList);
   }


   private boolean areRankingListEqual(List<Ranking> firstList, List<Ranking> secondList, double precision) {
      KendallsTau tau = new KendallsTau();
      double kendallsCorrealtion = 0.0;
      try {
         kendallsCorrealtion = tau.getAggregatedLossForRatings(firstList, secondList);
      } catch (LossException e) {
         e.printStackTrace();
      }

      return TestUtils.areDoublesEqual(kendallsCorrealtion, 1.0, precision);
   }


   /**
    * Checks the distance between two ratings lists stepwise against the threshold
    * {@link TestUtils#DOUBLE_DELTA}.
    * 
    * @param firstList the first list which should be checked
    * @param secondList the secondList second list which should be checked
    * @return {@code true} if the list are equal otherwise {@code false}
    */
   protected boolean areDoubleListsEqual(List<Double> firstList, List<Double> secondList) {
      for (int i = 0; i < firstList.size(); i++) {
         if (!areDoublesEqual(secondList.get(i), firstList.get(i))) {
            return false;
         }
      }
      return true;
   }


   /**
    * Checks whether the two double values are numerically equal. The two values are checked against
    * the threshold {@link TestUtils#DOUBLE_DELTA}.
    * 
    * @param firstValue the first double value
    * @param secondValue the second double value
    * @return {@code true}, if the two values are numerically equal, otherwise {@code false}
    */
   protected boolean areDoublesEqual(double firstValue, double secondValue) {
      return TestUtils.areDoublesEqual(firstValue, secondValue, TestUtils.DOUBLE_DELTA);
   }


   /**
    * A helper methods which returns a {@link IDataset} by parsing a GPRF file with an
    * {@link IDatasetParser}
    * 
    * @param datasetparser a dataset parser which is used to parse the file
    * @param pathToFile a path to a GPRF file
    * @return a dataset created by the parser or {@code null} if an error occurs
    */
   public IDataset<?, ?, ?> createDatasetOutOfFile(IDatasetParser datasetparser, String pathToFile) {
      DatasetFile datasetfile = new DatasetFile(new File(pathToFile));
      try {
         return datasetparser.parse(datasetfile);
      } catch (ParsingFailedException e) {
         fail(e.getMessage());
      }
      return null;
   }


   /**
    * Test if the algorithm throws a {@link TrainModelsFailedException} if they are trained with
    * wrong {@link IDataset}.
    * 
    */
   @Test
   public void testWrongDatasets() {
      ITrainableAlgorithm algorithm = getTrainableAlgorithm();
      List<IDataset<?, ?, ?>> wrongDatasetList = getWrongDatasetList();
      for (int i = 0; i < wrongDatasetList.size(); i++) {
         try {
            algorithm.train(wrongDatasetList.get(i));
            fail(String.format(ERROR_CORRECT_DATASET, wrongDatasetList.get(i)));
         } catch (TrainModelsFailedException e) {
            // Everything is ok when the Exception is thrown
         }
      }
   }


   /**
    * Test if the {@link ITrainableAlgorithm} runs without error by using correct {@link IDataset}s
    * for training.
    */
   @Test
   public void testCorrectDatasets() {
      ITrainableAlgorithm algorithm = getTrainableAlgorithm();
      List<IDataset<CONTEXT, ITEM, RATING>> correctDatasetList = getCorrectDatasetList();
      for (int i = 0; i < correctDatasetList.size(); i++) {
         try {
            // Check if model is not null
            ILearningModel<?> model = algorithm.train(correctDatasetList.get(i));
            assertNotNull(ERROR_NO_MODEL_RETURN, model);
         } catch (TrainModelsFailedException e) {
            // Check if the training not failed
            fail(String.format(ERROR_INCORRECT_DATASET, e.getMessage()));
         }
      }
   }


   /**
    * Test if the {@link ITrainableAlgorithm} if able to predict correct results.
    */
   @Test
   public void testCorrectPredictions() {
      List<IDataset<CONTEXT, ITEM, RATING>> correctDatasetList = getCorrectDatasetList();
      List<Pair<IDataset<CONTEXT, ITEM, RATING>, List<RATING>>> predictionDatasetList = getPredictionsForDatasetList();
      Assert.assertEquals(ERROR_DIFFERENT_LIST_LENGTHS, correctDatasetList.size(), predictionDatasetList.size());
      for (int i = 0; i < correctDatasetList.size(); i++) {
         IDataset<CONTEXT, ITEM, RATING> trainingDataset = correctDatasetList.get(i);
         IDataset<CONTEXT, ITEM, RATING> testingDataset = predictionDatasetList.get(i).getFirst();
         List<RATING> expectedPredictedValueOfDataset = predictionDatasetList.get(i).getSecond();
         try {
            // Create dataset
            ITrainableAlgorithm algorithm = getTrainableAlgorithm();
            // Check if model is not null
            @SuppressWarnings("unchecked")
            ILearningModel<RATING> trainedLearningModel = (ILearningModel<RATING>) algorithm.train(trainingDataset);
            // Test dataset
            List<RATING> predictedValueOfDataset = trainedLearningModel.predict(testingDataset);
            if (!areRatingListsEqual(predictedValueOfDataset, expectedPredictedValueOfDataset)) {
               fail(String.format(ERROR_WRONG_OUTPUT, expectedPredictedValueOfDataset, predictedValueOfDataset));

            }
         } catch (TrainModelsFailedException e) {
            fail(String.format(ERROR_INCORRECT_DATASET, e.getMessage()));
         } catch (PredictionFailedException e) {
            fail(String.format(ERROR_PREDICTION_FAILED, e.getMessage()));
         }
      }
   }
}
