package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.AClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Abstract test class for {@link ALinearClassification} implementations.
 * 
 * @author Tanja Tornede
 */
public abstract class ALinearClassificationTest extends AClassificationTest<double[], NullType, Double> {

   private static final String CONFIGURATION_PARAMETER_NUMBER_OF_ITERATIONS = "number_of_iterations";
   private static final String CONFIGURATION_PARAMETER_LEARNING_RATE = "learning_rate";

   private static final String RESOURCE_DIRECTORY_LEVEL = "linear" + File.separator;

   protected static String DEFAULT_LINEAR_CLASSIFICATION_CONFIGURATION_FILE_NAME = "linear_classification.json";
   protected static String WRONG_DATASET_NAME = "wrongdataset.gprf";


   /**
    * Creates a new test for linear classification algorithms with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ALinearClassificationTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Creates a new test for linear classification algorithms.
    */
   public ALinearClassificationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public void setupTest() {
      super.setupTest();
      RandomGenerator.initializeRNG(2345);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> datsetList = new ArrayList<>();
      datsetList.add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_NAME)));
      return datsetList;
   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> datsetList = new ArrayList<>();
      IDataset<double[], NullType, Double> baselearnerDataset = createBaselearnerDataset();
      datsetList.add(baselearnerDataset);
      return datsetList;
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
    * @return a sorted list of all ratings of the instances in the given dataset
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
      String[] parameterNames = { CONFIGURATION_PARAMETER_LEARNING_RATE, CONFIGURATION_PARAMETER_NUMBER_OF_ITERATIONS };
      double[] parameterValues = { 0.1, 40 };

      List<JsonObject> parameterList = new ArrayList<>();
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i])));
      }

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      String[] parameterNames = { CONFIGURATION_PARAMETER_LEARNING_RATE, CONFIGURATION_PARAMETER_NUMBER_OF_ITERATIONS };
      double[] parameterValues = { 0, 0 };
      String[] x = { TestUtils.getStringByReflectionSafely(LinearClassificationConfiguration.class, "ERROR_LEARNING_RATE_NOT_POSITIVE"),
            TestUtils.getStringByReflectionSafely(LinearClassificationConfiguration.class, "ERROR_NUMBER_ITERATIONS_NOT_GREATER_ONE") };

      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(x[i], JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      return parameterList;
   }


   /**
    * Creates a data set for which holds that a feature vector with an 1 at the end of the vector is
    * interpreted as positive.
    * 
    * @return a small {@link BaselearnerDataset}
    */
   protected abstract BaselearnerDataset createBaselearnerDataset();

}
