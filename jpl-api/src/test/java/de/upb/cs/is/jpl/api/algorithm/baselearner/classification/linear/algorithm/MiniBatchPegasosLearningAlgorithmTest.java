package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.algorithm;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.ALinearClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.minibatchpegasos.MiniBatchPegasosConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.minibatchpegasos.MiniBatchPegasosLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Test for the {@link MiniBatchPegasosLearningAlgorithm}.
 * 
 * @author Tanja Tornede
 *
 */
public class MiniBatchPegasosLearningAlgorithmTest extends ALinearClassificationTest {

   private static final String REFLECTION_MINIBATCH_PEGASOS_CONFIGURATION_ERROR_REGULARIZATION_PARAMETER_IS_NOT_POSITIVE_VALUE = "ERROR_REGULARIZATION_PARAMETER_IS_NOT_POSITIVE_VALUE";
   private static final String REFLECTION_MINIBATCH_PEGASOS_CONFIGURATION_ERROR_SIZE_OF_SUBSET_IS_TO_SMALL = "ERROR_SIZE_OF_SUBSET_IS_TO_SMALL";
   private static final String CONFIGURATION_PARAMETER_SUBSET_SIZE = "subset_size";
   private static final String CONFIGURATION_PARAMETER_REGULATIZATION_PARAMETER = "regularization_parameter";


   /**
    * Creates a new unit test for the {@link MiniBatchPegasosLearningAlgorithm}.
    */
   public MiniBatchPegasosLearningAlgorithmTest() {
      super();
   }


   @Override
   public void setupTest() {
      super.setupTest();
      RandomGenerator.initializeRNG(2345);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new MiniBatchPegasosLearningAlgorithm();
   }


   /**
    * Creates a data set for which holds that a feature vector with an 1 at the end of the vector is
    * interpreted as positive.
    * 
    * @return a small {@link BaselearnerDataset}
    */
   @Override
   protected BaselearnerDataset createBaselearnerDataset() {
      double[][] trainingInstances = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 1 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 }, { 1, 1, 1 } };
      double[] correctResults = { 1, 1, 1, -1, -1, -1, -1 };
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(trainingInstances.length, trainingInstances[0].length);
      for (int i = 0; i < correctResults.length; i++) {
         baselearnerDataset.addFeatureVectorWithResult(trainingInstances[i], correctResults[i]);
      }

      return baselearnerDataset;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> datasetList = new ArrayList<>();
      BaselearnerDataset dataset = createBaselearnerDataset();
      List<Double> ratings = getRatingsForDataset(dataset);
      datasetList.add(Pair.of(dataset, ratings));
      return datasetList;
   }


   /**
    * Returns a sorted list of all ratings, in the same order as the instances are in the dataset.
    * 
    * @param dataset the dataset to get the ratings from
    * 
    * @return an sorted list of all ratings of the instances in the given dataset
    */
   private List<Double> getRatingsForDataset(BaselearnerDataset dataset) {
      List<Double> ratings = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         BaselearnerInstance instance = dataset.getInstance(i);
         ratings.add(instance.getRating());
      }
      return ratings;
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> parameterList = super.getCorrectParameters();
      String[] parameterNames = { CONFIGURATION_PARAMETER_SUBSET_SIZE, CONFIGURATION_PARAMETER_REGULATIZATION_PARAMETER };
      double[] parameterValues = { 1, 0.01 };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i])));
      }

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> parameterList = super.getWrongParameters();

      String[] parameterNames = { CONFIGURATION_PARAMETER_SUBSET_SIZE, CONFIGURATION_PARAMETER_REGULATIZATION_PARAMETER };
      double[] parameterValues = { 0, 0 };
      String[] x = {
            TestUtils.getStringByReflectionSafely(MiniBatchPegasosConfiguration.class,
                  REFLECTION_MINIBATCH_PEGASOS_CONFIGURATION_ERROR_SIZE_OF_SUBSET_IS_TO_SMALL),
            TestUtils.getStringByReflectionSafely(MiniBatchPegasosConfiguration.class,
                  REFLECTION_MINIBATCH_PEGASOS_CONFIGURATION_ERROR_REGULARIZATION_PARAMETER_IS_NOT_POSITIVE_VALUE) };

      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(x[i], JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      return parameterList;
   }

}
