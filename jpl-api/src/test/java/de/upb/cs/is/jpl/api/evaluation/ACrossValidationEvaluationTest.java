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
 * The test template for the evaluations class. If there is any new cross validation evaluation
 * implementation, then this class needs to extend this class to test the basic functionality of the
 * cross validation evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public abstract class ACrossValidationEvaluationTest extends ACommonEvaluationTestForPercentageSplitAndCrossValidation {


   @Override
   public List<JsonObject> getCorrectParameters() {
      List<JsonObject> parameterList = new ArrayList<>();
      parameterList.add(JsonUtils.createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.FOLDS_CROSS_VALIDATION, String.valueOf(4)));
      return parameterList;
   }


   @Override
   public List<Pair<String, JsonObject>> getWrongParameters() {
      String errorMessage = String.format(
            TestUtils.getStringByReflectionSafely(ACrossValidationEvaluationConfiguration.class, "VALIDATION_FOLD_ERROR_MESSAGE"), -3);

      List<Pair<String, JsonObject>> parameterList = new ArrayList<>();
      parameterList.add(Pair.of(errorMessage,
            JsonUtils.createJsonObjectFromKeyAndValue(EvaluationsKeyValuePairs.FOLDS_CROSS_VALIDATION, String.valueOf(-3))));
      return parameterList;
   }


   /**
    * Creates a new unit test for cross validation evaluations with the additional path to the
    * resources given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ACrossValidationEvaluationTest(String additionalResourcePath) {
      super(additionalResourcePath);
   }


   /**
    * Splits the dataset into a list of testing and training dataset pairs by folding it into equal
    * chunks and selecting one of these as testing data and the rest as training data.
    * 
    * @param dataset the dataset on which these operations are performed
    * @param folds the number of folds for cross validation
    * @return a list of test- and training-dataset pairs
    * @throws TrainTestDatasetPairsNotCreated if the dataset could not be folded
    */
   protected List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairsForFolds(IDataset<?, ?, ?> dataset, int folds)
         throws TrainTestDatasetPairsNotCreated {
      ACrossValidationEvaluationConfiguration config = (ACrossValidationEvaluationConfiguration) evaluation.getEvaluationConfiguration();
      config.setFolds(folds);
      evaluation.setEvaluationConfiguration(config);
      return evaluation.getTestTrainPairs(dataset);

   }


   /**
    * Creates the list of {@link EvaluationSetting} for each pair of test-train pair of dataset
    * produced for each fold for cross validation.
    * 
    * @param setNumber the set number
    * @param numberOfFolds the number of folds for cross validation
    * @param datasetFilePath the dataset file path
    * @param learningAlgorithm the learning algorithm
    * @return the list of evaluation settings
    * @throws TrainTestDatasetPairsNotCreated if the dataset could not be folded
    */
   protected Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult> createEvaluationSettingsWithResultForFolds(int setNumber,
         int numberOfFolds, String datasetFilePath, ILearningAlgorithm learningAlgorithm) throws TrainTestDatasetPairsNotCreated {
      List<EvaluationSetting> settings = new ArrayList<>();
      IDataset<?, ?, ?> dataset = this.createDatasetOutOfFile(learningAlgorithm.getDatasetParser(), datasetFilePath);
      List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> testAndTrainPairs = getTestTrainPairsForFolds(dataset, numberOfFolds);
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
            Pair<Pair<Integer, List<EvaluationSetting>>, EvaluationResult> settings = createEvaluationSettingsWithResultForFolds(j, 4,
                  getTestRessourcePathFor(datasetName), learningAlgorithm);
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
