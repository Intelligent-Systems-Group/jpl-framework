package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankLearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDataset;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingDatasetParser;
import de.upb.cs.is.jpl.api.dataset.instanceranking.InstanceRankingInstance;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Unit tests for {@link PerceptronRankLearningAlgorithm}.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class PerceptronRankLearningAlgorithmTest extends ALearningAlgorithmTest<double[], NullType, Integer> {
   private static final String RESOURCE_DIRECTORY_LEVEL = "instanceranking" + File.separator;
   private static final String DATASET_FILE = "instancemovielens.gprf";
   private static final String ERROR_REFLECTION_FAILED = "Test can not run because reflection failed";
   private static final String ERROR_FAILED_TO_ADD_INSTANCES = "Failed to add instances to prediction list. Problem: ";
   private static final Logger logger = LoggerFactory.getLogger(PerceptronRankLearningAlgorithm.class);

   private static final String PARAMETER_K = "k";
   private static final String PARAMETER_K_REFLECTION_VARIABLE = "WRONG_VALUE_FOR_K";


   /**
    * Creates a new unit test for instance ranking algorithms.
    */
   public PerceptronRankLearningAlgorithmTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new PerceptronRankLearningAlgorithm();
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> returnList = new ArrayList<>();
      returnList.add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(DATASET_FILE)));
      return returnList;
   }


   @SuppressWarnings("unchecked")
   @Override
   public List<IDataset<double[], NullType, Integer>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Integer>> returnList = new ArrayList<>();
      returnList.add((IDataset<double[], NullType, Integer>) this.createDatasetOutOfFile(new InstanceRankingDatasetParser(),
            getTestRessourcePathFor(DATASET_FILE)));
      return returnList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Integer>, List<Integer>>> getPredictionsForDatasetList() {
      // No predictions defined
      List<Pair<IDataset<double[], NullType, Integer>, List<Integer>>> returnList = new ArrayList<>();
      IDataset<double[], NullType, Integer> dataset = new InstanceRankingDataset();
      List<Integer> predictionList = new LinkedList<>();

      try {
         double[] predictionFeatures = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
         dataset.addInstance(new InstanceRankingInstance(100, predictionFeatures, -1));
         predictionList.add(1);
         double[] predictionFeatures2 = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
         dataset.addInstance(new InstanceRankingInstance(101, predictionFeatures2, -1));
         predictionList.add(3);
         double[] predictionFeatures3 = { 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0 };
         dataset.addInstance(new InstanceRankingInstance(102, predictionFeatures3, -1));
         predictionList.add(1);
      } catch (InvalidInstanceException e) {
         Assert.fail(ERROR_FAILED_TO_ADD_INSTANCES + e.getMessage());
      }

      returnList.add(Pair.of(dataset, predictionList));
      return returnList;
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> returnList = new ArrayList<>();

      // Add correct combinations which should be tested
      returnList.add(JsonUtils.createJsonObjectFromKeyAndValue(PARAMETER_K, "3"));

      return returnList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      List<Pair<String, JsonObject>> returnList = new ArrayList<>();

      // Add correct combinations which should be tested
      try {
         returnList.add(Pair.of(TestUtils.getStringByReflection(PerceptronRankConfiguration.class, PARAMETER_K_REFLECTION_VARIABLE),
               JsonUtils.createJsonObjectFromKeyAndValue(PARAMETER_K, "-7")));
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         Assert.fail(ERROR_REFLECTION_FAILED);
         logger.error(ERROR_REFLECTION_FAILED);
      }

      return returnList;
   }
}
