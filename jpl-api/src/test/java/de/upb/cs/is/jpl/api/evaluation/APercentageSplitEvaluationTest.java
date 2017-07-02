package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.util.JsonUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The test template for the evaluations class. If there is any new percentage-split evaluation
 * implementation, then this class needs to extend this class to test the basic functionality of the
 * percentage-split evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class APercentageSplitEvaluationTest extends ACommonEvaluationTestForPercentageSplitAndCrossValidation {


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> parameterList = new ArrayList<>();
      parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.PERCENTAGE_FOR_EVALUATION, String.valueOf(0.4)));
      parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.NUMBER_OF_ITERATIONS_FOR_PERCENATGE_SPLIT,
            String.valueOf(10)));
      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {

      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      String errorMessage = String.format(
            TestUtils.getStringByReflectionSafely(APercentageSplitEvaluationConfiguration.class, "VALIDATION_PERCENTAGE_ERROR_MESSAGE"),
            1.0);

      parameterList.add(Pair.of(errorMessage,
            JsonUtils.createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.PERCENTAGE_FOR_EVALUATION, String.valueOf(1.0))));

      errorMessage = String.format(TestUtils.getStringByReflectionSafely(APercentageSplitEvaluationConfiguration.class,
            "VALIDATION_NUMBER_OF_DATASETS_ERROR_MESSAGE"), -3);

      parameterList.add(Pair.of(errorMessage, JsonUtils
            .createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.NUMBER_OF_ITERATIONS_FOR_PERCENATGE_SPLIT, String.valueOf(-3))));


      return parameterList;
   }


   /**
    * Creates a new unit test for percentage split validation evaluations with the additional path
    * to the resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public APercentageSplitEvaluationTest(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   /**
    * This method splits the dataset according to the provided percentage of split in the
    * {@link APercentageSplitEvaluationConfiguration} and create such number of dataset pairs for
    * the provided number of dataset pairs.
    * 
    * @param dataset the dataset on which these operations are performed
    * @param numberOfDatasets the number of datasets pairs to be generated
    * @param percentage the value for the percentage split
    * @return a list of test- and training-dataset pairs
    * @throws TrainTestDatasetPairsNotCreated if the dataset could not be folded
    */
   protected List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairsForPercentageSplit(IDataset<?, ?, ?> dataset,
         int numberOfDatasets, float percentage) throws TrainTestDatasetPairsNotCreated {
      APercentageSplitEvaluationConfiguration config = (APercentageSplitEvaluationConfiguration) evaluation.getEvaluationConfiguration();
      config.setPercentage(percentage);
      config.setNumOfDatasets(numberOfDatasets);
      evaluation.setEvaluationConfiguration(config);
      return evaluation.getTestTrainPairs(dataset);

   }


   /**
    * Creates the list of {@link EvaluationSetting} for each pair of test-train dataset from the
    * list of pairs as number of dataset produced for provided percentage of split
    * 
    * @param setNumber the set number
    * @param numberOfDatasets the number of shuffles pair of dataset for a percentage of split
    * @param percentage the percentage for split
    * @param filePath the dataset file path
    * @param learningAlgorithm the learning algorithm
    * @return the list of evaluation settings
    * @throws TrainTestDatasetPairsNotCreated if the dataset could not be folded
    */
   protected Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult> createEvaluationSettingsWithResultForPercentageSplit(
         int setNumber, int numberOfDatasets, float percentage, String filePath, ILearningAlgorithm learningAlgorithm)
         throws TrainTestDatasetPairsNotCreated {
      List<EvaluationSetting> settings = new ArrayList<>();
      IDataset<?, ?, ?> dataset = this.createDatasetOutOfFile(learningAlgorithm.getDatasetParser(), filePath);
      List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testAndTrainPairs = getTestTrainPairsForPercentageSplit(dataset, numberOfDatasets,
            percentage);
      for (int i = 0; i < testAndTrainPairs.size(); i++) {
         Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>> testAndTrainPair = testAndTrainPairs.get(i);
         ILearningModel<?> learningModel = getLearningModelForAlgorithmOnDataset(learningAlgorithm, testAndTrainPair.getSecond());
         EvaluationSetting evaluationSetting = new EvaluationSetting(testAndTrainPair.getFirst(), learningAlgorithm, learningModel,
               evaluationMetrics);
         settings.add(evaluationSetting);
      }
      Pair<Integer, List<EvaluationSetting>> evaluationSettingsSet = Pair.of(setNumber, settings);
      EvaluationResult evaluationResult = new EvaluationResult();
      evaluationResult.setDataset(dataset);
      evaluationResult.setLearningAlgorithm(learningAlgorithm);
      return Pair.of(evaluationSettingsSet, evaluationResult);
   }


   @Override
   protected List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> getListOfEvaluationSettingsSetWithEvaluationResultForMetricResults(
         double[][] metricResults, String datasetName, String evaluationOutput) {
      List<Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult>> listOfPairs = new ArrayList<>();
      List<ILearningAlgorithm> learningAlgorithms = getLearningAlgorithms();
      for (int j = 0; j < learningAlgorithms.size(); j++) {
         ILearningAlgorithm learningAlgorithm = learningAlgorithms.get(j);
         try {
            Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult> settings = createEvaluationSettingsWithResultForPercentageSplit(
                  j, 4, 0.7f, getTestRessourcePathFor(datasetName), learningAlgorithm);
            for (int i = 0; i < evaluationMetrics.size(); i++) {
               settings.getSecond().addLossWithMetric(metricResults[j][i], evaluationMetrics.get(i));
            }
            settings.getSecond().setExtraEvaluationInformation(evaluationOutput);
            listOfPairs.add(settings);
         } catch (TrainTestDatasetPairsNotCreated e) {
            Assert.fail(COULD_NOT_SPLIT_THE_DATASET_INTO_TRAINING_AND_TESTING_DATSET);
         }
      }
      return listOfPairs;
   }
}
