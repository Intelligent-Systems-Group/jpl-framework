package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.AClassificationTest;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The unit test for {@link SupportVectorMachineClassification}
 * 
 * @author Pritha Gupta
 *
 */
public class SupportVectorMachineClassificationTest extends AClassificationTest<double[], NullType, Double> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "svm" + File.separator;
   private static final String WRONG_DATASET_NAME = "wrongdataset.gprf";
   private static final String CONFIGURATION_KERNEL_TYPE = "kernel_type";
   private static final String CONFIGURATION_PROBABILTY = "probability";
   private static final String CONFIGURATION_SHRINING = "shrinking";


   /**
    * Creates a new unit test for the {@link SupportVectorMachineClassification}.
    */
   public SupportVectorMachineClassificationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public ITrainableAlgorithm getTrainableAlgorithm() {
      return new SupportVectorMachineClassification();
   }


   @Override
   public List<JsonObject> getCorrectParameters() {
      String[] parameterNames = { CONFIGURATION_KERNEL_TYPE, CONFIGURATION_SHRINING, CONFIGURATION_PROBABILTY };
      int[] parameterValues = { 2, 1, 1 };

      List<JsonObject> parameterList = new ArrayList<>();
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i])));
      }

      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      String[] parameterNames = { CONFIGURATION_KERNEL_TYPE, CONFIGURATION_PROBABILTY, CONFIGURATION_SHRINING, };
      int[] parameterValues = { 4, -1, -1 };
      String[] x = { TestUtils.getStringByReflectionSafely(SupportVectorMachineConfiguration.class, "INVALID_KERNEL_TYPE"),
            TestUtils.getStringByReflectionSafely(SupportVectorMachineConfiguration.class, "INVALID_PROBABILITY"),
            TestUtils.getStringByReflectionSafely(SupportVectorMachineConfiguration.class, "INVALID_SHRINKING_HEURISTICS") };

      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      for (int i = 0; i < parameterNames.length; i++) {
         parameterList.add(Pair.of(x[i], JsonUtils.createJsonObjectFromKeyAndValue(parameterNames[i], String.valueOf(parameterValues[i]))));
      }

      return parameterList;
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> datsetList = new ArrayList<>();
      datsetList.add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_NAME)));
      return datsetList;
   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      BaselearnerDataset dataset = createBaselearnerDatasetForTraining();
      List<IDataset<double[], NullType, Double>> correctDatasetList = new ArrayList<>();
      correctDatasetList.add(dataset);
      return correctDatasetList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      BaselearnerDataset dataset = createBaselearnerDatasetForTraining();

      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> correctDatasetList = new ArrayList<>();
      double[] correctPreictions = { -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
            1.0, 1.0 };
      List<Double> correctPredictions = Arrays.asList(ArrayUtils.toObject(correctPreictions));

      correctDatasetList.add(Pair.of(dataset, correctPredictions));

      return correctDatasetList;
   }


   /**
    * Creates a dummy {@link BaselearnerDataset} for training.
    * 
    * @return a correct {@link BaselearnerDataset} for training
    */
   private BaselearnerDataset createBaselearnerDatasetForTraining() {
      double[][] datasetEntries = { { 0.50, 0.75 }, { 0.75, 0.75 }, { 1.00, 0.75 }, { 1.25, 0.75 }, { 1.50, 0.75 }, { 1.75, 0.75 },
            { 1.75, 0.75 }, { 2.00, 0.75 }, { 2.25, 0.75 }, { 2.50, 0.75 }, { 2.75, 0.75 }, { 3.00, 0.75 }, { 3.25, 0.75 }, { 3.50, 0.75 },
            { 4.00, 0.75 }, { 4.25, 0.75 }, { 4.50, 0.75 }, { 4.75, 0.75 }, { 5.00, 0.75 }, { 5.50, 0.75 } };

      double[] correctResults = { -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, 1, 1, 1, 1, 1, 1, 1, 1 };

      BaselearnerDataset dataset = new BaselearnerDataset(datasetEntries.length, 2);
      for (int i = 0; i < datasetEntries.length; i++) {
         dataset.addFeatureVectorWithResult(datasetEntries[i], correctResults[i]);
      }
      return dataset;
   }


}
