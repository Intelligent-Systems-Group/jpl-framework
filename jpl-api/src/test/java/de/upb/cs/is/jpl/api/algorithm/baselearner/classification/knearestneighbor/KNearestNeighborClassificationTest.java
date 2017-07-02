package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.DefaultRelativeDataset;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Tests for {@link KNearestNeighborClassification}.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class KNearestNeighborClassificationTest extends ABaselearnerTest<double[], NullType, Double> {

   private static final String CHECK_VALUE_OF_K = "Check value of k:";
   private static final String RESOURCE_DIRECTORY_LEVEL = "knearestneighbor" + File.separator;


   /**
    * Creates the {@link KNearestNeighborClassificationTest}.
    */
   public KNearestNeighborClassificationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Test whether the default configuration is correctly set.
    */
   @Test
   public void testDefaultConfiguration() {
      KNearestNeighborClassification classifier = new KNearestNeighborClassification();
      KNearestNeighborConfiguration configuration = (KNearestNeighborConfiguration) classifier.getDefaultAlgorithmConfiguration();
      Assert.assertEquals(CHECK_VALUE_OF_K, 3, configuration.getNumberOfNeighbors());
   }


   /**
    * Create a {@link BaselearnerDataset} without prediction results.
    * 
    * @return the dataset for which the predictions should be made
    */
   private BaselearnerDataset getPredictionsDataset() {
      double[][] trainingInstances = { { 0.0, 0.1 }, { 0.0, 0.5 }, { 0.0, 0.9 }, { 1.0, 0.1 }, { 1.0, 0.5 }, { 1.0, 0.9 }, { 0.4, 0.5 },
            { 0.6, 0.5 } };

      BaselearnerDataset dataset = new BaselearnerDataset(trainingInstances.length, trainingInstances[0].length);
      for (int i = 0; i < trainingInstances.length; i++) {
         dataset.addFeatureVectorWithoutResult(trainingInstances[i]);
      }
      return dataset;
   }


   /**
    * Create the {@link List} of correct predictions for the {@link BaselearnerDataset} provided by
    * {@link KNearestNeighborClassificationTest#getPredictionsDataset()}. The order of the predictions is the same
    * order as the instances in the dataset.
    * 
    * @return the correct predictions for the datasets provided by
    *         {@link KNearestNeighborClassificationTest#getPredictionsDataset()}
    */
   private List<Double> getPredictions() {
      double[] predictionsArray = { 0, 0, 0, 1, 1, 1, 0, 1 };
      List<Double> predictions = new ArrayList<>();
      for (int i = 0; i < predictionsArray.length; i++) {
         predictions.add(predictionsArray[i]);
      }
      return predictions;
   }


   /**
    * Create a {@link BaselearnerDataset} which includes the correct classifications and is used in
    * the training operation.
    * 
    * @return the dataset which includes the training data
    */
   private BaselearnerDataset getTrainingDataset() {
      double[][] trainingInstances = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 0.4, 0.4 }, { 0.4, 0.4 }, { 0.6, 0.6 }, { 0.6, 0.6 } };
      double[] correctResults = { 0, 0, 1, 1, 0, 0, 1, 1 };

      BaselearnerDataset dataset = new BaselearnerDataset(correctResults.length, trainingInstances[0].length);
      for (int i = 0; i < correctResults.length; i++) {
         dataset.addFeatureVectorWithResult(trainingInstances[i], correctResults[i]);
      }
      return dataset;
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new KNearestNeighborClassification();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> datasetList = new ArrayList<>();
      datasetList.add(new DefaultRelativeDataset());
      return datasetList;

   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> datasetList = new ArrayList<>();
      datasetList.add(getTrainingDataset());
      return datasetList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> datasetList = new ArrayList<>();
      datasetList.add(Pair.of(getPredictionsDataset(), getPredictions()));
      return datasetList;

   }


}
