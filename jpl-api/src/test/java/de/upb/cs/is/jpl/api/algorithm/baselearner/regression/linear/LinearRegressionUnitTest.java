package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.ARegressionUnitTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Unit tests for {@link LinearRegression}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class LinearRegressionUnitTest extends ARegressionUnitTest<double[], NullType, Double> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "linear" + File.separator;
   private static final String DATASET_FILE = "instancerankingtest.gprf";


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new LinearRegression();
   }


   /**
    * Creates a new unit test for linear regression algorithms.
    */
   public LinearRegressionUnitTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Test if the algorithm fails by using a wrong instance.
    * 
    * @throws PredictionFailedException if the prediction failed
    * @throws TrainModelsFailedException if the algorithm could not trained on the dataset
    */
   @Test(expected = PredictionFailedException.class)
   public void testLinearRegresssionOnWrongInstance() throws PredictionFailedException, TrainModelsFailedException {
      LinearRegression linearRegression = new LinearRegression();

      ILearningModel<?> learningModel = linearRegression.train(getCorrectDatasetList().get(0));

      double[] instanceFeatures = { 2.2, 3.3 };
      IInstance<double[], NullType, Integer> instance = new InstanceRankingInstance(1, instanceFeatures, 2);
      learningModel.predict(instance);

   }


   /**
    * Test if the algorithm fails by using a wrong feature vector.
    * 
    * @throws PredictionFailedException if the prediction failed
    * @throws TrainModelsFailedException if the algorithm could not trained on the dataset
    */
   @Test(expected = PredictionFailedException.class)
   public void testLinearRegresssionOnWrongFeatureVector() throws PredictionFailedException, TrainModelsFailedException {
      LinearRegression linearRegression = new LinearRegression();

      ILearningModel<?> learningModel = linearRegression.train(getCorrectDatasetList().get(0));

      double[] features = { 2, 3, 4, 5 };
      IInstance<double[], NullType, Double> instance = new BaselearnerInstance(features, 2);
      learningModel.predict(instance);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> returnList = new ArrayList<IDataset<?, ?, ?>>();
      returnList.add(this.createDatasetOutOfFile(new InstanceRankingDatasetParser(), getTestRessourcePathFor(DATASET_FILE)));
      return returnList;

   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> returnList = new ArrayList<IDataset<double[], NullType, Double>>();

      // Create a correct dataset
      double[][] datasetEntries = { { 0.0 }, { 1.0 }, { 2.0 }, { 3.0 }, { 4.0 }, { 5.0 }, { 6.0 }, { 7.0 }, { 8.0 }, { 9.0 } };

      double[] correctResults = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
      BaselearnerDataset dataset = new BaselearnerDataset(datasetEntries.length, 1);
      for (int i = 0; i < datasetEntries.length; i++) {
         dataset.addFeatureVectorWithResult(datasetEntries[i], correctResults[i]);
      }

      returnList.add(dataset);
      return returnList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {

      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> finalList = new ArrayList<Pair<IDataset<double[], NullType, Double>, List<Double>>>();

      List<Double> returnList = Arrays.asList(5.0, 6.0, 7.0, 8.0, 9.0);

      // Predictions are part of correct dataset
      double[][] datasetEntries = { { 0.0 }, { 1.0 }, { 2.0 }, { 3.0 }, { 4.0 } };

      BaselearnerDataset returnDataset = new BaselearnerDataset(datasetEntries.length, 1);
      for (int i = 0; i < datasetEntries.length; i++) {
         returnDataset.addFeatureVectorWithoutResult(datasetEntries[i]);
      }

      finalList.add(Pair.of(returnDataset, returnList));
      return finalList;

   }

}
